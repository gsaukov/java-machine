package com.apps.searchandpagination.controller.cartography;

import com.apps.searchandpagination.service.cartography.CartographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CartographyController {

    @Autowired
    private CartographyService cartographyService;

    @ResponseBody
    @GetMapping("cartography/originmap")
    public String getOriginCartographyMap() throws Exception{
        return cartographyService.getOriginCartography();
    }

    @ResponseBody
    @GetMapping("cartography/valuemap")
    public String getValueCartographyMap() throws Exception{
        return cartographyService.getValueCartography();
    }

    @ResponseBody
    @GetMapping("cartography/pollutionmap")
    public String getPollutionCartographyMap() throws Exception{
        return cartographyService.getPollutionCartography();
    }
}
