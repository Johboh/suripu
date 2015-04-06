package com.hello.suripu.research.resources.v1;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hello.suripu.core.db.AccountDAO;
import com.hello.suripu.core.db.DeviceDAO;
import com.hello.suripu.core.db.DeviceDataDAO;
import com.hello.suripu.core.db.FeedbackDAO;
import com.hello.suripu.core.db.SleepLabelDAO;
import com.hello.suripu.core.db.TrackerMotionDAO;
import com.hello.suripu.core.models.Account;
import com.hello.suripu.core.models.AllSensorSampleList;
import com.hello.suripu.core.models.DataScience.JoinedSensorsMinuteData;
import com.hello.suripu.core.models.DataScience.UserLabel;
import com.hello.suripu.core.models.DeviceAccountPair;
import com.hello.suripu.core.models.Event;
import com.hello.suripu.core.models.Sample;
import com.hello.suripu.core.models.Sensor;
import com.hello.suripu.core.models.TimelineFeedback;
import com.hello.suripu.core.models.TrackerMotion;
import com.hello.suripu.core.oauth.AccessToken;
import com.hello.suripu.core.oauth.OAuthScope;
import com.hello.suripu.core.oauth.Scope;
import com.hello.suripu.core.resources.BaseResource;
import com.hello.suripu.core.util.DateTimeUtil;
import com.hello.suripu.core.util.JsonError;
import com.hello.suripu.core.util.NamedSleepHmmModel;
import com.hello.suripu.core.util.PartnerDataUtils;
import com.hello.suripu.core.util.SleepHmmSensorDataBinning;
import com.hello.suripu.core.util.TimelineUtils;
import com.hello.suripu.research.models.BinnedSensorData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pangwu on 12/1/14.
 */
@Path("/v1/datascience")
public class DataScienceResource extends BaseResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataScienceResource.class);
    private final AccountDAO accountDAO;
    private final TrackerMotionDAO trackerMotionDAO;
    private final DeviceDataDAO deviceDataDAO;
    private final DeviceDAO deviceDAO;
    private final SleepLabelDAO sleepLabelDAO;
    private final FeedbackDAO feedbackDAO;

    public DataScienceResource(final AccountDAO accountDAO,
                               final TrackerMotionDAO trackerMotionDAO,
                               final DeviceDataDAO deviceDataDAO,
                               final DeviceDAO deviceDAO,
                               final SleepLabelDAO sleepLabelDAO,
                               final FeedbackDAO feedbackDAO) {
        this.accountDAO = accountDAO;
        this.trackerMotionDAO = trackerMotionDAO;
        this.deviceDataDAO = deviceDataDAO;
        this.deviceDAO = deviceDAO;
        this.sleepLabelDAO = sleepLabelDAO;
        this.feedbackDAO = feedbackDAO;
    }

    @GET
    @Path("/pill/{email}/{query_date_local_utc}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrackerMotion> getMotion(@Scope({OAuthScope.SENSORS_BASIC, OAuthScope.RESEARCH}) final AccessToken accessToken,
                             @PathParam("query_date_local_utc") final String date,
                             @PathParam("email") final String email) {
        final DateTime targetDate = DateTime.parse(date, DateTimeFormat.forPattern(DateTimeUtil.DYNAMO_DB_DATE_FORMAT))
                .withZone(DateTimeZone.UTC).withHourOfDay(20);
        final DateTime endDate = targetDate.plusHours(16);
        LOGGER.debug("Target date: {}", targetDate);
        LOGGER.debug("End date: {}", endDate);

        final Optional<Long> accountId = getAccountIdByEmail(email);
        if (!accountId.isPresent()) {
            LOGGER.debug("ID not found for account {}", email);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final List<TrackerMotion> trackerMotions = trackerMotionDAO.getBetweenLocalUTC(accountId.get(), targetDate, endDate);
        LOGGER.debug("Length of trackerMotion: {}", trackerMotions.size());

        return trackerMotions;
    }

    @GET
    @Path("/pill/partner/{email}/{query_date_local_utc}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrackerMotion> getMotionWithPartnerMotionFiltered(@Scope({OAuthScope.SENSORS_BASIC, OAuthScope.RESEARCH}) final AccessToken accessToken,
                                         @PathParam("query_date_local_utc") final String date,
                                         @PathParam("email") final String email) {
        final DateTime targetDate = DateTime.parse(date, DateTimeFormat.forPattern(DateTimeUtil.DYNAMO_DB_DATE_FORMAT))
                .withZone(DateTimeZone.UTC).withHourOfDay(20);
        final DateTime endDate = targetDate.plusHours(16);
        LOGGER.debug("Target date: {}", targetDate);
        LOGGER.debug("End date: {}", endDate);

        final Optional<Long> accountId = getAccountIdByEmail(email);
        if (!accountId.isPresent()) {
            LOGGER.debug("ID not found for account {}", email);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final List<TrackerMotion> originalTrackerMotions = trackerMotionDAO.getBetweenLocalUTC(accountId.get(), targetDate, endDate);
        final Optional<Long> optionalPartnerAccountId = this.deviceDAO.getPartnerAccountId(accountId.get());

        final List<TrackerMotion> myMotions = new ArrayList<>();

        if (optionalPartnerAccountId.isPresent()) {
            final Long partnerAccountId = optionalPartnerAccountId.get();
            LOGGER.debug("partner account {}", partnerAccountId);
            final List<TrackerMotion> partnerMotions = this.trackerMotionDAO.getBetweenLocalUTC(partnerAccountId, targetDate, endDate);
            if(partnerMotions.isEmpty()){
                myMotions.addAll(originalTrackerMotions);
            }else{
                myMotions.addAll(PartnerDataUtils.getMyMotion(originalTrackerMotions, partnerMotions).myMotions);
            }
        }

        return myMotions;
    }

    @GET
    @Path("/light/{email}/{query_date_local_utc}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getLightOut(@Scope({OAuthScope.SENSORS_BASIC, OAuthScope.RESEARCH}) final AccessToken accessToken,
                                            @PathParam("query_date_local_utc") final String date,
                                            @PathParam("email") final String email) {
        final DateTime targetDate = DateTime.parse(date, DateTimeFormat.forPattern(DateTimeUtil.DYNAMO_DB_DATE_FORMAT))
                .withZone(DateTimeZone.UTC).withHourOfDay(20);
        final DateTime endDate = targetDate.plusHours(16);
        LOGGER.debug("Target date: {}", targetDate);
        LOGGER.debug("End date: {}", endDate);

        final Optional<Long> accountId = getAccountIdByEmail(email);
        if (!accountId.isPresent()) {
            LOGGER.debug("ID not found for account {}", email);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final Optional<Long> internalSenseIdOptional = this.deviceDAO.getMostRecentSenseByAccountId(accountId.get());

        if(!internalSenseIdOptional.isPresent()){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (internalSenseIdOptional.isPresent()) {
            final int slotDurationMins = 1;

            final AllSensorSampleList sensorData = deviceDataDAO.generateTimeSeriesByLocalTimeAllSensors(
                    targetDate.getMillis(), endDate.getMillis(),
                    accountId.get(), internalSenseIdOptional.get(),
                    slotDurationMins,
                    missingDataDefaultValue(accountId.get()));
            final List<Sample> lightData = sensorData.get(Sensor.LIGHT);
            final List<Event> lightEvents = TimelineUtils.getLightEventsWithMultipleLightOut(lightData);
            return lightEvents;
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }


    @GET
    @Path("/sensors/{email}/{query_date_local_utc}/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sample> getSensors(@Scope({OAuthScope.SENSORS_BASIC, OAuthScope.RESEARCH}) final AccessToken accessToken,
                                   @PathParam("query_date_local_utc") final String date,
                                   @PathParam("type") final String dataType,
                                   @PathParam("email") final String email) {
        final DateTime targetDate = DateTime.parse(date, DateTimeFormat.forPattern(DateTimeUtil.DYNAMO_DB_DATE_FORMAT))
                .withZone(DateTimeZone.UTC).withHourOfDay(20);
        final DateTime endDate = targetDate.plusHours(16);
        LOGGER.debug("Target date: {}", targetDate);
        LOGGER.debug("End date: {}", endDate);

        final Optional<Long> accountId = getAccountIdByEmail(email);
        if (!accountId.isPresent()) {
            LOGGER.debug("ID not found for account {}", email);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        final Optional<Long> deviceId = this.deviceDAO.getMostRecentSenseByAccountId(accountId.get());
        if (deviceId.isPresent()) {
            final int slotDurationMins = 1;

            AllSensorSampleList sensorData = this.deviceDataDAO.generateTimeSeriesByLocalTimeAllSensors(
                    targetDate.getMillis(), endDate.getMillis(),
                    accountId.get(), deviceId.get(),
                    slotDurationMins,
                    missingDataDefaultValue(accountId.get()));
            final List<Sample> data = sensorData.get(Sensor.valueOf(dataType));
            return data;
        }
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    private Optional<Long> getAccountIdByEmail(final String email) {
        final Optional<Account> accountOptional = accountDAO.getByEmail(email);

        if (!accountOptional.isPresent()) {
            LOGGER.debug("Account {} not found", email);
            return Optional.absent();
        }

        final Account account = accountOptional.get();
        if (!account.id.isPresent()) {
            LOGGER.debug("ID not found for account {}", email);
            return Optional.absent();
        }
        return account.id;
    }

    @GET
    @Path("/feedback/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<TimelineFeedback> getFeedbackFromUser(@Scope(OAuthScope.RESEARCH) final AccessToken accessToken,
                                     @PathParam("email") final String email) {
        final Optional<Long> accountId = getAccountIdByEmail(email);
        if (!accountId.isPresent()) {
            LOGGER.debug("ID not found for account {}", email);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final List<TimelineFeedback> feedback = this.feedbackDAO.getForAccount(accountId.get());
        return feedback;
    }




    @GET
    @Path("/label/{email}/{night}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserLabel> getLabels(@Scope(OAuthScope.RESEARCH) final AccessToken accessToken,
                                     @PathParam("email") String email,
                                     @PathParam("night") String night) {
        final DateTime nightDate = DateTime.parse(night, DateTimeFormat.forPattern(DateTimeUtil.DYNAMO_DB_DATE_FORMAT))
                .withZone(DateTimeZone.UTC).withTimeAtStartOfDay();
        LOGGER.debug("{} {}", email, nightDate);
        return sleepLabelDAO.getUserLabelsByEmailAndNight(email, nightDate);
    }

    // APIs for Benjo's analysis
    @GET
    @Path("/device_sensors_motion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<JoinedSensorsMinuteData> getJoinedSensorDataByEmail(@Scope({OAuthScope.RESEARCH}) final AccessToken accessToken,
                                                                @QueryParam("email") String email,
                                                                @QueryParam("account_id") Long accountId,
                                                                @QueryParam("from_ts") Long fromTimestamp,
                                                                @DefaultValue("3") @QueryParam("num_days") Integer numDays) {

        if ( (email == null && accountId == null) || fromTimestamp == null) {
            throw new WebApplicationException(Response.status(400).entity(new JsonError(400,
                    "Missing query parameters, use email or account_id, and from_ts")).build());
        }

        Optional<Account> optionalAccount;
        if (email != null) {
            optionalAccount = accountDAO.getByEmail(email);
        } else {
            optionalAccount = accountDAO.getById(accountId);
        }

        if (!optionalAccount.isPresent() || !optionalAccount.get().id.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final Account account = optionalAccount.get();

        return getJoinedSensorData(account.id.get(), fromTimestamp,numDays);
    }

    private List<JoinedSensorsMinuteData> getJoinedSensorData(final Long accountId, final Long ts, final int numDays) {

        final Optional<DeviceAccountPair> deviceAccountPairOptional = deviceDAO.getMostRecentSensePairByAccountId(accountId);
        if (!deviceAccountPairOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("This account does not have a sense recently").build());
        }

        final DateTime startTs = new DateTime(ts, DateTimeZone.UTC);
        final DateTime endTs = startTs.plusDays(numDays); // return 3 days of data max.


        LOGGER.debug("Getting joined sensor minute data for account id {} between {} and {}", accountId, startTs,endTs);

        final ImmutableList<TrackerMotion> motionData = trackerMotionDAO.getBetween(
                accountId,
                startTs,
                endTs
        );

        final Map<Long, TrackerMotion> motionSamples = new HashMap<>();
        for (final TrackerMotion motion: motionData) {
            motionSamples.put(motion.timestamp, motion);
        }

        final int slotDurationInMinutes = 1;
        final Integer missingDataDefaultValue = 0;
        final AllSensorSampleList sensorSamples = deviceDataDAO.generateTimeSeriesByUTCTimeAllSensors(
                startTs.getMillis(),
                endTs.getMillis(),
                accountId,
                deviceAccountPairOptional.get().internalDeviceId,
                slotDurationInMinutes,
                missingDataDefaultValue
        );

        final List<Sample> lightSamples = sensorSamples.get(Sensor.LIGHT);
        final List<Sample> waveCount = sensorSamples.get(Sensor.WAVE_COUNT);

        final int numSamples = lightSamples.size();

        final List<JoinedSensorsMinuteData> joinedSensorsMinuteData = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            final Long timestamp = lightSamples.get(i).dateTime;

            joinedSensorsMinuteData.add(new JoinedSensorsMinuteData(timestamp, accountId,
                    lightSamples.get(i).value,
                    sensorSamples.get(Sensor.SOUND_NUM_DISTURBANCES).get(i).value,
                    sensorSamples.get(Sensor.SOUND_PEAK_DISTURBANCE).get(i).value,
                    motionSamples.containsKey(timestamp) ? motionSamples.get(timestamp).value : null,
                    motionSamples.containsKey(timestamp) ? motionSamples.get(timestamp).kickOffCounts : null,
                    motionSamples.containsKey(timestamp) ? motionSamples.get(timestamp).motionRange : null,
                    motionSamples.containsKey(timestamp) ? motionSamples.get(timestamp).onDurationInSeconds : null,
                    (int)waveCount.get(i).value,
                    lightSamples.get(i).offsetMillis));
        }

        return joinedSensorsMinuteData;
    }


    @GET
    @Path("/binneddata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BinnedSensorData getBinnedSensorDataByEmailOrAccountID(@Scope({OAuthScope.RESEARCH}) final AccessToken accessToken,
                                                                  @QueryParam("email") String email,
                                                                  @QueryParam("account_id") Long accountId,
                                                                  @QueryParam("from_ts") Long fromTimestamp,
                                                                  @DefaultValue("3") @QueryParam("num_days") Integer numDays,
                                                                  @QueryParam("pill_threshold_counts") Double pillThreshold,
                                                                  @QueryParam("sound_threshold_db") Double soundThreshold,
                                                                  @QueryParam("nat_light_start_hour") Double naturalLightStartHour,
                                                                  @QueryParam("nat_light_stop_hour") Double naturalLightStopHour,
                                                                  @QueryParam("meas_period") Integer numMinutesInMeasPeriod
    ) {


       /* String modelName,
        ImmutableSet<Integer> sleepStates,
        ImmutableSet<Integer> onBedStates,
        ImmutableSet<Integer> allowableEndingStates,
        ImmutableList<Integer> sleepDepthsByState,
        double soundDisturbanceThresholdDB,
        double pillMagnitudeDisturbanceThresholdLsb,
        double naturalLightFilterStartHour,
        double naturalLightFilterStopHour,
        int numMinutesInMeasPeriod,
        boolean isUsingIntervalSearch) */

        NamedSleepHmmModel model = new NamedSleepHmmModel(null,"dummy", ImmutableSet.copyOf(Collections.EMPTY_SET),ImmutableSet.copyOf(Collections.EMPTY_SET),ImmutableSet.copyOf(Collections.EMPTY_SET),ImmutableList.copyOf(Collections.EMPTY_LIST),
                soundThreshold,pillThreshold,naturalLightStartHour,naturalLightStopHour,numMinutesInMeasPeriod,false);

        if ( (email == null && accountId == null) || fromTimestamp == null) {
            throw new WebApplicationException(Response.status(400).entity(new JsonError(400,
                    "Missing query parameters, use email or account_id, and from_ts")).build());
        }

        if (numDays == null || pillThreshold == null || soundThreshold == null || naturalLightStartHour == null || naturalLightStopHour == null || numMinutesInMeasPeriod == null) {
            throw new WebApplicationException(Response.status(400).entity(new JsonError(400,
                    "Missing query parameters")).build());
        }

        // GET ACCOUNT ID VIA EMAIL OR DIRECTLY
        Optional<Account> optionalAccount;
        if (email != null) {
            optionalAccount = accountDAO.getByEmail(email);
        } else {
            optionalAccount = accountDAO.getById(accountId);
        }

        if (!optionalAccount.isPresent() || !optionalAccount.get().id.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final Account account = optionalAccount.get();

        final Optional<DeviceAccountPair> deviceAccountPairOptional = deviceDAO.getMostRecentSensePairByAccountId(accountId);
        if (!deviceAccountPairOptional.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new JsonError(500,"This account does not have a sense recently")).build());
        }

        final DateTime date = new DateTime(fromTimestamp);

        final DateTime startTs = date.withTimeAtStartOfDay().withHourOfDay(DateTimeUtil.DAY_STARTS_AT_HOUR);
        final DateTime endTs = date.withTimeAtStartOfDay().plusDays(numDays).withHourOfDay(DateTimeUtil.DAY_ENDS_AT_HOUR);


        LOGGER.debug("Getting binned sensor minute data for account id {} between {} and {}", accountId, startTs,endTs);



        final ImmutableList<TrackerMotion> motionData = trackerMotionDAO.getBetweenLocalUTC(
                accountId,
                startTs,
                endTs
        );


        if (motionData.isEmpty()) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new JsonError(500,"No data on this day")).build());
        }

        List<TrackerMotion> filteredTrackerData = SleepHmmSensorDataBinning.removeDuplicatesAndInvalidValues(motionData.asList());

        final int slotDurationInMinutes = 1;
        final Integer missingDataDefaultValue = 0;
        final AllSensorSampleList sensorSamples = deviceDataDAO.generateTimeSeriesByUTCTimeAllSensors(
                startTs.getMillis(),
                endTs.getMillis(),
                accountId,
                deviceAccountPairOptional.get().internalDeviceId,
                slotDurationInMinutes,
                missingDataDefaultValue
        );

        final int timezoneOffset = filteredTrackerData.get(0).offsetMillis;
        Optional<SleepHmmSensorDataBinning.BinnedData> optionalBinnedData = Optional.absent();
        try {
            optionalBinnedData = SleepHmmSensorDataBinning.getBinnedSensorData(sensorSamples, filteredTrackerData, model, startTs.getMillis(), endTs.getMillis(), timezoneOffset);

        }
        catch (Exception e) {
            LOGGER.debug(e.toString());

            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new JsonError(500, "something failed when getting the binned sensor data.  go check the logs")).build());
        }

        if (!optionalBinnedData.isPresent()) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new JsonError(500,"failed to get binned data")).build());
        }

        final SleepHmmSensorDataBinning.BinnedData result = optionalBinnedData.get();
        List<List<Double>> matrix = new ArrayList<>();
        for (int j = 0; j < result.data.length; j++) {
            final double [] row = result.data[j];
            final List<Double> rowList = new ArrayList<>();
            for (int i = 0; i < result.data[0].length; i++) {
                rowList.add(row[i]);
            }

            matrix.add(rowList);
        }

        List<Long> times = new ArrayList<>();
        for (int i = 0; i < result.data[0].length; i++) {
            times.add(startTs.getMillis() + i*numMinutesInMeasPeriod*60000L);
        }

        return new BinnedSensorData(accountId,matrix,times,timezoneOffset);




    }

}
