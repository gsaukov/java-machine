package com.apps.bowlingcalculator.core.frame;

import com.apps.bowlingcalculator.core.roll.Roll;

public class Frame {
    private Roll firstRoll;
    private Roll secondRoll;
    private int score = 0;

    protected Frame(Roll roll) {
        this(roll, Roll.miss());
    }

    public Frame(Roll firstRoll, Roll secondRoll) {
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
    }

    public int getNumberOfPins() {
        return firstRoll.getNumberOfPins() + secondRoll.getNumberOfPins();
    }

    public int getNumberOfPinsFirstRoll() {
        return firstRoll.getNumberOfPins();
    }

    public int getNumberOfPinsSecondRoll() {
        return secondRoll.getNumberOfPins();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isStrike() {
        return false;
    }

    public boolean isSpare() {
        return false;
    }

    public boolean isBonus() {
        return false;
    }

    public boolean isIncomplete() {
        return false;
    }

    public boolean isDummy() {
        return false;
    }

}
