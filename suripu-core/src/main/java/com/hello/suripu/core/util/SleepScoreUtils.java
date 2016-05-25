package com.hello.suripu.core.util;

import com.google.common.base.Optional;
import com.hello.suripu.core.models.MotionScore;
import com.hello.suripu.core.models.Sample;
import com.hello.suripu.core.models.TrackerMotion;
import com.hello.suripu.core.processors.insights.Lights;
import com.hello.suripu.core.processors.insights.Particulates;
import com.hello.suripu.core.processors.insights.SleepDuration;
import com.hello.suripu.core.processors.insights.TemperatureHumidity;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ksg on 02/25/15
 */
public class SleepScoreUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SleepScoreUtils.class);

    public static final Integer DURATION_MAX_SCORE = 80;
    public static final Integer DURATION_MIN_SCORE = 10;
    public static final float MOTION_SCORE_MIN = 10.0f;
    private static final float MOTION_SCORE_RANGE = 80.0f; // max score is 90
    private static final float MIN_ASLEEP_MINUTES_REQUIRED = 60.0f; // need to be asleep for at least 60 minutes

    public static final int PENALTY_PER_SOUND_EVENT = 20;
    public static final int SENSOR_IDEAL_SCORE = 100;
    public static final int SENSOR_WARNING_SCORE = 75;
    public static final int SENSOR_ALERT_SCORE = 50;

    public static final Float DURATION_SCORE_SCALE = (float) (DURATION_MAX_SCORE - DURATION_MIN_SCORE);
    public static final Integer TOO_LITTLE_SLEEP_ALLOWANCE = 1;
    public static final Integer TOO_MUCH_SLEEP_ALLOWANCE = 4; // allow too much sleep recommendation to exceed by 4 hours

    public static final Integer DURATION_V2_MIN_SCORE_HOURS = 2;
    public static final Integer DURATION_TOO_MUCH_HOURS = 15;
    public static final Integer DURATION_TOO_MUCH_SCORE = 50;

    public static final Integer MAX_TIMES_AWAKE_PENALTY_SCORE = -30;
    public static final Integer AWAKE_PENALTY_SCORE = -5; // minus 5 for each time-awake
    /**
     * compute a score based on sleep duration.
     * if duration is within recommended range, return max score
     * if duration is way out of the min/max + allowance recommended range, return min score
     * everything else is a linear decrease in score
     * @param userAgeInYears age
     * @param sleepDurationMinutes sleep duration
     * @return duration score
     */
    public static Integer getSleepDurationScore(final int userAgeInYears, final Integer sleepDurationMinutes) {
        final SleepDuration.recommendation idealHours = SleepDuration.getSleepDurationRecommendation(userAgeInYears);

        final Float sleepDurationHours = (float) sleepDurationMinutes / 60.0f;
        if (sleepDurationHours >= (float) idealHours.minHours && sleepDurationHours <= idealHours.maxHours) {
            return DURATION_MAX_SCORE;
        }

        if (sleepDurationHours < (float) (idealHours.absoluteMinHours - TOO_LITTLE_SLEEP_ALLOWANCE) ||
                sleepDurationHours > (float) (idealHours.absoluteMaxHours + TOO_MUCH_SLEEP_ALLOWANCE)) {
            return DURATION_MIN_SCORE;
        }

        Float diffMinutes;
        Float scaleMinutes;
        if (sleepDurationHours < (float) idealHours.minHours) {
            // under-sleep
            diffMinutes = (float) (idealHours.minHours * 60  - sleepDurationMinutes);
            scaleMinutes = (idealHours.minHours - idealHours.absoluteMinHours + 1) * 60.0f;
        } else {
            // over-slept
            diffMinutes = (float) (sleepDurationMinutes - idealHours.maxHours * 60);
            scaleMinutes = (idealHours.absoluteMaxHours + TOO_MUCH_SLEEP_ALLOWANCE - idealHours.maxHours) * 60.0f;
        }
        return DURATION_MAX_SCORE - Math.round((diffMinutes / scaleMinutes) * DURATION_SCORE_SCALE);
    }

    public static Integer getSleepDurationScoreV2(final int userAgeInYears,  final Integer sleepDurationMinutes) {
        final SleepDuration.recommendation idealHours = SleepDuration.getSleepDurationRecommendation(userAgeInYears);
        final Float sleepDurationHours = (float) sleepDurationMinutes / 60.0f;

        final float baseScore;
        final float topScore;
        final float diffMinutes;
        final float bucketTotalMinutes;

        if (sleepDurationHours < DURATION_V2_MIN_SCORE_HOURS) {
            // if you sleep less than 2 hours, you're doomed.
            return DURATION_MIN_SCORE;
        }

        else if (sleepDurationHours > DURATION_TOO_MUCH_HOURS) {
            // sleep too much, doomed as well.
            return DURATION_TOO_MUCH_SCORE;
        }

        else if (sleepDurationHours < idealHours.absoluteMinHours) {
            // between 2 hours to ideal-min
            // score is between 10 - 60
            // this awards the user 1 extra point for every ~5 minutes of sleep
            baseScore = (float) DURATION_MIN_SCORE;
            topScore = 70.0f;
            bucketTotalMinutes = idealHours.absoluteMinHours - DURATION_V2_MIN_SCORE_HOURS;
            diffMinutes = sleepDurationMinutes - (DURATION_V2_MIN_SCORE_HOURS * 60.0f);
        }

        else if (sleepDurationHours < idealHours.maxHours) {
            // between absolute min and ideal-max, score between 60 - 95
            baseScore = 70.0f;
            topScore = 95.0f;
            bucketTotalMinutes = idealHours.maxHours - idealHours.absoluteMinHours;
            diffMinutes = sleepDurationMinutes - (idealHours.absoluteMinHours * 60.0f);
        }

        // between ideal-max and absolute max, score between 70 - 80
        else if (sleepDurationHours < idealHours.absoluteMaxHours) {
            baseScore = 90.0f;
            topScore = 70.0f;
            bucketTotalMinutes = idealHours.absoluteMaxHours - idealHours.maxHours;
            diffMinutes = sleepDurationMinutes - (idealHours.maxHours * 60.0f);
        }

        else {
            // between absolute-max and way-too-much sleep, reduce score
            baseScore = 70.0f;
            topScore = DURATION_TOO_MUCH_SCORE;
            bucketTotalMinutes = DURATION_TOO_MUCH_HOURS - idealHours.absoluteMaxHours;
            diffMinutes = sleepDurationMinutes - (idealHours.absoluteMaxHours) * 60.0f;
        }

        return Math.round(baseScore + diffMinutes * ((topScore - baseScore) / (bucketTotalMinutes * 60.0f)));
    }


    public static Integer getSleepScoreDurationV3(final int userAgeInYears, final Integer sleepDurationThreshold, final Integer sleepDurationMinutes) {
        final SleepDuration.recommendation idealHours = SleepDuration.getSleepDurationRecommendation(userAgeInYears);
        final float rawScore;
        final int durationMin = 120; //2 hours
        final int durationMax = 720; //12 hours
        final float rawScoreMin = 39.32f;
        final float rawScoreMax = 56.63f;
        final Integer adjSleepDuration, sleepDurationTarget;
        final Integer sleepDurationPopIdeal = 460; //median sleep duration for great quality sleep

        //Sets sleep duration target to individualized ideal within age-specific range
        if (sleepDurationThreshold == 0){

            if (userAgeInYears < 6){
                sleepDurationTarget = 690;
            }

            else if (userAgeInYears < 14){
                sleepDurationTarget = 600;
            }

            else if (userAgeInYears < 18){
                sleepDurationTarget = 540;
            }

            else {
                sleepDurationTarget = sleepDurationPopIdeal;
            }


        }
        else if (sleepDurationThreshold > idealHours.maxHours*60) {
            sleepDurationTarget = idealHours.maxHours*60;
        }

        else if (sleepDurationThreshold < idealHours.minHours*60) {
            sleepDurationTarget = idealHours.minHours*60;
        }

        else {
            sleepDurationTarget = sleepDurationThreshold;
        }

        //Adjusted sleep duration based on deviations from population mean
        adjSleepDuration = sleepDurationMinutes + (sleepDurationPopIdeal - sleepDurationTarget);

        if (adjSleepDuration < durationMin) {
            rawScore = rawScoreMin;
        }

        else if (adjSleepDuration > durationMax) {
            rawScore = 52.75f;
        }

        //rawScore calculated using 5th degree polynomial model to extrapolate change in sleep quality with sleep duration
        else{
            rawScore = Math.round((14.8027f + (4.3001e-01) * adjSleepDuration + (-2.7177e-03) * (Math.pow(adjSleepDuration, 2)) + (8.2262e-06) * (Math.pow(adjSleepDuration, 3)) + (-1.1033e-08) * (Math.pow(adjSleepDuration, 4)) + ((5.333e-12) * (Math.pow(adjSleepDuration, 5))))*100)/100;
        }

        //normalize rawscore  (score range: 0 to 100)
        return Math.round((rawScore-rawScoreMin)/(rawScoreMax-rawScoreMin)*100);
    }

        /**
         * Compute motion score based on average number of agitation during sleep.
         * score ranges from 10 to 90. A ZERO score actually means no score is computed.
         * @param targetDate nightdate
         * @param trackerMotions pill data
         * @param fallAsleepTimestamp detected fell asleep time
         * @param wakeUpTimestamp detected woke up time
         * @return a score for motion during the night
         */
    public static MotionScore getSleepMotionScore(final DateTime targetDate, final List<TrackerMotion> trackerMotions, final Long fallAsleepTimestamp, final Long wakeUpTimestamp) {
        float numAgitations = 0.0f;
        Float avgMotionAmplitude = 0.0f;
        Integer maxMotionAmplitude = 0;

        final Integer offsetMillis = trackerMotions.get(0).offsetMillis;

        // check if sleep time is valid
        Long sleepStartMillis = fallAsleepTimestamp;
        if (sleepStartMillis == 0L) {
            sleepStartMillis = targetDate.withHourOfDay(22).minusMillis(offsetMillis).getMillis();
        }

        // check if awake time is valid, sleep-duration needs to be >= 60 mins
        Long sleepStopMillis = wakeUpTimestamp;
        if (sleepStopMillis == 0L || (sleepStopMillis - sleepStartMillis) < MIN_ASLEEP_MINUTES_REQUIRED * 60000) {
            sleepStopMillis = sleepStartMillis + 12 * 3600000L;
        }

        Long firstMotionTime = 0L;
        Long lastMotionTime = 0L;

        // Compute average motion per hour
        for (final TrackerMotion motion : trackerMotions) {
            if (motion.timestamp > sleepStopMillis) {
                break;
            }

            if (motion.timestamp > sleepStartMillis) {
                if (firstMotionTime == 0L) {
                    firstMotionTime = motion.timestamp;
                }
                lastMotionTime = motion.timestamp;
                numAgitations += 1.0f;
                avgMotionAmplitude += (float) motion.value;
                if (motion.value > maxMotionAmplitude) {
                    maxMotionAmplitude = motion.value;
                }
            }
        }

        float numAsleepMinutes = (float) ((double) (lastMotionTime - firstMotionTime) / 60000.0);
        float totalScore = 0.0f;
        float score = 0.0f;
        if (numAsleepMinutes > MIN_ASLEEP_MINUTES_REQUIRED) {
            totalScore = (numAgitations / numAsleepMinutes) * 100.0f;
            score = ((100.0f - totalScore) / 100.0f) * MOTION_SCORE_RANGE + MOTION_SCORE_MIN;
        }


        // TODO: factor in motion amplitude
        if (numAgitations > 0.0f) {
            avgMotionAmplitude = avgMotionAmplitude / numAgitations;
        }

        final MotionScore motionScore = new MotionScore((int) numAgitations, (int) numAsleepMinutes,
                avgMotionAmplitude, maxMotionAmplitude, Math.round(score));


        LOGGER.trace("NEW SCORING - Mins asleep: {}, num_agitations: {}, total Score: {}, final score {}, avg Amplitude: {}, max: {}",
                numAsleepMinutes, numAgitations, totalScore, motionScore.score, avgMotionAmplitude, maxMotionAmplitude);


        return motionScore;
    }

    public static int calculateSoundScore(final int numberSoundEvents) {
        return Math.max(0, 100 - (numberSoundEvents * PENALTY_PER_SOUND_EVENT));
    }

    public static float calculateSensorAverageInTimeRange(final List<Sample> samples, final long startTime, final long endTime) {
        float sum = 0;
        int total = 0;
        for (Sample sample : samples) {
            final long dateTime = sample.dateTime;
            if (dateTime >= startTime && dateTime <= endTime) {
                sum += sample.value;
                total++;
            }
        }
        return sum / total;
    }

    public static int calculateTemperatureScore(final List<Sample> samples, final long fallAsleepTimestamp, final long wakeUpTimestamp) {
        final float average = calculateSensorAverageInTimeRange(samples, fallAsleepTimestamp, wakeUpTimestamp);
        if (average > TemperatureHumidity.ALERT_TEMP_MAX_CELSIUS) {
            return SENSOR_ALERT_SCORE;
        } else if (average > TemperatureHumidity.IDEAL_TEMP_MAX_CELSIUS) {
            return SENSOR_WARNING_SCORE;
        } else if (average < TemperatureHumidity.ALERT_TEMP_MIN_CELSIUS) {
            return SENSOR_ALERT_SCORE;
        } else if (average < TemperatureHumidity.IDEAL_TEMP_MIN_CELSIUS) {
            return SENSOR_WARNING_SCORE;
        } else {
            return SENSOR_IDEAL_SCORE;
        }
    }

    public static int calculateHumidityScore(final List<Sample> samples, final long fallAsleepTimestamp, final long wakeUpTimestamp) {
        final float average = calculateSensorAverageInTimeRange(samples, fallAsleepTimestamp, wakeUpTimestamp);
        if (average < TemperatureHumidity.ALERT_HUMIDITY_LOW) {
            return SENSOR_ALERT_SCORE;
        } else if (average < TemperatureHumidity.IDEAL_HUMIDITY_MIN) {
            return SENSOR_WARNING_SCORE;
        } else if (average > TemperatureHumidity.ALERT_HUMIDITY_HIGH) {
            return SENSOR_ALERT_SCORE;
        } else if (average > TemperatureHumidity.IDEAL_HUMIDITY_MAX) {
            return SENSOR_WARNING_SCORE;
        } else {
            return SENSOR_IDEAL_SCORE;
        }
    }

    public static int calculateLightScore(final List<Sample> samples, final long fallAsleepTimestamp, final long wakeUpTimestamp) {
        final float average = calculateSensorAverageInTimeRange(samples, fallAsleepTimestamp, wakeUpTimestamp);
        if (average > Lights.LIGHT_LEVEL_ALERT) {
            return SENSOR_ALERT_SCORE;
        } else if (average > Lights.LIGHT_LEVEL_WARNING) {
            return SENSOR_WARNING_SCORE;
        } else {
            return SENSOR_IDEAL_SCORE;
        }
    }

    public static int calculateParticulateScore(final List<Sample> samples, final long fallAsleepTimestamp, final long wakeUpTimestamp) {
        final float average = calculateSensorAverageInTimeRange(samples, fallAsleepTimestamp, wakeUpTimestamp);
        if (average > Particulates.PARTICULATE_DENSITY_MAX_WARNING) {
            return SENSOR_ALERT_SCORE;
        } else if (average > Particulates.PARTICULATE_DENSITY_MAX_IDEAL) {
            return SENSOR_WARNING_SCORE;
        } else {
            return SENSOR_IDEAL_SCORE;
        }
    }

    public static int calculateAggregateEnvironmentScore(final int soundScore, final int temperatureScore, final int humidityScore, final int lightScore, final int particulateScore) {
        return Math.round((0.2f * temperatureScore) + (0.2f * humidityScore) + (0.2f * soundScore) + (0.2f * lightScore) + (0.2f * particulateScore));
    }


    public static Optional<MotionScore> getSleepMotionScoreMaybe(final DateTime targetDate, final List<TrackerMotion> trackerMotions, final Long fallAsleepTimestamp, final Long wakeUpTimestamp) {
        try {
            return Optional.of(getSleepMotionScore(targetDate, trackerMotions, fallAsleepTimestamp, wakeUpTimestamp));
        } catch (Exception e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
        }

        return Optional.absent();
    }


    public static Integer calculateTimesAwakePenaltyScore(final int timesAwake) {
        // penalty, returns a negative score
        final int penalty = timesAwake * AWAKE_PENALTY_SCORE;
        return (penalty < MAX_TIMES_AWAKE_PENALTY_SCORE) ? MAX_TIMES_AWAKE_PENALTY_SCORE : penalty;
    }
}