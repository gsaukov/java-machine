package com.apps.t24app.controller;

import com.apps.t24app.core.data.Entry;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ModelHandler {

    String line = "    =================================================================================================" +
            "=================================================================================================\n";

    public void enrichError(Model model, String error){
        model.addAttribute("frame", error);
    }

    public void enrichFrame(Model model, List<Entry> entries) {
        if(entries.isEmpty()){
            model.addAttribute("frame", "    No one applied so far. Be first!");
            return;
        }

        String frame = line;
        for(Entry entry: entries){
            frame += "    | " + entry.getId() +
                    " | " + inlineSpace(entry.getName(), 89) +
                    " | " + inlineSpace(entry.getEmail(), 39) +
                    " | " + inlineSpace(entry.getAge().toString(), 4) +
                    " | " + inlineSpace(entry.getStatus(), 9) +
                    " |\n";
        }
        frame += line;
        model.addAttribute("frame", frame);
    }

    public void enrichStatus(Model model, String status) {
        model.addAttribute("status", "Thank you for your application, status: " + status);
    }

    private String inlineSpace(String s, int size) {
        while (size - s.length() > 0) {
            s += " ";
        }
        return s;
    }

}
