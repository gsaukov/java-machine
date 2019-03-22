package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

public class Spare extends Frame {
    public Spare(Roll firstRoll, Roll secondRoll) {
        super(firstRoll, secondRoll);
    }

    @Override
    public boolean isSpare() {
        return true;
    }
}
