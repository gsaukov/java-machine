package com.apps.hackerrank.testdome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccessLog {

    public static final String LOG_CONTENT = "188.191.254.20,[22/Mar/2009:07:00:32 +0100],GET / HTTP/1.0,200\n"
            + "166.249.66.231,[22/Mar/2009:07:06:20 +0100],GET / HTTP/1.1,200\n"
            + "166.249.66.231,[22/Mar/2009:07:11:20 +0100],GET / HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET / HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /style.css HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/ht1.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/ht2.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/en.jpg HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/bt_qui.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/bt_references.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/bt_contacts.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/ht3.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/vide.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/ht4.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/contact.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/puce.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/h_facades.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:06 +0100],GET /images/f_blanc.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:07 +0100],GET /images/bt_mail.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:07 +0100],GET /images/v_home_1.gif HTTP/1.1,200\n"
            + "183.198.25.175,[22/Mar/2009:07:40:07 +0100],GET /favicon.ico HTTP/1.1,404\n"
            + "188.191.254.20,[22/Mar/2009:07:50:33 +0100],GET / HTTP/1.0,200\n"
            + "188.191.254.20,[22/Mar/2009:08:40:32 +0100],GET / HTTP/1.0,200\n"
            + "185.114.141.210,[22/Mar/2009:08:54:11 +0100],GET / HTTP/1.1,200\n"
            + "188.191.254.20,[22/Mar/2009:09:30:31 +0100],GET / HTTP/1.0,200\n"
            + "185.214.36.20,[22/Mar/2009:09:30:51 +0100],GET / HTTP/1.1,200\n"
            + "182.249.2.69,[22/Mar/2009:09:32:23 +0100],GET / HTTP/1.1,200\n"
            + "29.120.218.236,[22/Mar/2009:09:37:19 +0100],HEAD / HTTP/1.1,200\n"
            + "29.120.218.236,[22/Mar/2009:09:37:19 +0100],GET / HTTP/1.1,200\n"
            + "29.120.218.236,[22/Mar/2009:09:37:19 +0100],GET / HTTP/1.1,200";

    public static class IPCount {
        public final String ipAddress;
        public final int count;

        public IPCount(String ipAddress, int count) {
            this.ipAddress = ipAddress;
            this.count = count;
        }
    }

    public static IPCount findMostFrequentIP(BufferedReader reader) {
        List<String> lines = reader.lines().collect(Collectors.toList());
        if (lines.isEmpty()) {
            return null;
        }
        Map<String, Integer> map = parseLines(lines);
        return findMostFrequentIP(map);
    }

    private static Map<String, Integer> parseLines(List<String> lines) {
        Map<String, Integer> map = new HashMap<>();
        for (String line : lines) {
            String ip = line.split(",")[0];
            if (map.containsKey(ip)) {
                map.put(ip, map.get(ip) + 1);
            } else {
                map.put(ip, 1);
            }
        }
        return map;
    }

    private static IPCount findMostFrequentIP(Map<String, Integer> map) {
        String key = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
        return new IPCount(key, map.get(key));
    }

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new StringReader(LOG_CONTENT));
        IPCount ipcount = findMostFrequentIP(reader);
        System.out.println("IP address: " + ipcount.ipAddress);
        System.out.println("Occurrence: " + ipcount.count);
    }
}
