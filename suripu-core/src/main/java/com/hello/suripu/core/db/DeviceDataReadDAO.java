package com.hello.suripu.core.db;

import com.google.common.collect.ImmutableList;
import com.hello.suripu.core.models.DeviceData;
import com.hello.suripu.core.models.DeviceId;
import org.joda.time.DateTime;

/**
 * Created by jakepiccolo on 11/9/15.
 */
public interface DeviceDataReadDAO {

    ImmutableList<DeviceData> getBetweenByLocalHourAggregateBySlotDuration(
            final Long accountId,
            final DeviceId deviceId,
            final DateTime start,
            final DateTime end,
            final DateTime startLocal,
            final DateTime endLocal,
            final int startHour,
            final int endHour,
            final Integer slotDuration);

    ImmutableList<DeviceData> getLightByBetweenHourDateByTS(
            final Long accountId,
            final DeviceId deviceId,
            final int lightLevel,
            final DateTime startTimestamp,
            final DateTime endTimestamp,
            final DateTime startLocalTimestamp,
            final DateTime endLocalTimestamp,
            final int startHour,
            final int endHour);

    ImmutableList<DeviceData> getBetweenHourDateByTSSameDay(
            final Long accountId,
            final DeviceId deviceId,
            final DateTime startTimestamp,
            final DateTime endTimestamp,
            final DateTime startLocalTimestamp,
            final DateTime endLocalTimestamp,
            final int startHour,
            final int endHour);

    ImmutableList<DeviceData> getBetweenHourDateByTS(
            final Long accountId,
            final DeviceId deviceId,
            final DateTime startTimestamp,
            final DateTime endTimestamp,
            final DateTime startLocalTimestamp,
            final DateTime endLocalTimestamp,
            final int startHour,
            final int endHour);

    ImmutableList<Integer> getAirQualityRawList(
            final Long accountId,
            final DeviceId deviceId,
            final DateTime startTimestamp,
            final DateTime endTimestamp,
            final DateTime startLocalTimeStamp,
            final DateTime endLocalTimeStamp);
}
