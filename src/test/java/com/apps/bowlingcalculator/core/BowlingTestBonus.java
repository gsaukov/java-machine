package com.apps.bowlingcalculator.core;

import org.testng.annotations.Test;

import static com.apps.bowlingcalculator.core.BowlingTestSimple.scoreOfArray;
import static org.testng.AssertJUnit.assertEquals;

public class BowlingTestBonus {

    @Test
    public void bonus_300_a_perfect_game() {
        assertEquals(300, scoreOfArray("XXXXXXXXXXXX"));
    }

    @Test
    public void bonus_150_all_spare() {
        assertEquals(150, scoreOfArray("5/5/5/5/5/5/5/5/5/5/5"));
    }

    @Test
    public void bonus_293() {
        assertEquals(293, scoreOfArray("XXXXXXXXXXX3"));
    }

    @Test
    public void bonus_13_spare() {
        assertEquals(13, scoreOfArray("------------------3/3"));
    }

    @Test
    public void bonus_20_spare_strike() {
        assertEquals(20, scoreOfArray("------------------3/X"));
    }

    @Test
    public void bonus_16_strike_frame() {
        assertEquals(16, scoreOfArray("------------------X33"));
    }

    @Test
    public void bonus_19_spare_frame() {
        assertEquals(19, scoreOfArray("------------------2/9"));
    }

    @Test
    public void bonus_23_strike_roll_strike() {
        assertEquals(23, scoreOfArray("------------------X3X"));
    }

    @Test
    public void bonus_23_strike_strike_roll() {
        assertEquals(23, scoreOfArray("------------------XX3"));
    }

    @Test
    public void bonus_53_strike_strike_strike_roll() {
        assertEquals(53, scoreOfArray("----------------XXX3"));
    }

    @Test
    public void bonus_110_spares_known_issue() {
        assertEquals(110, scoreOfArray("1////////////////////"));
    }

}
