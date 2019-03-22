package com.apps.bowlingcalculator.core.roll;

public class Roll {
    private int numberOfPins;

    public Roll(int numberOfPins) {
        this.numberOfPins = numberOfPins;
    }

    public int getNumberOfPins() {
        return numberOfPins;
    }

    public static Roll miss() {
        return new Roll(0);
    }
}
