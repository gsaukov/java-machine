package com.apps.t24app.controller;

import com.apps.t24app.core.data.Entry;
import com.apps.t24app.core.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {

    private EntryService entryService;
    private ModelHandler modelHandler;

    @GetMapping({"/", "/klt"})
    public String homePage(Model model) {
        modelHandler.enrichFrame(model, entryService.getEntries());
        return "home";
    }

    @PostMapping("/")
    public String newEntry(@RequestParam("name") String name, @RequestParam("email") String email,
                           @RequestParam("age") Integer age, Model model) {
        try{// cool error handling.
            Entry newEntry = new Entry.Builder()
                    .withName(name)
                    .withEmail(email)
                    .withAge(age)
                    .build();

            String status = entryService.processEntry(newEntry);
            modelHandler.enrichFrame(model, entryService.getEntries());
            modelHandler.enrichStatus(model, status);
        } catch (Exception e){
            modelHandler.enrichError(model, "something went wrong");
        }
        return "home";
    }

    @GetMapping("/klt/rest/age/{age}")
    public @ResponseBody String validateAge(@PathVariable("age") String age) {
        try{// cool error handling.
            return entryService.processAge(Integer.valueOf(age));
        } catch (Exception e){
            return "something went wrong";
        }

    }

    @Autowired
    public void setEntryService(EntryService entryService) {
        this.entryService = entryService;
    }

    @Autowired
    public void setModelHandler(ModelHandler modelHandler) {
        this.modelHandler = modelHandler;
    }

}
