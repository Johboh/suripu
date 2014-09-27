package com.hello.suripu.core.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.hello.suripu.core.models.Alarm;
import com.hello.suripu.core.models.AlarmInfo;
import com.hello.suripu.core.models.RingTime;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by pangwu on 9/25/14.
 */
public class MergedAlarmInfoDynamoDB {

    private final static Logger LOGGER = LoggerFactory.getLogger(MergedAlarmInfoDynamoDB.class);
    private final AmazonDynamoDBClient dynamoDBClient;
    private final String tableName;

    public static final String MORPHEUS_ID_ATTRIBUTE_NAME = "device_id";
    public static final String ACCOUNT_ID_ATTRIBUTE_NAME = "account_id";

    // Alarm template
    public static final String ALARM_TEMPLATES_ATTRIBUTE_NAME = "alarm_templates";

    // Ring time
    public static final String EXPECTED_RING_TIME_ATTRIBUTE_NAME = "expected_ring_at_utc";
    public static final String ACTUAL_RING_TIME_ATTRIBUTE_NAME = "actual_ring_at_utc";
    public static final String SOUND_IDS_ATTRIBUTE_NAME = "sound_ids";

    // Timezone history
    public static final String TIMEZONE_ID_ATTRIBUTE_NAME = "timezone_id";

    public static final String UPDATED_AT_ATTRIBUTE_NAME = "updated_at";


    private static int MAX_CALL_COUNT = 3;
    public static final int MAX_ALARM_COUNT = 7;


    public void setInfo(final AlarmInfo info){
        this.setInfo(info, DateTime.now());
    }

    public boolean setInfo(final AlarmInfo info, final DateTime updateTime){

        final HashMap<String, AttributeValue> keys = new HashMap<>();
        keys.put(MORPHEUS_ID_ATTRIBUTE_NAME, new AttributeValue().withS(info.deviceId));
        keys.put(ACCOUNT_ID_ATTRIBUTE_NAME, new AttributeValue().withN(String.valueOf(info.accountId)));

        final HashMap<String, AttributeValueUpdate> items = new HashMap<String, AttributeValueUpdate>();


        final ObjectMapper mapper = new ObjectMapper();
        try {
            boolean hasUpdate = false;
            if(info.alarmList.isPresent()){
                final String alarmListJSON = mapper.writeValueAsString(info.alarmList.get());
                items.put(ALARM_TEMPLATES_ATTRIBUTE_NAME, new AttributeValueUpdate()
                                .withAction(AttributeAction.PUT)
                                .withValue(new AttributeValue().withS(alarmListJSON)));
                hasUpdate = true;
            }

            if(info.ringTime.isPresent()){

                items.put(EXPECTED_RING_TIME_ATTRIBUTE_NAME, new AttributeValueUpdate()
                    .withAction(AttributeAction.PUT)
                    .withValue(new AttributeValue().withN(String.valueOf(info.ringTime.get().expectedRingTimeUTC))));
                items.put(ACTUAL_RING_TIME_ATTRIBUTE_NAME, new AttributeValueUpdate()
                    .withAction(AttributeAction.PUT)
                    .withValue(new AttributeValue().withN(String.valueOf(info.ringTime.get().actualRingTimeUTC))));
                items.put(SOUND_IDS_ATTRIBUTE_NAME, new AttributeValueUpdate()
                    .withAction(AttributeAction.PUT)
                    .withValue(new AttributeValue().withS(mapper.writeValueAsString(info.ringTime.get().soundIds))));
                hasUpdate = true;
            }

            if(info.timeZone.isPresent()){
                items.put(TIMEZONE_ID_ATTRIBUTE_NAME, new AttributeValueUpdate()
                    .withAction(AttributeAction.PUT)
                    .withValue(new AttributeValue().withS(info.timeZone.get().getID())));
                hasUpdate = true;
            }

            if(!hasUpdate){
                LOGGER.warn("Nothing to update for device {}, account {} return.", info.deviceId, info.accountId);
                return false;
            }

            items.put(UPDATED_AT_ATTRIBUTE_NAME, new AttributeValueUpdate()
                .withAction(AttributeAction.PUT)
                .withValue(new AttributeValue().withN(String.valueOf(updateTime.getMillis()))));

            final UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                    .withTableName(this.tableName)
                    .withKey(keys)
                    .withAttributeUpdates(items)
                    .withReturnValues(ReturnValue.ALL_NEW);

            final UpdateItemResult result = this.dynamoDBClient.updateItem(updateItemRequest);
            return true;

        } catch (JsonProcessingException ex) {
            LOGGER.error("Set info for device {}, account {} failed, error: {}", info.deviceId, info.accountId, ex.getMessage());
        }

        return false;
    }

    public Optional<AlarmInfo> getInfo(final String deviceId, final long accountId){
        final List<AlarmInfo> alarmInfos = getInfo(deviceId);
        for(final AlarmInfo alarmInfo:alarmInfos){
            if(alarmInfo.accountId == accountId){
                return Optional.of(alarmInfo);
            }
        }

        return Optional.absent();
    }

    public List<AlarmInfo> getInfo(final String deviceId){
        final Map<String, Condition> queryConditions = new HashMap<String, Condition>();
        final Condition selectByDeviceId  = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(deviceId));
        queryConditions.put(MORPHEUS_ID_ATTRIBUTE_NAME, selectByDeviceId);

        final HashSet<String> targetAttributes = new HashSet<String>();
        Collections.addAll(targetAttributes,
                MORPHEUS_ID_ATTRIBUTE_NAME, ACCOUNT_ID_ATTRIBUTE_NAME,
                ALARM_TEMPLATES_ATTRIBUTE_NAME,
                EXPECTED_RING_TIME_ATTRIBUTE_NAME, ACTUAL_RING_TIME_ATTRIBUTE_NAME, SOUND_IDS_ATTRIBUTE_NAME,
                TIMEZONE_ID_ATTRIBUTE_NAME,
                UPDATED_AT_ATTRIBUTE_NAME);

        final QueryRequest queryRequest = new QueryRequest(this.tableName)
                .withKeyConditions(queryConditions)
                .withAttributesToGet(targetAttributes);

        final QueryResult queryResult = this.dynamoDBClient.query(queryRequest);
        if(queryResult.getItems() == null){
            return Collections.EMPTY_LIST;
        }

        if(queryResult.getItems().size() == 0){
            return Collections.EMPTY_LIST;
        }

        final List<Map<String, AttributeValue>> items = queryResult.getItems();
        final List<AlarmInfo> alarmInfos = new ArrayList<AlarmInfo>();

        for (final Map<String, AttributeValue> item:items) {
            final HashSet<String> accountDeviceIdAttributes = new HashSet<String>();
            Collections.addAll(targetAttributes,
                    MORPHEUS_ID_ATTRIBUTE_NAME, ACCOUNT_ID_ATTRIBUTE_NAME);
            if(!item.keySet().containsAll(accountDeviceIdAttributes)){
                LOGGER.warn("Corrupted row retrieved for device {}", deviceId);
                continue;
            }

            final long accountId = Long.valueOf(item.get(ACCOUNT_ID_ATTRIBUTE_NAME).getN());
            Optional<List<Alarm>> alarmListOptional = getAlarmListFromAttributes(deviceId, accountId, item);
            Optional<RingTime> ringTimeOptional = getRingTimeFromAttributes(deviceId, accountId, item);
            Optional<DateTimeZone> dateTimeZoneOptional = getTimeZoneFromAttributes(deviceId, accountId, item);
            alarmInfos.add(new AlarmInfo(deviceId, accountId, alarmListOptional, ringTimeOptional, dateTimeZoneOptional));
        }

        return ImmutableList.copyOf(alarmInfos);
    }

    public static Optional<List<Alarm>> getAlarmListFromAttributes(final String deviceId, final long accountId, final Map<String, AttributeValue> item){
        final HashSet<String> alarmAttributes = new HashSet<String>();
        Collections.addAll(alarmAttributes, ALARM_TEMPLATES_ATTRIBUTE_NAME);

        if(!item.keySet().containsAll(alarmAttributes)){
            return Optional.absent();
        }

        final String alarmListJSON = item.get(ALARM_TEMPLATES_ATTRIBUTE_NAME).getS();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final List<Alarm> alarmList = mapper.readValue(alarmListJSON, new TypeReference<List<Alarm>>(){});
            return Optional.of(alarmList);
        } catch (IOException e) {
            LOGGER.error("Deserialize JSON for alarm list failed, device {}, account id {}.", deviceId, accountId);
        }

        return Optional.absent();
    }


    public static Optional<RingTime> getRingTimeFromAttributes(final String deviceId, final long accountId, final Map<String, AttributeValue> item){
        final HashSet<String> alarmAttributes = new HashSet<String>();
        Collections.addAll(alarmAttributes, ACTUAL_RING_TIME_ATTRIBUTE_NAME, EXPECTED_RING_TIME_ATTRIBUTE_NAME, SOUND_IDS_ATTRIBUTE_NAME);

        if(!item.keySet().containsAll(alarmAttributes)){
            return Optional.absent();
        }

        final long expected = Long.valueOf(item.get(EXPECTED_RING_TIME_ATTRIBUTE_NAME).getN());
        final long actual = Long.valueOf(item.get(ACTUAL_RING_TIME_ATTRIBUTE_NAME).getN());

        final String soundArrayJSON = item.get(SOUND_IDS_ATTRIBUTE_NAME).getS();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final long[] soundIds = mapper.readValue(soundArrayJSON, long[].class);
            return Optional.of(new RingTime(actual, expected, soundIds));
        } catch (IOException e) {
            LOGGER.error("Deserialize JSON for ring time failed {}, device {}, account id {}.", e.getMessage(), deviceId, accountId);
        }

        return Optional.absent();
    }


    public static Optional<DateTimeZone> getTimeZoneFromAttributes(final String deviceId, final long accountId, final Map<String, AttributeValue> item){
        final HashSet<String> alarmAttributes = new HashSet<String>();
        Collections.addAll(alarmAttributes, TIMEZONE_ID_ATTRIBUTE_NAME);

        if(!item.keySet().containsAll(alarmAttributes)){
            return Optional.absent();
        }

        final String timeZoneId = item.get(TIMEZONE_ID_ATTRIBUTE_NAME).getS();
        try{
            return Optional.of(DateTimeZone.forID(timeZoneId));
        }catch (Exception ex){
            LOGGER.error("Create timezone failed {}, device {}, account id {}.", ex.getMessage(), deviceId, accountId);
        }

        return Optional.absent();
    }


    public MergedAlarmInfoDynamoDB(final AmazonDynamoDBClient dynamoDBClient, final String tableName){
        this.dynamoDBClient = dynamoDBClient;
        this.tableName = tableName;
    }

    public static CreateTableResult createTable(final String tableName, final AmazonDynamoDBClient dynamoDBClient){
        final CreateTableRequest request = new CreateTableRequest().withTableName(tableName);

        request.withKeySchema(
                new KeySchemaElement().withAttributeName(MORPHEUS_ID_ATTRIBUTE_NAME).withKeyType(KeyType.HASH),
                new KeySchemaElement().withAttributeName(ACCOUNT_ID_ATTRIBUTE_NAME).withKeyType(KeyType.RANGE)
        );

        request.withAttributeDefinitions(
                new AttributeDefinition().withAttributeName(MORPHEUS_ID_ATTRIBUTE_NAME).withAttributeType(ScalarAttributeType.S),
                new AttributeDefinition().withAttributeName(ACCOUNT_ID_ATTRIBUTE_NAME).withAttributeType(ScalarAttributeType.N)

        );


        request.setProvisionedThroughput(new ProvisionedThroughput()
                .withReadCapacityUnits(1L)
                .withWriteCapacityUnits(1L));

        final CreateTableResult result = dynamoDBClient.createTable(request);
        return result;
    }

}
