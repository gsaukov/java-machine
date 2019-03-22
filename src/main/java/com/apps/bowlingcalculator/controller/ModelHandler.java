package com.apps.bowlingcalculator.controller;

import com.apps.bowlingcalculator.core.exception.BowlingRulesViolationException;
import com.apps.bowlingcalculator.core.frame.Frame;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Service
public class ModelHandler {

    public void enrichModel(Model model, ArrayList<Frame> frames){
        initializeEmptyModel(model);
        intiializeDataModel(model, frames);
    }

    public void enrichError(Model model, BowlingRulesViolationException e){
        initializeEmptyModel(model);
        model.addAttribute("bowlingError", e.getMessage());
    }

    public void initializeEmptyModel(Model model){
        for(int i = 1; i <= 10; i++){
            model.addAttribute("frame" + i + "1", " ");
            model.addAttribute("frame" + i + "2", " ");
            model.addAttribute("score" + i, "   ");
        }
        model.addAttribute("frame103", " ");
    }

    private void intiializeDataModel(Model model, ArrayList<Frame> frames) {
        for (int i = 1; i <= frames.size(); i++) {
            Frame frame = frames.get(i-1);
            if(frame.isDummy()){
                continue;
            } else if (frame.isIncomplete()) {
                model.addAttribute("frame" + i + "1", frame.getNumberOfPinsFirstRoll());
            } else if (frame.isBonus()) {
                if(frames.get(i-2).isStrike()){
                    model.addAttribute("frame102", valueOrStrike(frame.getNumberOfPinsFirstRoll()));
                    model.addAttribute("frame103", valueOrStrike(frame.getNumberOfPinsSecondRoll()));
                } else {
                    model.addAttribute("frame103", valueOrStrike(frame.getNumberOfPinsFirstRoll()));
                }
                model.addAttribute("score10", inlineSpace(frame.getScore()));
            } else if (frame.isStrike()) {
                model.addAttribute("frame" + i + "1", "X");
                model.addAttribute("score" + i, inlineSpace(frame.getScore()));
            } else if (frame.isSpare()) {
                model.addAttribute("frame" + i + "1", frame.getNumberOfPinsFirstRoll());
                model.addAttribute("frame" + i + "2", "/");
                model.addAttribute("score" + i, inlineSpace(frame.getScore()));
            } else {
                model.addAttribute("frame" + i + "1", frame.getNumberOfPinsFirstRoll());
                model.addAttribute("frame" + i + "2", frame.getNumberOfPinsSecondRoll());
                model.addAttribute("score" + i, inlineSpace(frame.getScore()));
            }
        }
    }

    private String inlineSpace(int value) {
        String s = String.valueOf(value);
        while (3 - s.length() > 0) {
            s += " ";
        }
        return s;
    }

    private String valueOrStrike(int value) {
        if(value == 10){
            return "X";
        } else {
            return String.valueOf(value);
        }
    }
}
