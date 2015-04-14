package com.hello.suripu.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class SleepStats {

    @JsonProperty("sound_sleep")
    final public Integer soundSleepDurationInMinutes;

    @JsonIgnore
    final public Integer lightSleepDurationInMinutes;

    @JsonProperty("total_sleep")
    final public Integer sleepDurationInMinutes;

    @JsonProperty("times_awake")
    final public Integer numberOfMotionEvents;

    @JsonIgnore
    final public Long sleepTime;

    @JsonIgnore
    final public Long wakeTime;

    @JsonProperty("time_to_sleep")
    final public Integer sleepOnsetTimeMinutes;

    @JsonIgnore
    public final boolean isInBedDuration;

    public SleepStats(final Integer soundSleepDurationInMinutes, final Integer lightSleepDurationInMinutes,
                      final Integer sleepDurationInMinutes, final boolean isInBedDuration,
                      final Integer numberOfMotionEvents,
                      final Long sleepTime, final Long wakeTime, final Integer sleepOnsetTimeMinutes) {
        this.soundSleepDurationInMinutes = soundSleepDurationInMinutes;
        this.lightSleepDurationInMinutes = lightSleepDurationInMinutes;
        this.sleepDurationInMinutes = sleepDurationInMinutes;
        this.numberOfMotionEvents = numberOfMotionEvents;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.sleepOnsetTimeMinutes = sleepOnsetTimeMinutes;
        this.isInBedDuration = isInBedDuration;
    }


    @JsonCreator
    public static SleepStats create(
            @JsonProperty("sound_sleep") Integer soundSleepDurationInMinutes,
            @JsonProperty("total_sleep") Integer sleepDurationInMinutes,
            @JsonProperty("times_awake") Integer numberOfMotionEvents,
            @JsonProperty("time_to_sleep") Integer sleepOnsetTimeMinutes) {

        return new SleepStats(soundSleepDurationInMinutes, 0,
                sleepDurationInMinutes, true,
                numberOfMotionEvents, 0L, 0L, sleepOnsetTimeMinutes);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(SleepStats.class)
                .add("soundSleep", sleepDurationInMinutes)
                .add("lightSleep", lightSleepDurationInMinutes)
                .add("totalSleep", sleepDurationInMinutes)
                .add("# of motion events", numberOfMotionEvents)
                .add("sleep time", sleepTime)
                .add("wake time", wakeTime)
                .add("time to fall asleep", sleepOnsetTimeMinutes)
                .toString();
    }

    @JsonIgnore
    public boolean isFromNull(){
        return Objects.equal(this.soundSleepDurationInMinutes, null);
    }
}
