package com.apps.leetcode;

import java.util.*;

//https://leetcode.com/problems/similar-string-groups/
public class SimilarStringGroups {

    public static void main(String[] args) {
        String[] A = {"omv", "ovm"};
        String[] A1 = {"blw", "bwl", "wlb"};
        String[] A2 = {"tars","rats","arts","star"};
        SimilarStringGroups s = new SimilarStringGroups();
        s.numSimilarGroups(A2);
    }

    private Map<String, Set<String>> seen;

    public int numSimilarGroups(String[] strs) {
        seen = new HashMap();
        int count = 0;
        for (int i = 0; i < strs.length; i++) {
            char[] template = strs[i].toCharArray();
            for (int j = i + 1; j < strs.length; j++) {
                char[] match = strs[j].toCharArray();
                if (doesMatchOrNotAlreadySeen(template, match)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean doesMatchOrNotAlreadySeen(char[] template, char[] match) {
        if (template.length != match.length) {
            return false;
        }
        int count = 0;
        int[] pos = new int[2]; //tracks the position of the difference for different variations of the same char sequence;
        for (int i = 0; i < template.length; i++) {
            if (template[i] != match[i]) {
                count++;
                if (count > 2) {
                    return false;
                }
                pos[count - 1] = i;
            }
        }
        String templateKey = templateKey(template);
        String posKey = Arrays.toString(pos);
        if (seen.containsKey(templateKey) && seen.get(templateKey).contains(posKey)) {
            return false; //Already seen.
        } else {
            if (seen.containsKey(templateKey)) {
                seen.get(templateKey).add(posKey);
            } else {
                Set<String> set = new HashSet<>();
                set.add(posKey);
                seen.put(templateKey, set);
            }
        }
        return count == 2;
    }

    private String templateKey(char[] template) {
        char[] sortedTemplate = Arrays.copyOf(template, template.length);
        Arrays.sort(sortedTemplate);
        return new String(sortedTemplate); //Sort or unify all strings as keys.
    }

}
