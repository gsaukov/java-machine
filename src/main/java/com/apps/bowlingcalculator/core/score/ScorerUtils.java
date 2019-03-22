package com.apps.bowlingcalculator.core.score;

import com.apps.bowlingcalculator.core.frame.Frame;

import java.util.ArrayList;

import static com.apps.bowlingcalculator.core.frame.Dummy.DUMMY;

public class ScorerUtils {

    public static void addSpareBonus(Frame frame, ArrayList<Frame> frames) {
        Frame nextFrame = next(frame, frames);
        frame.setScore(frame.getScore() + nextFrame.getNumberOfPinsFirstRoll());
    }

    public static void addStrikeBonus(Frame frame, ArrayList<Frame> frames) {
        Frame nextFrame = next(frame, frames);
        frame.setScore(frame.getScore() + nextFrame.getNumberOfPins());
        if (nextFrame.isStrike()) {
            Frame nextNextFrame = next(nextFrame, frames);
            frame.setScore(frame.getScore() + nextNextFrame.getNumberOfPins());
        }
    }

    public static int addFinalBonus(Frame frame, ArrayList<Frame> frames) {
        Frame previousFrame = previous(frame, frames);
        Frame previousPreviousFrame = previous(previousFrame, frames);
        if (previousFrame.isSpare() || !previousPreviousFrame.isStrike()) {//Already applied
            return frame.getNumberOfPins();
        } else { // previous is be Strike
            if (frame.getNumberOfPinsFirstRoll() == 10) {
                return frame.getNumberOfPins() + previousFrame.getNumberOfPins();
            } else {
                return frame.getNumberOfPins();
            }
        }
    }

    public static Frame next(Frame frame, ArrayList<Frame> frames) {
        int frameNum = frames.indexOf(frame);
        if (frameNum < frames.size() - 1) {
            Frame next = frames.get(frameNum + 1);
            if (!next.isBonus()) {
                return next;
            }
        }
        return DUMMY;
    }

    public static Frame previous(Frame frame, ArrayList<Frame> frames) {
        int frameNum = frames.indexOf(frame);
        if (frameNum > 0) {
            return (frames.get(frameNum - 1));
        }
        return DUMMY;
    }
}
