package com.hello.suripu.admin.cli;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.hello.suripu.admin.configuration.SuripuAdminConfiguration;
import com.hello.suripu.core.db.FirmwareVersionMappingDAO;
import com.hello.suripu.core.configuration.DynamoDBConfiguration;
import com.hello.suripu.core.db.SenseEventsDAO;
import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

public class CreateDynamoDBTables extends ConfiguredCommand<SuripuAdminConfiguration> {

    public CreateDynamoDBTables() {
        super("create_dynamodb_tables", "Create dynamoDB tables");
    }

    @Override
    protected void run(Bootstrap<SuripuAdminConfiguration> bootstrap, Namespace namespace, SuripuAdminConfiguration configuration) throws Exception {
        final AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();

        createSenseEventsTable(configuration, awsCredentialsProvider);
        createFirmwareVersionsMappingTable(configuration, awsCredentialsProvider);
    }

    private void createSenseEventsTable(final SuripuAdminConfiguration configuration, final AWSCredentialsProvider awsCredentialsProvider) {
        final DynamoDBConfiguration config = configuration.getSenseEventsDBConfiguration();
        final AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCredentialsProvider);

        client.setEndpoint(config.getEndpoint());
        final String tableName = config.getTableName();
        try {
            client.describeTable(tableName);
            System.out.println(String.format("%s already exists.", tableName));
        } catch (AmazonServiceException exception) {
            final CreateTableResult result = SenseEventsDAO.createTable(tableName, client);
            final TableDescription description = result.getTableDescription();
            System.out.println(description.getTableStatus());
        }
    }

    private void createFirmwareVersionsMappingTable(final SuripuAdminConfiguration configuration, final AWSCredentialsProvider awsCredentialsProvider) {
        final DynamoDBConfiguration config = configuration.getFirmwareVersionsDynamoDBConfiguration();
        final AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCredentialsProvider);

        client.setEndpoint(config.getEndpoint());
        final String tableName = config.getTableName();
        try {
            client.describeTable(tableName);
            System.out.println(String.format("%s already exists.", tableName));
        } catch (AmazonServiceException exception) {
            final CreateTableResult result = FirmwareVersionMappingDAO.createTable(tableName, client);
            final TableDescription description = result.getTableDescription();
            System.out.println(description.getTableStatus());
        }
    }
}
