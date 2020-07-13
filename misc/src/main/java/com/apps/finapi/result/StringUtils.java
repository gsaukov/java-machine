package com.apps.finapi.result;

import java.util.*;

/**
 * TODO:
 * Write a utility class with a method that you can pass a Collection of strings, and which returns a new List of
 * strings according to the following criteria:
 * <p>
 * - the returned list contains only those strings from the given collection that are non-blank (NOT null and NOT whitespace-only)
 * - all strings that are contained in the returned list are trimmed (leading/trailing whitespace is removed)
 * - all strings that are contained in the returned list are converted to upper-case
 * - the returned list contains no duplicates
 * - the returned list has the strings ordered by their length (ascending; strings of the same length may be randomly ordered)
 * <p>
 * NOTE: This method is intended to be used with huge collections, so performance is an issue.
 */
public class StringUtils {

    public static List<String> stringsModifier(List<String> list){
        Set<String> tempSet = new HashSet<>();
        for(String s : list) {
            if(isValid(s)){
                tempSet.add(s.trim().toUpperCase());
            }
        }
        String[] tempArray = tempSet.toArray(new String[tempSet.size()]);
        Arrays.sort(tempArray, new StringSizeComparator());
        return Arrays.asList(tempArray);
    }

    private static boolean isValid(String s){
        return s != null && !s.trim().isEmpty();
    }

    private static class StringSizeComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            return Integer.compare(o1.length(), o2.length());
        }
    }

}
