package com.apps.bowlingcalculator.core;

import com.apps.bowlingcalculator.core.frame.Frame;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static com.apps.bowlingcalculator.core.frame.Dummy.DUMMY;
import static org.testng.AssertJUnit.assertEquals;

public class BowlingTestSimple {

    @Test
    public void simple_score_0() {
        assertEquals(0, scoreOfArray("--------------------"));
    }

    @Test
    public void simple_score_1() {
        assertEquals(1, scoreOfArray("1-------------------"));
    }

    @Test
    public void simple_score_2() {
        assertEquals(2, scoreOfArray("2-------------------"));
    }

    @Test
    public void score_20_when_all_rolls_throw_one_pin() {
        assertEquals(20, scoreOfArray("11111111111111111111"));
    }

    @Test
    public void score_10_when_the_first_turn_do_a_spare_and_miss_other_rolls() {
        assertEquals(10, scoreOfArray("3/------------------"));
    }

    @Test
    public void score_10_when_the_first_turn_do_a_strike_and_miss_other_rolls() {
        assertEquals(10, scoreOfArray("X------------------"));
    }

    @Test
    public void get_next_throw_of_bonus_after_spare() {
        assertEquals(16, scoreOfArray("3/3-----------------"));
    }

    @Test
    public void get_next_throw_of_bonus_after_strike() {
        assertEquals(19, scoreOfArray("3-X3----------------"));
    }

    @Test
    public void not_be_a_spare_the_score_of_10_in_two_consecutive_rolls_but_in_different_turns() {
        assertEquals(16, scoreOfArray("3553----------------"));
    }

    @Test
    public void be_next_two_rolls_of_bonus_after_strike() {
        assertEquals(26, scoreOfArray("X53----------------"));
    }

    @Test
    public void game_over_60() {
        assertEquals(60, scoreOfArray("3333333333333333333333333333333333333333333333"));
    }

    @Test
    public void game_over_bonus_300() {
        assertEquals(300, scoreOfArray("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"));
    }

    public static int scoreOfArray(String rolls) {
        BowlingCalculator bowling = new BowlingCalculator();
        ArrayList<Frame> frames = bowling.calculate(rolls);
        return getLastComplete(frames).getScore();
    }

    public static ArrayList<Frame> getResultArray(String rolls) {
        BowlingCalculator bowling = new BowlingCalculator();
        return bowling.calculate(rolls);
    }

    public static Frame getLastComplete(ArrayList<Frame> frames){
        for (int i = frames.size() - 1; i >= 0; i--) {
            if(!frames.get(i).isIncomplete() && !frames.get(i).isDummy()){
                return frames.get(i);
            }
        }
        return DUMMY;
    }

}
