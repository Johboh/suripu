package com.hello.suripu.workers.timeline;

import com.google.common.base.Optional;
import com.hello.suripu.api.ble.SenseCommandProtos;
import com.hello.suripu.core.db.DeviceDAO;
import com.hello.suripu.core.db.MergedUserInfoDynamoDB;
import com.hello.suripu.core.models.DeviceAccountPair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pangwu on 1/29/15.
 */
public class BatchProcessUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(BatchProcessUtils.class);

    public static Map<String, Set<DateTime>> groupRequestingPillIds(final List<SenseCommandProtos.batched_pill_data> batchedPillData){
        final HashMap<String, Set<DateTime>> pillIdTargetDatesMap = new HashMap<>();

        for (final SenseCommandProtos.batched_pill_data data : batchedPillData) {
            for(final SenseCommandProtos.pill_data pillData:data.getPillsList()) {
                if (pillData.hasUptime()) {  // Only triggered by heartbeat
                    if (!pillIdTargetDatesMap.containsKey(pillData.getDeviceId())) {
                        pillIdTargetDatesMap.put(pillData.getDeviceId(), new HashSet<DateTime>());
                    }

                    final DateTime targetDateUTC = new DateTime(pillData.getTimestamp() * 1000L, DateTimeZone.UTC);
                    pillIdTargetDatesMap.get(pillData.getDeviceId()).add(targetDateUTC);
                }
            }

        }

        return pillIdTargetDatesMap;
    }

    public static Map<Long, DateTime> groupAccountAndProcessDateLocalUTC(final Map<String, Set<DateTime>> groupedPillIdRequestDateUTC,
                                                                         final DateTime currentTimeUTC,
                                                                         final Integer earliestProcessTime,
                                                                         final Integer lastProcessTime,
                                                                          final DeviceDAO deviceDAO,
                                                                          final MergedUserInfoDynamoDB mergedUserInfoDynamoDB){
        final Map<Long, DateTime> targetDatesLocalUTC = new HashMap<>();
        for(final String pillId:groupedPillIdRequestDateUTC.keySet()) {

            final List<DeviceAccountPair> accountsLinkedWithPill = deviceDAO.getLinkedAccountFromPillId(pillId);
            if (accountsLinkedWithPill.size() == 0) {
                LOGGER.warn("No account linked with pill {}", pillId);
                continue;
            }

            if (accountsLinkedWithPill.size() > 1) {
                LOGGER.warn("{} accounts linked with pill {}, only account {} get the timeline",
                        accountsLinkedWithPill.size(),
                        pillId,
                        accountsLinkedWithPill.get(accountsLinkedWithPill.size() - 1).accountId);
            }

            final long accountId = accountsLinkedWithPill.get(accountsLinkedWithPill.size() - 1).accountId;
            final List<DeviceAccountPair> sensesLinkedWithAccount = deviceDAO.getSensesForAccountId(accountId);
            if (sensesLinkedWithAccount.size() == 0) {
                LOGGER.warn("No sense linked with account {} from pill {}", accountId, pillId);
                continue;
            }

            if (sensesLinkedWithAccount.size() > 1) {
                LOGGER.warn("{} senses linked with account {}, only sense {} got the timeline.",
                        sensesLinkedWithAccount.size(),
                        accountId,
                        sensesLinkedWithAccount.get(sensesLinkedWithAccount.size() - 1).externalDeviceId);
            }

            final String senseId = sensesLinkedWithAccount.get(sensesLinkedWithAccount.size() - 1).externalDeviceId;
            final Optional<DateTimeZone> dateTimeZoneOptional = mergedUserInfoDynamoDB.getTimezone(senseId, accountId);

            if (!dateTimeZoneOptional.isPresent()) {
                LOGGER.error("No timezone for sense {} account {}", senseId, accountId);
                continue;
            }

            final DateTime nowLocalTime = currentTimeUTC.withZone(dateTimeZoneOptional.get());
            if(nowLocalTime.getHourOfDay() < earliestProcessTime || nowLocalTime.getHourOfDay() > lastProcessTime){
                continue;
            }

            final DateTime targetDateLocalUTC = nowLocalTime
                    .withZone(DateTimeZone.UTC)
                    .plusMillis(dateTimeZoneOptional.get().getOffset(nowLocalTime.getMillis()))
                    .withTimeAtStartOfDay()
                    .minusDays(1);

            targetDatesLocalUTC.put(accountId, targetDateLocalUTC);

        }

        return targetDatesLocalUTC;
    }


}