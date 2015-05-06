package com.hello.suripu.admin.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hello.suripu.core.configuration.KinesisConfiguration;
import com.hello.suripu.core.configuration.KinesisLoggerConfiguration;
import com.hello.suripu.core.configuration.NewDynamoDBConfiguration;
import com.hello.suripu.core.configuration.RedisConfiguration;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SuripuAdminConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("sensors_db")
    private DatabaseConfiguration sensorsDB = new DatabaseConfiguration();

    public DatabaseConfiguration getSensorsDB() {
        return sensorsDB;
    }

    @Valid
    @NotNull
    @JsonProperty("common_db")
    private DatabaseConfiguration commonDB = new DatabaseConfiguration();

    public DatabaseConfiguration getCommonDB() {
        return commonDB;
    }


    @Valid
    @JsonProperty("debug")
    private Boolean debug = Boolean.FALSE;

    public Boolean getDebug() {
        return debug;
    }

    @Valid
    @NotNull
    @JsonProperty("kinesis")
    private KinesisConfiguration kinesisConfiguration;

    public KinesisConfiguration getKinesisConfiguration() {
        return kinesisConfiguration;
    }


    @Valid
    @NotNull
    @JsonProperty("kinesis_logger")
    private KinesisLoggerConfiguration kinesisLoggerConfiguration;

    public KinesisLoggerConfiguration getKinesisLoggerConfiguration() {
        return kinesisLoggerConfiguration;
    }


    @Valid
    @NotNull
    @JsonProperty("redis")
    private RedisConfiguration redisConfiguration;

    public RedisConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }


    @Valid
    @NotNull
    @JsonProperty("dynamodb")
    private NewDynamoDBConfiguration dynamoDBConfiguration;
    public NewDynamoDBConfiguration dynamoDBConfiguration(){
        return dynamoDBConfiguration;
    }
}