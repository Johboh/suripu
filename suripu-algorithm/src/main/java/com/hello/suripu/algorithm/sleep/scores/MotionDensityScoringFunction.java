package com.hello.suripu.algorithm.sleep.scores;

import com.hello.suripu.algorithm.core.AmplitudeData;
import com.hello.suripu.algorithm.pdf.LinearRankAscendingScoringFunction;
import com.hello.suripu.algorithm.pdf.LinearRankDescendingScoringFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pangwu on 1/15/15.
 */
public class MotionDensityScoringFunction implements SleepDataScoringFunction<AmplitudeData> {

    public static enum ScoreType {
        SLEEP,
        WAKE_UP
    }
    private final static Logger LOGGER = LoggerFactory.getLogger(MotionDensityScoringFunction.class);
    private final double motionMaxPower;
    private final ScoreType type;

    public MotionDensityScoringFunction(final ScoreType type){
        this.motionMaxPower = 9;
        this.type = type;
    }

    @Override
    public Map<AmplitudeData, EventScores> getPDF(Collection<AmplitudeData> data) {
        final List<Long> timestamps = new ArrayList<>(data.size());
        final List<Double> amplitudes = new ArrayList<>(data.size());
        for(final AmplitudeData amplitudeData:data){
            timestamps.add(amplitudeData.timestamp);
            amplitudes.add(amplitudeData.amplitude);
        }

        final LinearRankDescendingScoringFunction<Long> sleepTimeScoreFunction =
                new LinearRankDescendingScoringFunction<>(1d, 0.0d, new double[]{0d, 0.5d});  // sleep time should be desc
        final LinearRankAscendingScoringFunction<Double> sleepMotionScoringFunction =
                new LinearRankAscendingScoringFunction<>(0d, 1d, new double[]{0d, 1d});

        final LinearRankAscendingScoringFunction<Long> wakeUpTimeScoreFunction =
                new LinearRankAscendingScoringFunction<>(0d, 1d, new double[]{0.5d, 1d});
        final LinearRankAscendingScoringFunction<Double> wakeUpMotionScoringFunction =
                new LinearRankAscendingScoringFunction<>(0d, 1d, new double[]{0d, 1d});

        final Map<Long, Double> sleepTimePDF = sleepTimeScoreFunction.getPDF(timestamps);
        final Map<Long, Double> wakeUpTimePDF = wakeUpTimeScoreFunction.getPDF(timestamps);

        final Map<Double, Double> sleepMotionDensityRankPDF = sleepMotionScoringFunction.getPDF(amplitudes);
        final Map<Double, Double> wakeUpMotionDensityRankPDF = wakeUpMotionScoringFunction.getPDF(amplitudes);

        final HashMap<AmplitudeData, EventScores> pdf = new HashMap<>();

        for(final AmplitudeData datum:data){
            if(type == ScoreType.SLEEP) {
                final double sleepMotionDensityScore = datum.amplitude == 0 ? 0 : sleepMotionDensityRankPDF.get(datum.amplitude);
                final double sleepTimeScore = sleepTimePDF.get(datum.timestamp);
                pdf.put(datum, new EventScores(Math.pow(sleepMotionDensityScore, this.motionMaxPower) * sleepTimeScore,
                        1d, 1d, 1d));
                /*
                LOGGER.debug("    density {}: t {}, sl_r {}, wup 0, val {}",
                        new DateTime(datum.timestamp, DateTimeZone.forOffsetMillis(datum.offsetMillis)),
                        sleepTimeScore,
                        Math.pow(sleepMotionDensityScore, this.motionMaxPower),
                        datum.amplitude);
                */
            }

            if(this.type == ScoreType.WAKE_UP) {
                final double wakeUpMotionDensityScore = datum.amplitude == 0 ? 0 : wakeUpMotionDensityRankPDF.get(datum.amplitude);
                final double wakeUpTimeScore = wakeUpTimePDF.get(datum.timestamp);
                pdf.put(datum, new EventScores(1d,
                        Math.pow(wakeUpMotionDensityScore, this.motionMaxPower) * wakeUpTimeScore, 1d, 1d));
                /*
                LOGGER.debug("    density {}: wt {}, sl_r 0, wup {}, val {}",
                        new DateTime(datum.timestamp, DateTimeZone.forOffsetMillis(datum.offsetMillis)),
                        wakeUpTimeScore,
                        wakeUpMotionDensityScore,
                        datum.amplitude);
                        */

            }

        }
        return pdf;
    }
}
