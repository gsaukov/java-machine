package com.apps.searchandpagination.service.cartography;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class CartographyService {

    @Value("${cartographer.origin-map-cartographer-url}")
    private String originMapCartographerUrl;

    @Value("${cartographer.value-map-cartographer-url}")
    private String valueMapCartographerUrl;

    @Value("${cartographer.pollution-map-cartographer-url}")
    private String pollutionMapCartographerUrl;

    public String getOriginCartography() throws Exception {
        return getCartography(originMapCartographerUrl);
    }

    public String getValueCartography() throws Exception {
        return getCartography(valueMapCartographerUrl);
    }

    public String getPollutionCartography() throws Exception {
        return getCartography(pollutionMapCartographerUrl);
    }

    private String getCartography(String url) throws Exception {
        Document doc = Jsoup.parse(new URL(url), 3000);
        Element map = doc.select("body div").first();
        Element scripts = doc.select("body script").first();
        scripts = doc.select("body script").first();
        scripts.prepend(" function generateCartographyBlock() { ");
        scripts.append(" } generateCartographyBlock();");
        Element el = new Element(Tag.valueOf("div"), "");
        el.appendChild(map);
        el.appendChild(scripts);
        return el.toString();
    }
}
