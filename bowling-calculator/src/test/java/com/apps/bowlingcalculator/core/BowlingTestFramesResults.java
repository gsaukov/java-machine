package com.apps.bowlingcalculator.core;

import com.apps.bowlingcalculator.core.frame.Frame;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static com.apps.bowlingcalculator.core.BowlingTestSimple.getResultArray;
import static org.testng.AssertJUnit.assertEquals;

public class BowlingTestFramesResults {

    @Test
    public void test_result_frames() {
        ArrayList<Frame> frames = getResultArray("5/3/X14--223-5/5/X3/");
        assertEquals(13, frames.get(0).getScore());
        assertEquals(true, frames.get(0).isSpare());
        assertEquals(33, frames.get(1).getScore());
        assertEquals(48, frames.get(2).getScore());
        assertEquals(true, frames.get(2).isStrike());
        assertEquals(53, frames.get(3).getScore());
        assertEquals(53, frames.get(4).getScore());
        assertEquals(57, frames.get(5).getScore());
        assertEquals(60, frames.get(6).getScore());
        assertEquals(75, frames.get(7).getScore());
        assertEquals(95, frames.get(8).getScore());
        assertEquals(115, frames.get(10).getScore());
        assertEquals(true, frames.get(10).isBonus());
        assertEquals(true, frames.get(11).isDummy());
    }

    @Test
    public void test_incomplete_frames() {
        ArrayList<Frame> frames = getResultArray("X33X145");
        assertEquals(47, frames.get(4).getScore());
        assertEquals(true, frames.get(4).isIncomplete());
    }

}
