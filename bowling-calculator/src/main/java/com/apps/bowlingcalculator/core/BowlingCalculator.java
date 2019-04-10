package com.apps.bowlingcalculator.core;

import com.apps.bowlingcalculator.core.frame.Frame;
import com.apps.bowlingcalculator.core.frame.FrameParser;
import com.apps.bowlingcalculator.core.roll.Roll;
import com.apps.bowlingcalculator.core.roll.RollParser;
import com.apps.bowlingcalculator.core.score.Scorer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BowlingCalculator {
    public static final int MAX_FRAMES = 10;

    private final RollParser rollParser;
    private final FrameParser frameParser;
    private final Scorer scorer;

    private final ArrayList<Roll> rolls;

    public BowlingCalculator() {
        this.rollParser = new RollParser();
        this.frameParser = new FrameParser();
        this.scorer = new Scorer();
        this.rolls = new ArrayList<>();
    }

    public ArrayList<Frame> calculate(String array) {
        for (char roll : array.toCharArray()){
            rollParser.parse(String.valueOf(roll), rolls);
        }
        ArrayList<Frame> frames = frameParser.parse(rolls);
        scorer.score(frames);
        return frames;
    }

    public void reset(){
        rolls.clear();
    }

}
