package com.hello.suripu.core.models;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.hello.suripu.core.models.Insights.SumCountData;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by jyfan on 7/11/16.
 */
public class AggStatsTest {
    final long FAKE_ACCOUNT_ID = 9999L;
    final String FAKE_EXTERNAL_DEVICE_ID = "fakeit";

    @Test
    public void test_builder() {
        final DateTime dateLocal = DateTime.parse("2016-01-01");

        final Optional<Integer> device_data_length = Optional.of(100);
        final Optional<Integer> tracker_motion_length = Optional.of(50);

        final Optional<Integer> avg_temp = Optional.of(1002);
        final Optional<Integer> max_temp = Optional.of(1001);
        final Optional<Integer> min_temp = Optional.of(1000);
        final Optional<Integer> avg_humid = Optional.of(1005);
        final Optional<Integer> avg_dust = Optional.of(1006);

        final Map<Integer, SumCountData> sumCountMicroLuxHourMap = Maps.newHashMap();
        sumCountMicroLuxHourMap.put(22, new SumCountData(0, 0));
        sumCountMicroLuxHourMap.put(23, new SumCountData(0, 0));
        sumCountMicroLuxHourMap.put(0, new SumCountData(0, 0));

        final AggStats.Builder builder = new AggStats.Builder()
                .withAccountId(FAKE_ACCOUNT_ID)
                .withDateLocal(dateLocal)
                .withExternalDeviceId(FAKE_EXTERNAL_DEVICE_ID)

                .withDeviceDataCount(device_data_length)
                .withTrackerMotionCount(tracker_motion_length)

                .withAvgDailyTemp(avg_temp)
                .withMaxDailyTemp(max_temp)
                .withMinDailyTemp(min_temp)
                .withAvgDailyHumidity(avg_humid)
                .withAvgDailyDustDensity(avg_dust)

                .withSumCountMicroLuxHourMap(sumCountMicroLuxHourMap);

        final AggStats aggStats = builder.build();

        assertThat(aggStats.accountId, is(FAKE_ACCOUNT_ID));
        assertThat(aggStats.dateLocal, is(dateLocal));
        assertThat(aggStats.externalDeviceId, is(FAKE_EXTERNAL_DEVICE_ID));

        assertThat(aggStats.deviceDataCount, is(device_data_length));
        assertThat(aggStats.trackerMotionCount, is(tracker_motion_length));

        assertThat(aggStats.avgDailyTemp, is(avg_temp));
        assertThat(aggStats.maxDailyTemp, is(max_temp));
        assertThat(aggStats.minDailyTemp, is(min_temp));

        assertThat(aggStats.avgDailyHumidity, is(avg_humid));

        assertThat(aggStats.avgDailyDustDensity, is(avg_dust));
    }
}
