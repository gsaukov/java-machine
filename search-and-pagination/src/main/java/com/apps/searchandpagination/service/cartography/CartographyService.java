package com.apps.searchandpagination.service.cartography;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public class CartographyService {

    @Value("${cartographer.origin-map-cartographer-url}")
    private String originMapCartogrpherUrl;

    public String getOriginCartography() throws Exception {
//        Document doc = Jsoup.parse(new File("C:\\dev\\machine\\core\\webmap\\test7.html"), "UTF-8");
        Document doc = Jsoup.parse(new URL(originMapCartogrpherUrl), 3000);
        Element map = doc.select("body div").first();
        Element scripts = doc.select("body script").first();
        Element el = new Element(Tag.valueOf("div"), "");
        el.appendChild(map);
        el.appendChild(scripts);
        return el.toString();
    }
}
