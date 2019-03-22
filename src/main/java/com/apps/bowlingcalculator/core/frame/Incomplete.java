package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

public class Incomplete extends Frame {

    public Incomplete(Roll roll){
        super(roll);
    }

    public boolean isIncomplete() {
        return true;
    }

}
