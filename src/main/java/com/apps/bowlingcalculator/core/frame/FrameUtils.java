package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.exception.BowlingRulesViolationException;

import java.util.ArrayList;

import static com.apps.bowlingcalculator.core.BowlingCalculator.MAX_FRAMES;

public class FrameUtils {

    public static Frame last(ArrayList<Frame> frames){
        return frames.get(frames.size() - 1);
    }

    public static void removeEnd(ArrayList<Frame> frames){
        frames.remove(frames.size() - 1);
    }

    public static void addBonus(Frame frame, ArrayList<Frame> frames) {
        if (last(frames).isBonus()) {
            removeEnd(frames);
            frames.add(frame);
            close(frames);
        } else {
            frames.add(frame);
            tryCloseBonus(frames);
        }
    }

    public static void addCompleteFrame(Frame frame, ArrayList<Frame> frames) {
        validateFrame(frame);
        removeEnd(frames);
        frames.add(frame);
        tryCloseFrame(frames);
    }

    public static void tryCloseBonus(ArrayList<Frame> frames) {
        if (frames.get(MAX_FRAMES).isSpare()) {
            close(frames);
        }
    }

    public static void tryCloseFrame(ArrayList<Frame> frames) {
        if (frames.size() == MAX_FRAMES && !(last(frames).isStrike() || last(frames).isSpare())) {
            close(frames);
        }
    }

    public static void close(ArrayList<Frame> frames) {
        frames.add(Dummy.DUMMY);
    }

    public static void validateFrame(Frame frame) {
        if(frame.getNumberOfPins() > 10){
            throw new BowlingRulesViolationException("Frame can be less or equal to 10. Your frame input: "
                    + frame.getNumberOfPinsFirstRoll() + ", " + frame.getNumberOfPinsSecondRoll());
        }
    }
}
