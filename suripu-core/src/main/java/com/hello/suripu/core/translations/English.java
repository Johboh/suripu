package com.hello.suripu.core.translations;


public class English {

    /* BEGIN Events Declaration */

    public final static String FALL_ASLEEP_MESSAGE = "You fell asleep";
    public final static String IN_BED_MESSAGE = "You went to bed";
    public final static String LIGHT_MESSAGE = "";
    public final static String LIGHTS_OUT_MESSAGE = "";
    public final static String MOTION_MESSAGE = "Motion detected";
    public final static String NULL_MESSAGE = "";
    public final static String OUT_OF_BED_MESSAGE = "";
    public final static String PARTNER_MOTION_MESSAGE = "Your partner kicked you at";
    public final static String SLEEP_MOTION_MESSAGE = "Toss and turns";
    public final static String SUNRISE_MESSAGE = "";
    public final static String SUNSET_MESSAGE = "";
    public final static String WAKE_UP_MESSAGE = "";

    /* END Events Declaration */


    /* BEGIN Current Room State Declaration */

    // Be careful with changing the markdown & string specifiers i.e. **XX%s**, it could break the code

    // Loading States
    public final static String LOADING_TEMPERATURE_MESSAGE = "Waiting for data.";
    public final static String LOADING_HUMIDITY_MESSAGE = "Waiting for data.";
    public final static String LOADING_PARTICULATES_MESSAGE = "Waiting for data.";
    public final static String LOADING_LIGHT_MESSAGE = "Waiting for data.";
    public final static String LOADING_SOUND_MESSAGE = "Waiting for data.";

    // Unknown States
    public final static String UNKNOWN_TEMPERATURE_MESSAGE = "Could not retrieve a recent temperature reading";
    public final static String UNKNOWN_HUMIDITY_MESSAGE = "Could not retrieve a recent humidity reading";
    public final static String UNKNOWN_PARTICULATES_MESSAGE = "Could not retrieve a recent particulates reading";
    public final static String UNKNOWN_LIGHT_MESSAGE = "Could not retrieve a recent light reading";
    public final static String UNKNOWN_SOUND_MESSAGE = "Could not retrieve a recent sound reading";

    // Advices
    public final static String TEMPERATURE_ADVICE_MESSAGE = "You sleep better when temperature is between **XX%s** and **YY%s**.";
    public final static String HUMIDITY_ADVICE_MESSAGE = "You sleep better when humidity is between **XX** and **YY**.";
    public final static String PARTICULATES_ADVICE_MESSAGE = "You sleep better when particulates are below **XX**.";
    public final static String LIGHT_ADVICE_MESSAGE = "A pitch-black room is usually better";
    public final static String SOUND_ADVICE_MESSAGE = "You sleep better your room is quiet";

    // Temperature Conditions
    public final static String LOW_TEMPERATURE_MESSAGE = "It’s **pretty cold** in here.";
    public final static String HIGH_TEMPERATURE_MESSAGE = "It’s **pretty hot** in here.";
    public final static String IDEAL_TEMPERATURE_MESSAGE = "Temperature is **just right**.";

    // Humidity Conditions
    public final static String LOW_HUMIDITY_MESSAGE = "It’s **pretty dry** in here.";
    public final static String HIGH_HUMIDITY_MESSAGE = "It’s **pretty hot** in here.";
    public final static String IDEAL_HUMIDITY_MESSAGE = "Humidity is **just right**.";

    // Particulates Conditions (Air Quality)
    public final static String VERY_HIGH_PARTICULATES_MESSAGE = "AQI is at an **UNHEALTHY** level.";
    public final static String HIGH_PARTICULATES_MESSAGE = "AQI is **moderately high**.";
    public final static String IDEAL_PARTICULATES_MESSAGE = "Particulates level is **just right**.";

    // Light Conditions
    public final static String IDEAL_LIGHT_MESSAGE = "Light level is **just right**";

    // Sound Conditions
    public final static String IDEAL_SOUND_MESSAGE = "Sound level is **just right**";
    /* END Current Room State Declaration */
}