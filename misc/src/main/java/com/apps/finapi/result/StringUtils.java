package com.apps.finapi.result;

import java.util.*;

public class StringUtils {

    /**
     * @param col the Collection of strings
     * @return List of strings according to the following criteria:
     * <p>
     * - the returned list contains only those strings from the given collection that are non-blank (NOT null and NOT whitespace-only)
     * - all strings that are contained in the returned list are trimmed (leading/trailing whitespace is removed)
     * - all strings that are contained in the returned list are converted to upper-case
     * - the returned list contains no duplicates
     * - the returned list has the strings ordered by their length (ascending; strings of the same length may be randomly ordered)
     * <p>
     * NOTE: Big O - nlog(n)
     */
    public static List<String> stringsModifier(Collection<String> col){
        Set<String> set = new HashSet<>();
        for(String s : col) {
            if(isValid(s)){
                set.add(s.trim().toUpperCase());
            }
        }
        String[] arr = set.toArray(new String[set.size()]);
        Arrays.sort(arr, new StringSizeComparator());
        return Arrays.asList(arr);
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
