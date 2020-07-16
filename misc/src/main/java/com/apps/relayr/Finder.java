package com.apps.relayr;

import java.util.*;

/**
 * Simple class to extract matching strings from an array regardless of the characters order.
 * Case sensitive, supports letters, numbers, special characters and empty strings. Accepts duplicates.
 */
public class Finder {

    private final Map<String, List<String>> map = new HashMap<>();

    public Finder(String[] arr) {
        populateMap(arr);
    }

    public List<String> find(String s) {
        String k = sortString(s);
        if (map.containsKey(k)) {
            return map.get(k);
        } else {
            return new ArrayList<>();
        }
    }

    private void populateMap(String[] arr) {
        for (String e : arr) {
            String k = sortString(e);
            if (map.containsKey(k)) {
                map.get(k).add(e);
            } else {
                List<String> list = new ArrayList<>();
                list.add(e);
                map.put(k, list);
            }
        }
    }

    private String sortString(String s) {
        char[] arr = s.toCharArray();
        Arrays.sort(arr);
        return String.valueOf(arr);
    }
}
