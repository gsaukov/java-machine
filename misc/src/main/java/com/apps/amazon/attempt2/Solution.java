package com.apps.amazon.attempt2;

import java.util.HashMap;
import java.util.Map;

class Solution {

    public int solution(int[] A) {
        if (!isValid(A)) {
            return 0;
        }

        Map<Integer, Integer> resMap = new HashMap<>();

        for (int el : A) {
            Integer val = resMap.get(el);
            if (val != null) {
                resMap.put(el, val + 1);
            } else {
                resMap.put(el, 1);
            }
        }

        int maxVal = 0;
        for (Integer el : resMap.keySet()) {
            Integer val = resMap.get(el);
            if (el.equals(val)) {
                maxVal = Math.max(maxVal, val);
            }
        }

        return maxVal;
    }

    private boolean isValid(int[] A) {
        if (A == null || A.length == 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] A = {5,5,5,5,5,66,3,4,4,4,4};
        Solution s = new Solution();
        System.out.println(s.solution(A));
    }
}

