package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

public class Bonus extends Frame {

    public Bonus(Roll roll) {
        super(roll);
    }

    public Bonus(Roll firstRoll, Roll secondRoll) {
        super(firstRoll, secondRoll);
    }

    @Override
    public boolean isBonus() {
        return true;
    }
}
