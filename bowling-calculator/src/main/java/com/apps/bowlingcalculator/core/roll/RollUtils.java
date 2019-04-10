package com.apps.bowlingcalculator.core.roll;

import com.apps.bowlingcalculator.core.frame.Bonus;
import com.apps.bowlingcalculator.core.frame.Frame;
import com.apps.bowlingcalculator.core.frame.FrameUtils;
import com.apps.bowlingcalculator.core.frame.Incomplete;
import com.apps.bowlingcalculator.core.frame.Spare;
import com.apps.bowlingcalculator.core.frame.Strike;

import java.util.ArrayList;

public class RollUtils {

    public static Frame firstRollFrame(int rollNum, ArrayList<Roll> rolls) {
        Roll firstRoll = rolls.get(rollNum);
        if (firstRoll.getNumberOfPins() == 10) {
            return new Strike(firstRoll);
        }
        return new Incomplete(firstRoll);
    }

    public static Frame secondRollFrame(int rollNum, ArrayList<Roll> rolls) {
        Roll firstRoll = rolls.get(rollNum - 1);
        Roll secondRoll = rolls.get(rollNum);
        if (firstRoll.getNumberOfPins() + secondRoll.getNumberOfPins() == 10) {
            return new Spare(firstRoll, secondRoll);
        }
        return new Frame(firstRoll, secondRoll);
    }

    public static Frame bonusFrameRoll(int rollNum, ArrayList<Frame> frames, ArrayList<Roll> rolls) {
        if (FrameUtils.last(frames).isBonus()) {
            return new Bonus(rolls.get(rollNum - 1), rolls.get(rollNum));
        } else {
            return new Bonus(rolls.get(rollNum));
        }
    }

}
