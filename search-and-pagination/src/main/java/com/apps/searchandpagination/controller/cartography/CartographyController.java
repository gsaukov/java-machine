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
    public String getCartographyMap() throws Exception{
        return cartographyService.getOriginCartography();
    }
}
