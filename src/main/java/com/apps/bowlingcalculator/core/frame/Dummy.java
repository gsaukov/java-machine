package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

public class Dummy extends Frame {

    public static final Dummy DUMMY = new Dummy();

    private Dummy() {
        super(Roll.miss(), Roll.miss());
    }

    public int getScore() {
        return 0;
    }

    @Override
    public boolean isDummy() {
        return true;
    }
}
