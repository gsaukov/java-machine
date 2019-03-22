package com.apps.bowlingcalculator.core.roll;

import com.apps.bowlingcalculator.core.exception.BowlingRulesViolationException;

import java.util.ArrayList;

public class RollParser {

    public ArrayList<Roll> parse(String roll, ArrayList<Roll> rolls) {
        rolls.add(createRoll(roll, rolls));
        return rolls;
    }

    private Roll createRoll(String roll, ArrayList<Roll> rolls) {
        return new Roll(scoreOf(roll, rolls));
    }

    private int scoreOf(String roll, ArrayList<Roll> rolls) {
        try{
            if ("-".equals(roll)) {
                return 0;
            }
            if ("/".equals(roll)) {
                return 10 - rolls.get(rolls.size() - 1).getNumberOfPins();
            }
            if ("X".equals(roll)) {
                return 10;
            }
            return Integer.valueOf(roll);
        } catch (Exception e){
            throw new BowlingRulesViolationException("You are using unsupported characters or violating Bowling rules. Check your input: " + roll);
        }
    }
}
