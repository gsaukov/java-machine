package com.apps.searchandpagination.controller;

import com.apps.searchandpagination.service.DataObject;
import com.apps.searchandpagination.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AppController {

    private DataService service;


    @GetMapping({"/"})
    public String home(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<DataObject> dataPage = service.findPaginated(PageRequest.of(currentPage, pageSize));

        PageWrapper<DataObject> page = new PageWrapper<DataObject>(dataPage, "getpage");
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "home";
    }

    @GetMapping({"getpage/"})
    public String getPage(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<DataObject> dataPage = service.findPaginated(PageRequest.of(currentPage, pageSize));

        PageWrapper<DataObject> page = new PageWrapper<DataObject>(dataPage, "getpage");
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "datatable :: datatable";
    }

    @Autowired
    public void setService(DataService service) {
        this.service = service;
    }
}
