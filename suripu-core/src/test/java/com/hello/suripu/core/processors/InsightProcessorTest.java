package com.hello.suripu.core.processors;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hello.suripu.core.db.AccountDAO;
import com.hello.suripu.core.db.AggregateSleepScoreDAODynamoDB;
import com.hello.suripu.core.db.CalibrationDAO;
import com.hello.suripu.core.db.DeviceDAO;
import com.hello.suripu.core.db.DeviceDataDAODynamoDB;
import com.hello.suripu.core.db.InsightsDAODynamoDB;
import com.hello.suripu.core.db.MarketingInsightsSeenDAODynamoDB;
import com.hello.suripu.core.db.SleepStatsDAODynamoDB;
import com.hello.suripu.core.db.TrendsInsightsDAO;
import com.hello.suripu.core.db.responses.Response;
import com.hello.suripu.core.flipper.FeatureFlipper;
import com.hello.suripu.core.insights.InsightsLastSeen;
import com.hello.suripu.core.insights.InsightsLastSeenDAO;
import com.hello.suripu.core.models.AggregateScore;
import com.hello.suripu.core.models.AggregateSleepStats;
import com.hello.suripu.core.models.DeviceAccountPair;
import com.hello.suripu.core.models.DeviceData;
import com.hello.suripu.core.models.DeviceId;
import com.hello.suripu.core.models.DeviceStatus;
import com.hello.suripu.core.models.Insights.InfoInsightCards;
import com.hello.suripu.core.models.Insights.InsightCard;
import com.hello.suripu.core.models.Insights.MarketingInsightsSeen;
import com.hello.suripu.core.models.MotionScore;
import com.hello.suripu.core.models.SleepStats;
import com.hello.suripu.core.preferences.AccountPreferencesDAO;
import com.hello.suripu.core.processors.insights.LightData;
import com.hello.suripu.core.processors.insights.WakeStdDevData;
import com.librato.rollout.RolloutClient;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;

/**
 * Created by jyfan on 9/4/15.
 */
public class InsightProcessorTest {



    private final Long FAKE_ACCOUNT_ID = 9999L;
    private final DeviceId FAKE_DEVICE_ID_INT = DeviceId.create(9998L);
    private final DeviceId FAKE_DEVICE_ID_EXT = DeviceId.create("XYZBLAH");
    private final DeviceAccountPair FAKE_DEVICE_ACCOUNT_PAIR = new DeviceAccountPair(FAKE_ACCOUNT_ID, FAKE_DEVICE_ID_INT.internalDeviceId.get(), FAKE_DEVICE_ID_EXT.externalDeviceId.get(), DateTime.parse("2015-01-01"));


    private final DateTime FAKE_SATURDAY = DateTime.parse("2015-09-05").withTimeAtStartOfDay();
    private final DateTime FAKE_FRIDAY = DateTime.parse("2015-09-04").withTimeAtStartOfDay();

    private final DateTime FAKE_DATE_1 = DateTime.parse("2015-09-01").withTimeAtStartOfDay();
    private final DateTime FAKE_DATE_10 = DateTime.parse("2015-09-10").withTimeAtStartOfDay();
    private final DateTime FAKE_DATE_13 = DateTime.parse("2015-09-13").withTimeAtStartOfDay();
    private final DateTime FAKE_DATE_NONE = DateTime.parse("2015-09-11").withTimeAtStartOfDay();


    private DeviceDataDAODynamoDB deviceDataDAODynamoDB;

    private static final ImmutableSet<InsightCard.Category> marketingInsightPool = ImmutableSet.copyOf(Sets.newHashSet(InsightCard.Category.DRIVE,
            InsightCard.Category.EAT,
            InsightCard.Category.LEARN,
            InsightCard.Category.LOVE,
            InsightCard.Category.PLAY,
            InsightCard.Category.RUN,
            InsightCard.Category.SWIM,
            InsightCard.Category.WORK));

    private static final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet(InsightCard.Category.DRIVE);

    private static RolloutClient featureFlipOn() {
        final Long FAKE_ACCOUNT_ID = 9999L;

        RolloutClient mockFeatureFlipper = Mockito.mock(RolloutClient.class);
        Mockito.when(mockFeatureFlipper.userFeatureActive(FeatureFlipper.INSIGHTS_HUMIDITY, FAKE_ACCOUNT_ID, Collections.EMPTY_LIST)).thenReturn(Boolean.TRUE);

        return mockFeatureFlipper;
    }

    private static RolloutClient featureFlipMarketingScheduleOn() {
        final Long FAKE_ACCOUNT_ID = 9999L;

        RolloutClient mockFeatureFlipper = Mockito.mock(RolloutClient.class);
        Mockito.when(mockFeatureFlipper.userFeatureActive(FeatureFlipper.INSIGHTS_MARKETING_SCHEDULE, FAKE_ACCOUNT_ID, Collections.EMPTY_LIST)).thenReturn(Boolean.TRUE);

        return mockFeatureFlipper;
    }

    private static RolloutClient featureFlipOff() {
        final Long FAKE_ACCOUNT_ID = 9999L;

        RolloutClient mockFeatureFlipper = Mockito.mock(RolloutClient.class);
        Mockito.when(mockFeatureFlipper.userFeatureActive(FeatureFlipper.INSIGHTS_HUMIDITY, FAKE_ACCOUNT_ID, Collections.EMPTY_LIST)).thenReturn(Boolean.FALSE);

        Mockito.when(mockFeatureFlipper.userFeatureActive(FeatureFlipper.INSIGHTS_MARKETING_SCHEDULE, FAKE_ACCOUNT_ID, Collections.EMPTY_LIST)).thenReturn(Boolean.FALSE);

        return mockFeatureFlipper;
    }

    private InsightProcessor setUp() {

        final Long FAKE_ACCOUNT_ID = 9999L;
        final Long FAKE_DEVICE_ID_EXT = 9998L;

        //No Marketing Insights seen yet
        final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet();

        deviceDataDAODynamoDB = Mockito.mock(DeviceDataDAODynamoDB.class);
        final DeviceDAO deviceDAO = Mockito.mock(DeviceDAO.class);
        Mockito.when(deviceDAO.getMostRecentSenseByAccountId(FAKE_ACCOUNT_ID)).thenReturn(Optional.of(FAKE_DEVICE_ID_EXT));
        final TrendsInsightsDAO trendsInsightsDAO = Mockito.mock(TrendsInsightsDAO.class);
        final AggregateSleepScoreDAODynamoDB scoreDAODynamoDB = Mockito.mock(AggregateSleepScoreDAODynamoDB.class);
        final InsightsDAODynamoDB insightsDAODynamoDB = Mockito.mock(InsightsDAODynamoDB.class);
        final InsightsLastSeenDAO insightsLastSeenDAO = Mockito.mock(InsightsLastSeenDAO.class);
        final SleepStatsDAODynamoDB sleepStatsDAODynamoDB = Mockito.mock(SleepStatsDAODynamoDB.class);
        final AccountPreferencesDAO preferencesDAO = Mockito.mock(AccountPreferencesDAO.class);
        final AccountDAO accountDAO = Mockito.mock(AccountDAO.class);
        final LightData lightData = Mockito.mock(LightData.class);
        final WakeStdDevData wakeStdDevData = Mockito.mock(WakeStdDevData.class);
        final CalibrationDAO calibrationDAO = Mockito.mock(CalibrationDAO.class);
        final AccountInfoProcessor accountInfoProcessor = Mockito.mock(AccountInfoProcessor.class);
        final MarketingInsightsSeenDAODynamoDB marketingInsightsSeenDAODynamoDB = Mockito.mock(MarketingInsightsSeenDAODynamoDB.class);

        //Prepping for taking care of @NotNull check for light
        final int light = 2;
        final int zeroLight = 0;
        final DateTime timestamp = DateTime.now(DateTimeZone.UTC).withHourOfDay(19).withMinuteOfHour(0);
        final int offsetMillis = -28800000;
        final List<DeviceData> data = Lists.newArrayList();
        data.add(new DeviceData(FAKE_ACCOUNT_ID, FAKE_DEVICE_ID_EXT, 0, 0, 0, 0, 0, 0, 0, light,light, 0, 0, timestamp, offsetMillis, 1, 1, 1, 0, 0, 0, 0));
        data.add(new DeviceData(FAKE_ACCOUNT_ID, FAKE_DEVICE_ID_EXT, 0, 0, 0, 0, 0, 0, 0, light + 1,light + 1, 0, 0, timestamp.withMinuteOfHour(10), offsetMillis, 1, 1, 1, 0, 0, 0, 0));
        data.add(new DeviceData(FAKE_ACCOUNT_ID, FAKE_DEVICE_ID_EXT, 0, 0, 0, 0, 0, 0, 0, light + 1,light + 1, 0, 0, timestamp.withMinuteOfHour(30), offsetMillis, 1, 1, 1, 0, 0, 0, 0));
        data.add(new DeviceData(FAKE_ACCOUNT_ID, FAKE_DEVICE_ID_EXT, 0, 0, 0, 0, 0, 0, 0, light,light, 0, 0, timestamp.withMinuteOfHour(45), offsetMillis, 1, 1, 1, 0, 0, 0, 0));
        data.add(new DeviceData(FAKE_ACCOUNT_ID, FAKE_DEVICE_ID_EXT, 0, 0, 0, 0, 0, 0, 0, zeroLight,zeroLight, 0, 0, timestamp.withHourOfDay(21), offsetMillis, 1, 1, 1, 0, 0, 0, 0));

        final List<InfoInsightCards> mockInfoInsightCardsList = Lists.newArrayList(Mockito.mock(InfoInsightCards.class));
        final DeviceStatus mockDeviceStatus = Mockito.mock(DeviceStatus.class);
        final AggregateScore mockAggregateScore = Mockito.mock(AggregateScore.class);
        final List<InsightCard> mockInsightCardList = Lists.newArrayList(Mockito.mock(InsightCard.class));

        final MotionScore fakeMotionScore = Mockito.mock(MotionScore.class);
        final SleepStats fakeSleepStat = Mockito.mock(SleepStats.class);
        final List<AggregateSleepStats> fakeAggregateSleepStatsList = Lists.newArrayList();
        fakeAggregateSleepStatsList.add(new AggregateSleepStats(FAKE_ACCOUNT_ID, timestamp, offsetMillis, 0, "1", fakeMotionScore, 0, 0, 0, fakeSleepStat));

        //Taking care of @NotNull check for light
        final Response<ImmutableList<DeviceData>> successfulResponse = Response.success(ImmutableList.copyOf(data));
        Mockito.when(deviceDataDAODynamoDB.getLightByBetweenHourDateByTS(Mockito.any(Long.class), Mockito.any(DeviceId.class), Mockito.any(Integer.class), Mockito.any(DateTime.class), Mockito.any(DateTime.class), Mockito.any(DateTime.class), Mockito.any(DateTime.class),Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(successfulResponse);

        Mockito.when(deviceDataDAODynamoDB.toString()).thenReturn("someString");
        Mockito.when(deviceDAO.getMostRecentSenseByAccountId(FAKE_ACCOUNT_ID)).thenReturn(Optional.of(FAKE_DEVICE_ID_EXT));
        Mockito.when(trendsInsightsDAO.getAllGenericInsightCards()).thenReturn(ImmutableList.copyOf(mockInfoInsightCardsList));
        Mockito.when(scoreDAODynamoDB.getSingleScore(FAKE_ACCOUNT_ID, "2015-09-14")).thenReturn(mockAggregateScore);
        Mockito.when(insightsDAODynamoDB.getInsightsByCategory(FAKE_ACCOUNT_ID, InsightCard.Category.LIGHT, 1)).thenReturn(ImmutableList.copyOf(mockInsightCardList));
        Mockito.when(sleepStatsDAODynamoDB.getBatchStats(Mockito.any(Long.class), Mockito.any(String.class), Mockito.any(String.class))).thenReturn(ImmutableList.copyOf(fakeAggregateSleepStatsList));

        Mockito.when(preferencesDAO.toString()).thenReturn("someString");
        Mockito.when(accountInfoProcessor.toString()).thenReturn("someString");
        Mockito.when(lightData.getLightPercentile(Mockito.any(Integer.class))).thenReturn(1);
        Mockito.when(wakeStdDevData.getWakeStdDevPercentile(Mockito.any(Integer.class))).thenReturn(1);

        //Taking care of @NotNull check for humidity
        Mockito.when(sleepStatsDAODynamoDB.getTimeZoneOffset(FAKE_ACCOUNT_ID)).thenReturn(Optional.of(offsetMillis));
        Mockito.when(deviceDataDAODynamoDB.getBetweenHourDateByTS(Mockito.any(Long.class), Mockito.any(DeviceId.class),Mockito.any(DateTime.class), Mockito.any(DateTime.class), Mockito.any(DateTime.class), Mockito.any(DateTime.class), Mockito.any(Integer.class), Mockito.any(Integer.class)))
                .thenReturn(successfulResponse);
        Mockito.when(insightsDAODynamoDB.getInsightsByCategory(FAKE_ACCOUNT_ID, InsightCard.Category.HUMIDITY, 1)).thenReturn(ImmutableList.copyOf(mockInsightCardList));

        //Specify which marketing insights have been seen (ever)
        Mockito.when(marketingInsightsSeenDAODynamoDB.getSeenCategories(FAKE_ACCOUNT_ID)).thenReturn(Optional.of(new MarketingInsightsSeen(marketingInsightsSeen, DateTime.now())));

        //Taking care of exception when updating marketing insights seen
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.DRIVE)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.EAT)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.LEARN)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.LOVE)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.PLAY)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.RUN)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.SWIM)).thenReturn(Boolean.TRUE);
        Mockito.when(marketingInsightsSeenDAODynamoDB.updateSeenCategories(FAKE_ACCOUNT_ID, InsightCard.Category.WORK)).thenReturn(Boolean.TRUE);

        //Initialize InsightProcessor
        final InsightProcessor insightProcessor = new InsightProcessor(deviceDataDAODynamoDB,
                deviceDAO,
                trendsInsightsDAO,
                scoreDAODynamoDB,
                insightsDAODynamoDB,
                insightsLastSeenDAO,
                sleepStatsDAODynamoDB,
                preferencesDAO,
                accountInfoProcessor,
                accountDAO,
                lightData,
                wakeStdDevData,
                calibrationDAO,
                marketingInsightsSeenDAODynamoDB);

        //only to get rid of null pointer exception
        final InsightCard insightCardMock = Mockito.mock(InsightCard.class);
        final ImmutableList<InsightCard> insightCardMockList = ImmutableList.copyOf(Lists.newArrayList(insightCardMock));
        Mockito.when(insightsDAODynamoDB.getInsightsByDate(FAKE_ACCOUNT_ID, DateTime.now().minusDays(7), Boolean.TRUE, 7)).thenReturn(insightCardMockList);
        
        return insightProcessor;
    }

    @Test
    public void test_generateNewUserInsights() {
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();

        final Optional<InsightCard.Category> something = spyInsightProcessor.generateNewUserInsights(FAKE_ACCOUNT_ID, 1, recentCategories);
        assertThat(something.get(), is(InsightCard.Category.GENERIC));
    }

    @Test
    public void test_generateNewUserInsights_2() {
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.GENERIC, DateTime.now());

        final Optional<InsightCard.Category> something = spyInsightProcessor.generateNewUserInsights(FAKE_ACCOUNT_ID, 1, recentCategories);
        assertThat(something.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void test_generateNewUserInsights_3() {
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.GENERIC, DateTime.now());

        final Optional<InsightCard.Category> something = spyInsightProcessor.generateNewUserInsights(FAKE_ACCOUNT_ID, 2, recentCategories);
        assertThat(something.get(), is(InsightCard.Category.SLEEP_HYGIENE));
    }

    @Test
    public void test_generateNewUserInsights_4() {
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.GENERIC, DateTime.now());

        final Optional<InsightCard.Category> something = spyInsightProcessor.generateNewUserInsights(FAKE_ACCOUNT_ID, 3, recentCategories);
        assertThat(something.get(), is(InsightCard.Category.SLEEP_DURATION));
    }

    @Test
    public void test_generateGeneralInsights() {

        final RolloutClient mockFeatureFlipper = featureFlipOff();
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.LIGHT, DateTime.now());
        recentCategories.put(InsightCard.Category.TEMPERATURE, DateTime.now());

        spyInsightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_SATURDAY, mockFeatureFlipper);

        //TEST - Look for weekly Insight, try to generate wake variance, get Optional.absent() b/c no data
        Mockito.verify(spyInsightProcessor).selectWeeklyInsightsToGenerate(recentCategories, FAKE_SATURDAY);
        Mockito.verify(spyInsightProcessor).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);

        final Optional<InsightCard.Category> wakeCardCategory = spyInsightProcessor.generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);
        assertThat(wakeCardCategory.isPresent(), is(Boolean.FALSE));

        //look for high priority Insight - get nothing

        //look for random old Insight - get nothing b/c wrong date
        Mockito.verify(spyInsightProcessor).selectRandomOldInsightsToGenerate(FAKE_ACCOUNT_ID, recentCategories, FAKE_SATURDAY, mockFeatureFlipper);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.LIGHT);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.TEMPERATURE);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.SLEEP_QUALITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.BED_LIGHT_DURATION);
    }

    @Test
    public void test_generateGeneralInsights_3() {

        final RolloutClient mockFeatureFlipper = featureFlipOn();
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.LIGHT, DateTime.now());
        recentCategories.put(InsightCard.Category.WAKE_VARIANCE, DateTime.now());

        spyInsightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_SATURDAY, mockFeatureFlipper);

        //TEST - Look for weekly Insight, do not try to generate b/c recent
        Mockito.verify(spyInsightProcessor).selectWeeklyInsightsToGenerate(recentCategories, FAKE_SATURDAY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);

        final Optional<InsightCard.Category> wakeCardCategory = spyInsightProcessor.generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);
        assertThat(wakeCardCategory.isPresent(), is(Boolean.FALSE));

        //look for high priority Insight - get nothing b/c feature wrong date

        //look for random old Insight - get nothing b/c wrong date
        Mockito.verify(spyInsightProcessor).selectRandomOldInsightsToGenerate(FAKE_ACCOUNT_ID, recentCategories, FAKE_SATURDAY, mockFeatureFlipper);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.LIGHT);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.TEMPERATURE);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.SLEEP_QUALITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.BED_LIGHT_DURATION);
    }

    @Test
    public void test_generateGeneralInsights_6() {

        final RolloutClient mockFeatureFlipper = featureFlipOn();
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.TEMPERATURE, DateTime.now());
        recentCategories.put(InsightCard.Category.LIGHT, DateTime.now());

        spyInsightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_DATE_1, mockFeatureFlipper);

        //TEST - Look for weekly Insight, do not try to generate b/c wrong date
        Mockito.verify(spyInsightProcessor).selectWeeklyInsightsToGenerate(recentCategories, FAKE_DATE_1);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);

        //look for high priority Insight - get nothing

        //look for random old Insight, try to generate humidity
        Mockito.verify(spyInsightProcessor).selectRandomOldInsightsToGenerate(FAKE_ACCOUNT_ID, recentCategories, FAKE_DATE_1, mockFeatureFlipper);
        Mockito.verify(spyInsightProcessor).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.HUMIDITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.TEMPERATURE);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.SLEEP_QUALITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.BED_LIGHT_DURATION);
    }

    @Test
    public void test_generateGeneralInsights_7() {

        final RolloutClient mockFeatureFlipper = featureFlipOn();
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.GOAL_WAKE_VARIANCE, DateTime.now());

        spyInsightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_SATURDAY, mockFeatureFlipper);

        //TEST - Correct date for weekly insight, but Goal inserted does nothing, so generate wake variance, get Optional.absent() b/c no data
        Mockito.verify(spyInsightProcessor).selectWeeklyInsightsToGenerate(recentCategories, FAKE_SATURDAY);
        Mockito.verify(spyInsightProcessor).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);

        //look for high priority Insight - get nothing

        //Look for random old insight, but get nothing because wrong date
        Mockito.verify(spyInsightProcessor).selectRandomOldInsightsToGenerate(FAKE_ACCOUNT_ID, recentCategories, FAKE_SATURDAY, mockFeatureFlipper);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.HUMIDITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.TEMPERATURE);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.SLEEP_QUALITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.BED_LIGHT_DURATION);
    }

    @Test
    public void test_generateGeneralInsights_8() {

        //Turn on feature flip for marketing schedule
        final RolloutClient mockFeatureFlipper = featureFlipMarketingScheduleOn();

        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.LIGHT, DateTime.now());

        spyInsightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_DATE_13, mockFeatureFlipper);

        //TEST - Incorrect date for weekly insight - get nothing
        Mockito.verify(spyInsightProcessor).selectWeeklyInsightsToGenerate(recentCategories, FAKE_DATE_13);

        //look for high priority Insight - get nothing

        //Look for random old insight - light is already generated, so we do nothing
        Mockito.verify(spyInsightProcessor).selectRandomOldInsightsToGenerate(FAKE_ACCOUNT_ID, recentCategories, FAKE_DATE_13, mockFeatureFlipper);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.LIGHT);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.HUMIDITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.TEMPERATURE);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.SLEEP_QUALITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.BED_LIGHT_DURATION);

        //Look for marketing insight - can't spy on private random, so do assert
        assertThat(insightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_DATE_13, mockFeatureFlipper).get(), isIn(marketingInsightPool));
    }

    @Test
    public void test_generateGeneralInsights_9() {

        final RolloutClient mockFeatureFlipper = featureFlipOn();
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        //actually simulating recent categories
        final Map<InsightCard.Category, DateTime> recentCategories = new HashMap<>();
        recentCategories.put(InsightCard.Category.TEMPERATURE, DateTime.now());

        spyInsightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_DATE_13, mockFeatureFlipper);

        //TEST - Look for weekly Insight, do not try to generate b/c wrong date
        Mockito.verify(spyInsightProcessor).selectWeeklyInsightsToGenerate(recentCategories, FAKE_DATE_13);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);

        //look for high priority Insight - get nothing

        //look for random old Insight, try to generate humidity
        Mockito.verify(spyInsightProcessor).selectRandomOldInsightsToGenerate(FAKE_ACCOUNT_ID, recentCategories, FAKE_DATE_13, mockFeatureFlipper);
        Mockito.verify(spyInsightProcessor).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.LIGHT);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.TEMPERATURE);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.SLEEP_QUALITY);
        Mockito.verify(spyInsightProcessor, Mockito.never()).generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.BED_LIGHT_DURATION);

        //Marketing Insight is not generated b/c already made humidity - can't spy on private random, so do assert
        assertThat(marketingInsightPool.contains(insightProcessor.generateGeneralInsights(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, recentCategories, FAKE_DATE_13, mockFeatureFlipper).get()), is(Boolean.FALSE));
    }


    @Test
    public void test_selectMarketingInsightToGenerate_0() {
        final InsightProcessor insightProcessor = setUp();
        final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet(InsightCard.Category.RUN);

        final Random random = new Random();
        final Optional<InsightCard.Category> marketingInsightToGenerate = insightProcessor.selectMarketingInsightToGenerate(FAKE_DATE_1, marketingInsightsSeen, random, FAKE_DATE_1);

        //TEST - correct date, but we already generated a marketing insight today
        assertThat(marketingInsightToGenerate.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void test_selectMarketingInsightToGenerate() {
        final InsightProcessor insightProcessor = setUp();
        final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet(InsightCard.Category.RUN);

        final Random random = new Random();
        final Optional<InsightCard.Category> marketingInsightToGenerate = insightProcessor.selectMarketingInsightToGenerate(FAKE_DATE_1, marketingInsightsSeen, random, FAKE_DATE_10);

        //TEST - correct date, and there are categories to pick from
        assertThat(marketingInsightToGenerate.isPresent(), is(Boolean.TRUE));
    }

    @Test
    public void test_selectMarketingInsightToGenerate_2() {
        final InsightProcessor insightProcessor = setUp();
        final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet(InsightCard.Category.RUN);

        final Random random = new Random();
        final Optional<InsightCard.Category> marketingInsightToGenerate = insightProcessor.selectMarketingInsightToGenerate(FAKE_DATE_NONE, marketingInsightsSeen, random, FAKE_DATE_10);

        //TEST - incorrect date
        assertThat(marketingInsightToGenerate.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void test_selectMarketingInsightToGenerate_3() {
        final InsightProcessor insightProcessor = setUp();
        final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet(InsightCard.Category.DRIVE,
                InsightCard.Category.EAT,
                InsightCard.Category.LEARN,
                InsightCard.Category.LOVE,
                InsightCard.Category.PLAY,
                InsightCard.Category.RUN,
                InsightCard.Category.SWIM,
                InsightCard.Category.WORK);

        final Random random = new Random();
        final Optional<InsightCard.Category> marketingInsightToGenerate = insightProcessor.selectMarketingInsightToGenerate(FAKE_DATE_1, marketingInsightsSeen, random, FAKE_DATE_10);

        //TEST - correct date, but there are no categories to pick from
        assertThat(marketingInsightToGenerate.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void test_selectMarketingInsightToGenerate_4() {
        final InsightProcessor insightProcessor = setUp();

        Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet();

        final Random random = new Random();

        for (int i = 0; i < marketingInsightPool.size(); i ++) {
            final Optional<InsightCard.Category> randomInsightCategory = insightProcessor.pickRandomInsightCategory(marketingInsightPool, marketingInsightsSeen, random);
            assertThat(randomInsightCategory.isPresent(), is(Boolean.TRUE));
            assertThat(marketingInsightsSeen.contains(randomInsightCategory), is(Boolean.FALSE));
            marketingInsightsSeen.add(randomInsightCategory.get());
        }
    }

    @Test
    public void test_selectMarketingInsight() {
        //All marketing insights are available, we pull a random one

        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        final Set<InsightCard.Category> marketingInsightsSeen= Sets.newHashSet(InsightCard.Category.LIGHT);

        final Random random = new Random();

        //TEST No marketing insights have been generated yet, so marketingInsightToGenerate can be any of the entire pool
        Optional<InsightCard.Category> marketingInsightToGenerate = spyInsightProcessor.selectMarketingInsightToGenerate(FAKE_DATE_1, marketingInsightsSeen, random, FAKE_DATE_10);
        assertThat(marketingInsightPool.contains(marketingInsightToGenerate.get()), is(Boolean.TRUE));
    }

    @Test
    public void test_selectMarketingInsight_2() {
        //All marketing insights are already seen, so we do not generate Marketing Insight

        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        final Set<InsightCard.Category> marketingInsightsSeen = Sets.newHashSet(InsightCard.Category.DRIVE,
                InsightCard.Category.EAT,
                InsightCard.Category.LEARN,
                InsightCard.Category.LOVE,
                InsightCard.Category.PLAY,
                InsightCard.Category.RUN,
                InsightCard.Category.SWIM,
                InsightCard.Category.WORK);

        final Random random = new Random();

        //TEST All marketing insights have been generated, so we do not have a marketingInsightToGenerate
        Optional<InsightCard.Category> marketingInsightToGenerate = spyInsightProcessor.selectMarketingInsightToGenerate(FAKE_DATE_1, marketingInsightsSeen, random, FAKE_DATE_10);
        assertThat(marketingInsightToGenerate.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void test_pickRandomInsightCategory() {
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        final Set<InsightCard.Category> marketingInsightsSeen = new HashSet<>();
        marketingInsightsSeen.add(InsightCard.Category.RUN);

        final Random random = new Random();
        final Optional<InsightCard.Category> randomInsightCategory = spyInsightProcessor.pickRandomInsightCategory(marketingInsightPool, marketingInsightsSeen, random);

        //TEST b/c run has already been generated, randomInsightCategory will never be run
        assertThat(randomInsightCategory.get() == InsightCard.Category.RUN, is(Boolean.FALSE));
    }

    @Test
    public void test_generateCategory_light() {

        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        final Optional<InsightCard.Category> generatedInsight = spyInsightProcessor.generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.LIGHT);

        assertThat(generatedInsight.isPresent(), is(Boolean.TRUE));
    }

    @Test
    public void test_generateCategory_humidity() {

        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        final Optional<InsightCard.Category> generatedInsight = spyInsightProcessor.generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.HUMIDITY);

        assertThat(generatedInsight.isPresent(), is(Boolean.TRUE));
    }


    @Test
    public void test_generateCategory_wakeVariance() {

        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);

        final Optional<InsightCard.Category> generatedInsight = spyInsightProcessor.generateInsightsByCategory(FAKE_ACCOUNT_ID, FAKE_DEVICE_ACCOUNT_PAIR, deviceDataDAODynamoDB, InsightCard.Category.WAKE_VARIANCE);

        //no real data for wake variance, will not generate Insight
        assertThat(generatedInsight.isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void test_recentCategories() {

        //Turn on feature flip for marketing schedule
        final InsightProcessor insightProcessor = setUp();
        final InsightProcessor spyInsightProcessor = Mockito.spy(insightProcessor);
        final InsightsLastSeen fakeInsightLastSeen1 = new InsightsLastSeen(FAKE_ACCOUNT_ID, InsightCard.Category.AIR_QUALITY, DateTime.now().minusDays(14));
        final InsightsLastSeen fakeInsightLastSeen2 = new InsightsLastSeen(FAKE_ACCOUNT_ID, InsightCard.Category.ALCOHOL, DateTime.now().minusDays(1));
        final List<InsightsLastSeen> fakeInsightsLastSeen = Lists.newArrayList();;
        fakeInsightsLastSeen.add(fakeInsightLastSeen1);
        fakeInsightsLastSeen.add(fakeInsightLastSeen2);

        Map<InsightCard.Category, DateTime> recentCategories = spyInsightProcessor.getLastSeenInsights(FAKE_ACCOUNT_ID, fakeInsightsLastSeen);

        //TEST - Incorrect date for weekly insight - get nothing

        //Look for marketing insight - can't spy on private random, so do assert
        assertThat(recentCategories.containsKey(InsightCard.Category.AIR_QUALITY), is(true));
        assertThat(recentCategories.containsKey(InsightCard.Category.ALCOHOL), is(true));
        assertThat(recentCategories.containsKey(InsightCard.Category.BED_LIGHT_DURATION), is(false));

        final Boolean checkBedLight = spyInsightProcessor.checkQualifiedInsight(recentCategories, InsightCard.Category.BED_LIGHT_DURATION, 13);
        final Boolean checkAlcohol = spyInsightProcessor.checkQualifiedInsight(recentCategories, InsightCard.Category.ALCOHOL, 13);
        final Boolean checkAirQuality = spyInsightProcessor.checkQualifiedInsight(recentCategories, InsightCard.Category.AIR_QUALITY, 13);
        final int numRecentInsights = spyInsightProcessor.getNumRecentInsights(recentCategories, 13);

        assertThat(checkBedLight, is(true));
        assertThat(checkAirQuality, is(true));
        assertThat(checkAlcohol, is(false));
        assertThat(numRecentInsights, is(1));
    }

}

