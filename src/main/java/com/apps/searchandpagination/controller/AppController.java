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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AppController {

    private DataService service;


    @GetMapping({"/"})
    public String home(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<DataObject> dataPage = service.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("dataPage", dataPage);

        int totalPages = dataPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "datatable";
    }

    @Autowired
    public void setService(DataService service) {
        this.service = service;
    }
}
