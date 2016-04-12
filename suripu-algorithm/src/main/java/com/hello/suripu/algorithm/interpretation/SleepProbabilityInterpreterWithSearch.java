package com.hello.suripu.algorithm.interpretation;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.hello.suripu.algorithm.hmm.BetaPdf;
import com.hello.suripu.algorithm.hmm.HiddenMarkovModelFactory;
import com.hello.suripu.algorithm.hmm.HiddenMarkovModelInterface;
import com.hello.suripu.algorithm.hmm.HmmDecodedResult;
import com.hello.suripu.algorithm.hmm.HmmPdfInterface;
import com.hello.suripu.algorithm.hmm.PdfCompositeBuilder;
import com.hello.suripu.algorithm.hmm.PoissonPdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by benjo on 3/24/16.
 */
public class SleepProbabilityInterpreterWithSearch {

    final static Logger LOGGER = LoggerFactory.getLogger(SleepProbabilityInterpreterWithSearch.class);
    final static HmmPdfInterface[] obsModelsMain = {new BetaPdf(1.0,10.0,0),new BetaPdf(2.0,2.0,0),new BetaPdf(10.0,1.0,0)};

    final static int SLEEP_TRANSITION_STATE = 2;
    final static int WAKE_TRANSITION_STATE = 5;

    final static double MIN_HMM_PDF_EVAL = 1e-320;
    final static int MAX_ON_BED_SEARCH_WINDOW = 60; //minutes to find beginning of motion cluster
    final static int MAX_OFF_BED_SEARCH_WINDOW = 30; //minutes to find beginning of motion cluster
    final static int MAX_TIME_AWAKE_TO_SPLIT_SEGMENTS = 90;

    final protected static int DEFAULT_SPACING_OF_OUT_OF_BED_AFTER_WAKE = 1;
    final protected static int DEFAULT_SPACING_OF_IN_BED_BEFORE_SLEEP = 5;

    final static double POISSON_MEAN_FOR_A_LITTLE_MOTION = 0.1;
    final static double POISSON_MEAN_FOR_MOTION = 5.0;
    final static double POISSON_MEAN_FOR_NO_MOTION = 0.1;

    final static double MIN_SLEEP_PROB = 0.001;

    final static int MIN_SLEEP_DURATION = 60; //insanity check



    private static class MergeResult {
        final public List<IdxPair> mergedSegments;
        final public List<IdxPair> skippedOverWakePeriods;

        public MergeResult(List<IdxPair> mergedSegments, List<IdxPair> skippedOverWakePeriods) {
            this.mergedSegments = mergedSegments;
            this.skippedOverWakePeriods = skippedOverWakePeriods;
        }
    }

    private static MergeResult mergeCloseSegments(final List<IdxPair> segments, int maxDistanceToMerge) {
        final List<IdxPair> mergedSegments = Lists.newArrayList();
        final List<IdxPair> skippedOverWakePeriods = Lists.newArrayList();

        if (segments.isEmpty()) {
            return new MergeResult(mergedSegments,skippedOverWakePeriods);
        }

        IdxPair currentSegment = segments.get(0);
        for (int i = 1; i < segments.size(); i++) {
            if (segments.get(i).getDistance(currentSegment) > maxDistanceToMerge) {
                mergedSegments.add(currentSegment);
                currentSegment = segments.get(i);
            }
            else {
                final int i1WakePeriod = currentSegment.i2;
                final int i2WakePeriod = segments.get(i).i1;
                LOGGER.info("action=merge_over_wake_period seg1_wake={} seg2_sleep={} duration={}",i1WakePeriod,i2WakePeriod,i2WakePeriod-i1WakePeriod);
                skippedOverWakePeriods.add(new IdxPair(i1WakePeriod,i2WakePeriod));
                currentSegment = currentSegment.merge(segments.get(i));
            }
        }

        mergedSegments.add(currentSegment);


        return new MergeResult(mergedSegments,skippedOverWakePeriods);
    }

    /*
     *  This takes the output of a neural network that outputs p(sleep), and a motion signal (on duration seconds) from the pill
     *  and will output when it thinks you got in bed, fell asleep, woke up, and got out of bed.
     *
     *
     *
     *  There are two steps:
     *  Step 1) Segment sleep with a hidden Markov model.
     *
     *                /--->{med(2)}----\
     *               |                 v
     *{med(1)} <--> {low(0)}        {high(3)} <-----> {med(4)}
     *                ^                /
     *                \-----{med(5)}---
     *
     *  The general idea is when you go from low to high, you will have a transition period on state 2
     *  And when you go from high to low, you will have a transition period in state 5
     *
     *  When you're in state 2 you search for a sleep event
     *  When you're in state 5 you search for a wake event
     *
     *  sleep event is max d/dt[p(sleep)] weighted by how close you are to p(sleep) = 0.5
     *  so maybe weighed by p * (1 - p)
     *
     *  wake event is min d/dt[p(sleep)] * pill_svm_magnitude
     *
     *  Initial state is always state 0 or 1, and final state is always 0 or 1
     *

     */
    public static Optional<EventIndices> getEventIndices(final double [] sleepProbabilities, final double [] myMotionDurations, final double [] myPillMagintude) {

        int iSleep = -1;
        int iWake = -1;
        int iInBed = -1;
        int iOutOfBed = -1;
        final List<IdxPair> skippedOverWakePeriods = Lists.newArrayList();

        if (sleepProbabilities.length <= 1 || myMotionDurations.length <= 1 || myPillMagintude.length <= 1) {
            return Optional.absent();
        }



        final double [] sleep = sleepProbabilities.clone();

        //a sleep prob of 0.0 can screw up the decode b/c of a negative inf likelihood
        for (int t = 0; t < sleep.length; t++) {
            if (sleep[t] < MIN_SLEEP_PROB) {
                sleep[t] = MIN_SLEEP_PROB;
            }
        }


        final double [] dsleep = new double[sleep.length];

        for (int i = 1; i < dsleep.length; i++) {
            dsleep[i] = sleep[i] - sleep[i-1];
        }

        final double [][] sleepProbsWithDeltaProb = {sleep,dsleep};

        {
            final HmmPdfInterface s0 = PdfCompositeBuilder.newBuilder().withPdf(obsModelsMain[0]).build();
            final HmmPdfInterface s1 = PdfCompositeBuilder.newBuilder().withPdf(obsModelsMain[1]).build();
            final HmmPdfInterface s2 = PdfCompositeBuilder.newBuilder().withPdf(obsModelsMain[1]).build();
            final HmmPdfInterface s3 = PdfCompositeBuilder.newBuilder().withPdf(obsModelsMain[2]).build();
            final HmmPdfInterface s4 = PdfCompositeBuilder.newBuilder().withPdf(obsModelsMain[1]).build();
            final HmmPdfInterface s5 = PdfCompositeBuilder.newBuilder().withPdf(obsModelsMain[1]).build();


            final HmmPdfInterface[] obsModels = {s0, s1, s2, s3, s4, s5};

            final double[][] A = new double[obsModels.length][obsModels.length];
            A[0][0] = 0.98; A[0][1] = 0.01; A[0][2] = 0.01;
            A[1][0] = 0.02; A[1][1] = 0.98;
                                            A[2][2] = 0.98; A[2][3] = 0.02;
                                                            A[3][3] = 0.98; A[3][4] = 0.01; A[3][5] = 0.01;
                                                            A[4][3] = 0.05; A[4][4] = 0.95;
            A[5][0] = 0.02;                                                                 A[5][5] = 0.98;



            final double[] pi = new double[obsModels.length];
            pi[0] = 0.9;
            pi[1] = 0.1;

            //segment this shit
            final HiddenMarkovModelInterface hmm = HiddenMarkovModelFactory.create(HiddenMarkovModelFactory.HmmType.LOGMATH, obsModels.length, A, pi, obsModels, 0);

            final HmmDecodedResult res = hmm.decode(sleepProbsWithDeltaProb, new Integer[]{0,1}, MIN_HMM_PDF_EVAL);


            if (res.bestPath.size() <= 1) {
                LOGGER.info("action=return_invalid_indices reason=path_size_less_than_one");
                return Optional.absent();
            }
/*
        float [] f = new float[sleep.length];
        for (int i = 0; i < sleep.length; i++) {
            f[i] = (float)sleep[i];
        }
        LOGGER.info("\n{}\n{}",f,res.bestPath);
*/

            final List<Integer> sleepTransitionIndices = Lists.newArrayList();
            final List<Integer> wakeTransitionIndices = Lists.newArrayList();


            Integer prevState = res.bestPath.get(0);
            for (int i = 1; i < res.bestPath.size(); i++) {
                final Integer state = res.bestPath.get(i);

                if (!state.equals(prevState)) {
                    LOGGER.info("action=hmm_decode from_state={} to_state={} index={}", prevState, state, i);
                }

                if (state.equals(SLEEP_TRANSITION_STATE) && !prevState.equals(SLEEP_TRANSITION_STATE)) {
                    sleepTransitionIndices.add(i);
                }

                if (!state.equals(SLEEP_TRANSITION_STATE) && prevState.equals(SLEEP_TRANSITION_STATE)) {
                    sleepTransitionIndices.add(i-1);
                }

                if (state.equals(WAKE_TRANSITION_STATE) && !prevState.equals(WAKE_TRANSITION_STATE)) {
                    wakeTransitionIndices.add(i);
                }

                if (!state.equals(WAKE_TRANSITION_STATE) && prevState.equals(WAKE_TRANSITION_STATE)) {
                    wakeTransitionIndices.add(i-1);
                }

                prevState = state;
            }

            //test if odd number of wake transtions.  Due to setup of HMM this should be impossible.
            if ((wakeTransitionIndices.size() / 2 * 2) != wakeTransitionIndices.size()) {
                LOGGER.info("action=return_invalid_indices reason=odd_number_of_wake_transitions");
                return Optional.absent();
            }

            //test if odd number of sleep transtions.  Due to setup of HMM this should be impossible.
            if ((sleepTransitionIndices.size() / 2 * 2) != sleepTransitionIndices.size()) {
                LOGGER.info("action=return_invalid_indices reason=odd_number_of_sleep_transitions");
                return Optional.absent();
            }

            //test if number of sleep transitions is equal to the number of wakes
            if (sleepTransitionIndices.size() != wakeTransitionIndices.size()) {
                LOGGER.info("action=return_invalid_indices reason=num_sleep_neq_num_wake_transitions");
                return Optional.absent();
            }

            //make sure there's enough to work with here
            if (sleepTransitionIndices.size() < 2) {
                LOGGER.info("action=return_invalid_indices reason=never_transitioned_to_sleep");
                return Optional.absent();
            }

            if (wakeTransitionIndices.size() < 2) {
                LOGGER.info("action=return_invalid_indices reason=never_transitioned_to_wake");
                return Optional.absent();
            }

            final List<IdxPair> segments = Lists.newArrayList();
            for (int i = 0; i < wakeTransitionIndices.size() / 2; i++) {
                final int idx = 2*i;
                final int is = getSleepInInterval(sleepProbsWithDeltaProb[0],sleepProbsWithDeltaProb[1],sleepTransitionIndices.get(idx),sleepTransitionIndices.get(idx+1));
                final int iw = getWakeInInterval(myPillMagintude,sleepProbsWithDeltaProb[1],wakeTransitionIndices.get(idx),wakeTransitionIndices.get(idx + 1));
                LOGGER.info("action=deterine_wake_sleep_pair sleep={} wake={}",is,iw);
                segments.add(new IdxPair(is,iw));
            }

            if (segments.isEmpty()) {
                //this should never happen, given all the previous checks
                LOGGER.error("action=return_invalid_indices reason=no_valid_segments");
                return Optional.absent();
            }

            final MergeResult mergeResult = mergeCloseSegments(segments,MAX_TIME_AWAKE_TO_SPLIT_SEGMENTS);

            skippedOverWakePeriods.addAll(mergeResult.skippedOverWakePeriods);

            //find max duration
            IdxPair max = mergeResult.mergedSegments.get(0);

            for (int i = 1; i < mergeResult.mergedSegments.size(); i++) {
                if (mergeResult.mergedSegments.get(i).duration() > max.duration()) {
                    max = mergeResult.mergedSegments.get(i);
                }
            }

            iSleep = max.i1;
            iWake = max.i2;

            if (iSleep == -1 || iWake == -1) {
                LOGGER.info("action=return_invalid_indices reason=somehow_never_got_valid_indices");
                return Optional.absent();
            }

            if (iWake - iSleep < MIN_SLEEP_DURATION) {
                LOGGER.info("action=return_invalid_indices reason=sleep_duration_less_than_min");
                return Optional.absent();
            }

            iInBed = iSleep - DEFAULT_SPACING_OF_IN_BED_BEFORE_SLEEP;
            iOutOfBed = iWake + DEFAULT_SPACING_OF_OUT_OF_BED_AFTER_WAKE;
        }


        /*
         *  Step 2:  Figure out when you got into and out of bed based off of motion and sleep.
         *
         *  The general idea is that you got into bed before you went to sleep, and you got out of bed after you woke up.
         *  So for sleep, you go backwards from when we think you fell asleep, and find the beginning of the previous motion segment.
         *  For wake, you go forwards from wake, and find the end of the next motion segment.
         *
         *
         *  Motion data is sporadic, meaning that you'll get one minute with five seconds of motion, and then ten minutes later
         *  you'll get another minute with six seconds of motion, and then maybe two hours of no motion.  The idea is that these two minutes
         *  of motion, spaced ten minutes apart constitute a "segment" of motion.
         *
         *  Motion segments are determined by an HMM, again.
         *  State 0: No motion for long duration
         *  State 1: moderate amount of motion for short duration
         *  State 2: no motion for short duration.
         *
         *  States 1 and 2 are considered in a motion segment.
         *
         *  So for the above example, it'd be  [.... 0 0 0 0 1 2 2 2 2 2 2 2 2 2 2 1 0 0 0 0 0 ....]
         *
         */

        {
            final double[][] x = {myMotionDurations};
            final double[][] A = {
                    {0.999, 1e-3, 0.0},
                    {1e-5, 0.90, 0.10},
                    {0.0, 0.10, 0.90}
            };
            final double[] pi = {1.0,0.0,0.0};
            final HmmPdfInterface[] obsModels = {new PoissonPdf(POISSON_MEAN_FOR_NO_MOTION, 0), new PoissonPdf(POISSON_MEAN_FOR_MOTION, 0),new PoissonPdf(POISSON_MEAN_FOR_A_LITTLE_MOTION, 0)};

            final HiddenMarkovModelInterface hmm = HiddenMarkovModelFactory.create(HiddenMarkovModelFactory.HmmType.LOGMATH, 3, A, pi, obsModels, 0);


            final HmmDecodedResult result = hmm.decode(x, new Integer[]{0, 1,2}, MIN_HMM_PDF_EVAL);

            final List<Integer> motionInts = Lists.newArrayList();

            for (final double d : myMotionDurations) {
                motionInts.add(Integer.valueOf((int)d));
            }

            //LOGGER.debug("\nclusterpath={};\nmotion={};\n",result.bestPath,motionInts);

            boolean foundCluster = false;

            //go backwards from sleep and find beginning of next motion cluster encountered
            for (int i = iSleep; i >= 0; i--) {
                final Integer state = result.bestPath.get(i);

                if (!state.equals(0)) {
                    //if motion cluster start was found too far before sleep, then stop search and use default
                    if (iSleep - i > MAX_ON_BED_SEARCH_WINDOW && !foundCluster) {
                        LOGGER.warn("action=return_default_in_bed reason=motion_cluster_too_far_out");
                        break;
                    }

                    foundCluster = true;
                    continue;
                }

                if (state.equals(0) && foundCluster) {
                    iInBed = i;
                    break;
                }
            }

            foundCluster = false;
            for (int i = iWake; i < myMotionDurations.length; i++) {
                final Integer state = result.bestPath.get(i);

                if (!state.equals(0)) {
                    //if motion cluster start was found too far after wake, then stop search and use default
                    if (i - iWake > MAX_OFF_BED_SEARCH_WINDOW && !foundCluster) {
                        LOGGER.warn("action=return_default_out_of_bed reason=motion_cluster_too_far_out");
                        break;
                    }
                    foundCluster = true;
                }

                if (state.equals(0) && foundCluster) {
                    iOutOfBed = i - 1;
                    break;
                }
            }
        }

        //sanity checks, make sure event times are not the same
        final int outOfBedBounds = iWake + DEFAULT_SPACING_OF_OUT_OF_BED_AFTER_WAKE;

        if (iOutOfBed < outOfBedBounds) {
            LOGGER.info("action=moving_out_of_bed reason=default_bounds_violation change={}",outOfBedBounds - iOutOfBed );
            iOutOfBed = outOfBedBounds;
        }

        final int inBedBounds = iSleep - DEFAULT_SPACING_OF_IN_BED_BEFORE_SLEEP;

        if (iInBed > inBedBounds) {
            LOGGER.info("action=moving_in_bed reason=default_bounds_violation change={}",inBedBounds - iInBed );
            iInBed = inBedBounds;
        }

        LOGGER.info("timeline_event=IN_BED idx={} psleep={}",iInBed,sleep[iInBed]);
        LOGGER.info("timeline_event=SLEEP  idx={} psleep={}",iSleep,sleep[iSleep]);
        LOGGER.info("timeline_event=WAKE_UP idx={} psleep={}",iWake,sleep[iWake]);
        LOGGER.info("timeline_event=OUT_OF_BED idx={} psleep={}",iOutOfBed,sleep[iOutOfBed]);

        return Optional.of(new EventIndices(iInBed,iSleep,iWake,iOutOfBed,skippedOverWakePeriods));

    }



    static int getWakeInInterval(final double [] pillMagnitude,final double [] deltasleepprobs, final int begin, final int end) {
        double minScore = Double.POSITIVE_INFINITY;
        int minIdx = end;
        for (int i = begin; i <= end; i++ ) {
            final double score = deltasleepprobs[i] * pillMagnitude[i];
            if (score < minScore) {
                minScore = score;
                minIdx = i;
            }
        }

        return minIdx;
    }

    static int getSleepInInterval(final double [] sleepprobs, final double [] deltasleepprobs, final int begin, final int end) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int maxIdx = begin;

        for (int i = begin; i <= end; i++) {
            final double weight = sleepprobs[i] * (1.0 - sleepprobs[i]); // weight towards 0.5
            final double score = deltasleepprobs[i] * weight;

            if (score > maxScore) {
                maxScore = score;
                maxIdx = i;
            }

        }

        return maxIdx;
    }

}
