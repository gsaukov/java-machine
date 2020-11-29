package com.apps.codility;

import java.util.*;

public class NailingPlanks {
    public static void main(String[] args) {
        int[] A = new int[]{1, 4, 5, 8};
        int[] B = new int[]{4, 5, 9, 10};
        int[] C = new int[]{4, 6, 7, 10, 2};

        System.out.println(solution(A, B, C));
    }

    private static int solution(int[] A, int[] B, int[] C) {
        Set<Integer> usedNails = new HashSet<>();
        TreeSet<Integer> availableNails = new TreeSet<>();
        for (Integer e : C) {
            availableNails.add(e);
        }


        for(int i = 0; i < A.length; i++){
            //plank start and end
            int start = A[i];
            int end = B[i];
            NavigableSet<Integer> applicableNails = availableNails.subSet(start, true, end, true);
            if(alreadyNailed(applicableNails, usedNails)) {
                //if already nailed do nothing
            } else {
                //pick nail with max value that would nail the plank.
                Integer nail = applicableNails.last();
                usedNails.add(nail);
            }
        }

        for(int i = 0; i < C.length; i++){
            usedNails.remove(C[i]);
            if(usedNails.isEmpty()) {
                return i + 1;
            }
        }
        return 0;
    }

    private static boolean alreadyNailed(NavigableSet<Integer> applicableNails, Set<Integer> usedNails) {
        for(Integer e : applicableNails){
            if (usedNails.contains(e)){
                return true;
            }
        }
        return false;
    }
}
