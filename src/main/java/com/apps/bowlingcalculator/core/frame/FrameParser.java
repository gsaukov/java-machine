package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

import java.util.ArrayList;

import static com.apps.bowlingcalculator.core.BowlingCalculator.MAX_FRAMES;
import static com.apps.bowlingcalculator.core.frame.FrameUtils.*;
import static com.apps.bowlingcalculator.core.frame.FrameUtils.addBonus;
import static com.apps.bowlingcalculator.core.frame.FrameUtils.addCompleteFrame;
import static com.apps.bowlingcalculator.core.frame.FrameUtils.last;
import static com.apps.bowlingcalculator.core.roll.RollUtils.*;
import static com.apps.bowlingcalculator.core.roll.RollUtils.bonusFrameRoll;
import static com.apps.bowlingcalculator.core.roll.RollUtils.firstRollFrame;
import static com.apps.bowlingcalculator.core.roll.RollUtils.secondRollFrame;

public class FrameParser {

    public ArrayList<Frame> parse(ArrayList<Roll> rolls) {
        ArrayList<Frame> frames = new ArrayList<>();
        for (int i = 0; i < rolls.size(); i++) {
            if (isOver(frames)) {
                break;
            }
            addFrame(createFrame(i, frames, rolls), frames);
        }
        return frames;
    }

    private Frame createFrame(int rollNum, ArrayList<Frame> frames, ArrayList<Roll> rolls) {
        if (isNewFrame(frames)) {
            return firstRollFrame(rollNum, rolls);
        } else if (isBonus(frames)) {
            return bonusFrameRoll(rollNum, frames, rolls);
        } else {
            return secondRollFrame(rollNum, rolls);
        }
    }

    private void addFrame(Frame frame, ArrayList<Frame> frames) {
        if ((frame.isIncomplete() || frame.isStrike())) {
            frames.add(frame);
        } else if (frame.isBonus()) {
            addBonus(frame, frames);
        } else {
            addCompleteFrame(frame, frames);
        }
    }

    private boolean isOver(ArrayList<Frame> frames) {
        return frames.size() > 0 && last(frames).isDummy();
    }

    private boolean isNewFrame(ArrayList<Frame> frames) {
        return !isBonus(frames) && (frames.isEmpty() || !last(frames).isIncomplete());
    }

    private boolean isBonus(ArrayList<Frame> frames) {
        return !frames.isEmpty() && !last(frames).isIncomplete() && MAX_FRAMES <= frames.size();
    }

}
