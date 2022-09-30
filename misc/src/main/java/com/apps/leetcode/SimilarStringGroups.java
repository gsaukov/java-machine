package com.apps.leetcode;

//https://leetcode.com/problems/similar-string-groups/
public class SimilarStringGroups {

    public static void main(String[] args) {
        String[] A = {"tars", "rats", "arts", "star"};
        SimilarStringGroups s = new SimilarStringGroups();
        s.numSimilarGroups(A);
    }

    public int numSimilarGroups(String[] strs) {
        int count = 0;
        for (int i = 0; i < strs.length; i++) {
            char[] template = strs[i].toCharArray();
            for (int j = i + 1; j < strs.length; j++) {
                char[] match = strs[j].toCharArray();
                if(doesMatch(template, match)){
                    count++;
                }
            }
        }
        return count;
    }

    public boolean doesMatch(char[] template, char[] match) {
        if (template.length != match.length) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < template.length; i++) {
            if(template[i] != match[i]) {
                count++;
                if (count > 2) {
                    return false;
                }
            }
        }
        return count == 2;
    }

}
