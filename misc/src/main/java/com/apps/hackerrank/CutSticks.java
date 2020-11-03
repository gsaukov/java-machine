package com.apps.hackerrank;

import java.util.*;

//https://www.hackerrank.com/challenges/cut-the-sticks/problem
public class CutSticks {

    static int[] cutTheSticks(int[] arr) {
        NavigableMap<Integer, Integer> trees = new TreeMap<>();
        for(Integer e: arr) {
            if(trees.containsKey(e)){
                trees.put(e, trees.get(e) + 1);
            } else {
                trees.put(e, 1);
            }
        }

        int[] res = new int[trees.size()];
        int currentTree = 0;
        int size = trees.size();
        for (int i = 0; i < size; i++) {
            for(Integer e : trees.values()) {
                res[i] = res[i] + e;
            }

            currentTree = trees.firstEntry().getKey();
            trees = trees.tailMap(currentTree, false);
        }
        return res;
    }


    public static void main(String[] args) {
        int[] A = new int[]{1, 2, 3, 4, 3, 3, 2, 1 };
        System.out.println(Arrays.toString(cutTheSticks(A)));
    }

}
