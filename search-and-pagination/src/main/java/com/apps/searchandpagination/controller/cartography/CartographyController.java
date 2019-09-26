package com.apps.searchandpagination.controller.cartography;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartographyController {

    @GetMapping("cartography/map")
    public String getCartographyMap() {
        return "cartography/map";
    }
}
