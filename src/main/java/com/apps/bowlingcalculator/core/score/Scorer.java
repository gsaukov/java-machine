package com.apps.bowlingcalculator.core.score;

import com.apps.bowlingcalculator.core.frame.Frame;

import java.util.ArrayList;

import static com.apps.bowlingcalculator.core.score.ScorerUtils.addFinalBonus;
import static com.apps.bowlingcalculator.core.score.ScorerUtils.addSpareBonus;
import static com.apps.bowlingcalculator.core.score.ScorerUtils.addStrikeBonus;
import static com.apps.bowlingcalculator.core.score.ScorerUtils.previous;

public class Scorer {

    public void score(ArrayList<Frame> frames) {
        for (Frame frame : frames) {
            if(isFrame(frame)){
                frameScore(frame, frames);
            } else {
                finalBonusScore(frame, frames);
            }
        }
    }

    private void frameScore(Frame frame, ArrayList<Frame> frames) {
        frame.setScore(previous(frame, frames).getScore() + frame.getNumberOfPins());
        addStrikeSpareBonus(frame, frames);
    }

    private void addStrikeSpareBonus(Frame frame, ArrayList<Frame> frames) {
        if (frame.isSpare()) {
            addSpareBonus(frame, frames);
        }

        if (frame.isStrike()) {
            addStrikeBonus(frame, frames);
        }
    }

    private void finalBonusScore(Frame frame, ArrayList<Frame> frames) {
        frame.setScore(previous(frame, frames).getScore() + addFinalBonus(frame, frames));
    }

    private boolean isFrame(Frame frame){
        return !frame.isIncomplete() && !frame.isBonus();
    }
}
