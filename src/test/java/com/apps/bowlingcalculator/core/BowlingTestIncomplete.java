package com.apps.bowlingcalculator.core;

import org.testng.annotations.Test;

import static com.apps.bowlingcalculator.core.BowlingTestSimple.getLastComplete;
import static com.apps.bowlingcalculator.core.BowlingTestSimple.scoreOfArray;

import static org.testng.AssertJUnit.assertEquals;

public class BowlingTestIncomplete {

    @Test
    public void scores_small() {
        assertEquals(120, scoreOfArray("XXXXX"));
    }

    @Test
    public void not_be_a_spare_the_small() {
        assertEquals(16, scoreOfArray("3553"));
    }

    @Test
    public void small_scores_28() {
        assertEquals(28, scoreOfArray("5/5/3"));
    }

    @Test
    public void split_game_20_28() {
        BowlingCalculator bowling = new BowlingCalculator();
        assertEquals(20, getLastComplete(bowling.calculate("------------------XX")).getScore());
        assertEquals(28, getLastComplete(bowling.calculate("8")).getScore());
    }

    @Test
    public void bonus_split_game_28_47() {
        BowlingCalculator bowling = new BowlingCalculator();
        assertEquals(28, getLastComplete(bowling.calculate("5/5/3")).getScore());
        assertEquals(47, getLastComplete(bowling.calculate("-------------X33")).getScore());
    }


}
