package com.hello.suripu.core.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import java.util.HashMap;
import java.util.Map;

public class PublicKeyStoreDynamoDB implements PublicKeyStore {

    private final AmazonDynamoDBClient dynamoDBClient;
    private final String keyStoreTableName;

    private final static String DEVICE_ID_ATTRIBUTE_NAME = "device_id";
    private final static String PUBLIC_KEY_ATTRIBUTE_NAME = "public_key";

    public PublicKeyStoreDynamoDB(
            final AmazonDynamoDBClient dynamoDBClient,
            final String keyStoreTableName) {
        this.dynamoDBClient = dynamoDBClient;
        this.keyStoreTableName = keyStoreTableName;
    }

    @Override
    public byte[] get(final String deviceId) {
        final HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put(DEVICE_ID_ATTRIBUTE_NAME, new AttributeValue().withS(deviceId));
        final GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(keyStoreTableName)
                .withKey(key);

        final GetItemResult getItemResult = dynamoDBClient.getItem(getItemRequest);
        final String base64EncodedPublicKey = getItemResult.getItem().get(PUBLIC_KEY_ATTRIBUTE_NAME).getS();
        return base64EncodedPublicKey.getBytes();
    }

    @Override
    public void put(final String deviceId, final String publicKey) {
        final Map<String, AttributeValue> attributes = new HashMap<String, AttributeValue>();
        attributes.put(DEVICE_ID_ATTRIBUTE_NAME, new AttributeValue().withS(deviceId));
        attributes.put(PUBLIC_KEY_ATTRIBUTE_NAME, new AttributeValue().withS(publicKey));

//        final HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
//        key.put("device_id", new AttributeValue().withS(deviceId));
        final PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName(keyStoreTableName)
                .withItem(attributes);

        final PutItemResult putItemResult = dynamoDBClient.putItem(putItemRequest);
        // TODO: Log consumed capacity
    }
}
