package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

public class Strike extends Frame {

    public Strike(Roll roll) {
        super(roll);
    }

    @Override
    public boolean isStrike() {
        return true;
    }
}
