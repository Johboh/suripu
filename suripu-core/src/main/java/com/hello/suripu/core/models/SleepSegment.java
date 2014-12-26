package com.hello.suripu.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.hello.suripu.core.util.EventTypeSerializer;
import com.hello.suripu.core.util.EventUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;

import java.util.List;

public class SleepSegment implements Comparable {

    public static class SoundInfo {

        @JsonProperty("url")
        public final String url;

        @JsonProperty("duration_millis")
        public final Integer durationMillis;

        public SoundInfo(final String url, final Integer durationMillis) {
            this.url = url;
            this.durationMillis = durationMillis;
        }

        public static SoundInfo empty(){
            return new SoundInfo("", 0);
        }

        @JsonProperty("is_empty")
        public boolean isEmpty(){
            return this.url.equals("") && this.durationMillis == 0;
        }
    }

    private final Event event;

    @JsonProperty("id")
    final public Long id;

    @JsonProperty("sensors")
    final public List<SensorReading> sensors;

    @JsonProperty("sleep_depth")
    public Integer getSleepDepth(){
        return this.event.getSleepDepth();
    }

    @JsonProperty("sound")
    final public SoundInfo getSound(){
        return this.event.getSoundInfo();
    }


    @JsonProperty("timestamp")
    public long getTimestamp(){
        return this.event.getStartTimestamp();
    }

    @JsonProperty("duration")
    public int getDurationInSeconds(){
        return EventUtil.getEventDurationInSecond(this.getEvent());
    }

    @JsonProperty("offset_millis")
    public int getOffsetMillis(){
        return this.event.getTimezoneOffset();
    }

    @JsonProperty("event_type")
    @JsonSerialize(using = EventTypeSerializer.class)
    public Event.Type getType(){
        return this.event.getType();
    }

    @JsonProperty("message")
    public String getMessage(){
        return this.event.getDescription();
    }

    @JsonIgnore
    public Event getEvent(){
        return this.event;
    }


    public SleepSegment(final Long id,
                        final Event event,
                        final List<SensorReading> sensors) {
        this.event = event;
        this.id = id;
        this.sensors = sensors;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(SleepSegment.class)
                .add("id", this.id)
                .add("timestamp", this.getTimestamp())
                .add("offsetMillis", this.getOffsetMillis())
                .add("durationInSeconds", this.getDurationInSeconds())
                .add("sleepDepth", this.getSleepDepth())
                .add("eventType", this.getType())
                .add("message", this.getMessage())
                .add("sensors", this.sensors)
                .add("soundInfo", this.getSound())
                .add("from", new DateTime(this.getTimestamp(), DateTimeZone.forOffsetMillis(this.getOffsetMillis())))
                .add("to", new DateTime(this.getTimestamp() + this.getDurationInSeconds() * DateTimeConstants.MILLIS_PER_SECOND,
                        DateTimeZone.forOffsetMillis(this.getOffsetMillis())))
                .add("$minutes", this.getDurationInSeconds() / DateTimeConstants.SECONDS_PER_MINUTE)
                .toString();
    }

    @Override
    public int compareTo(final Object o) {
        final SleepSegment that = (SleepSegment) o;
        return ComparisonChain.start()
                .compare(this.getTimestamp(), that.getTimestamp())
                .result();
    }
}
