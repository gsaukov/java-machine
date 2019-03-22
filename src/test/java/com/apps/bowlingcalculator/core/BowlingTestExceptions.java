package com.apps.bowlingcalculator.core;

import com.apps.bowlingcalculator.core.exception.BowlingRulesViolationException;
import org.testng.annotations.Test;

import static com.apps.bowlingcalculator.core.BowlingTestSimple.scoreOfArray;

public class BowlingTestExceptions {

    @Test(expectedExceptions = BowlingRulesViolationException.class)
    public void invalid_roll() {
        scoreOfArray("D-------------------");
    }

    @Test(expectedExceptions = BowlingRulesViolationException.class)
    public void invalid_roll_spare() {
        scoreOfArray("/------------------");
    }

    @Test(expectedExceptions = BowlingRulesViolationException.class)
    public void invalid_frame_total_value() {
        scoreOfArray("------77------------");
    }

}
