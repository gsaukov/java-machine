package com.apps.permutations;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public static List<String> COMBINATIONS = new ArrayList<>();

    static void permute(Object[] a, int k, PermuteCallback callback) {
        int n = a.length;

        int[] indexes = new int[k];
        int total = (int) Math.pow(n, k);

        Object[] snapshot = new Object[k];
        while (total-- > 0) {
            for (int i = 0; i < k; i++){
                snapshot[i] = a[indexes[i]];
            }
            callback.handle(snapshot);

            for (int i = 0; i < k; i++) {
                if (indexes[i] >= n - 1) {
                    indexes[i] = 0;
                } else {
                    indexes[i]++;
                    break;
                }
            }
        }
    }

    public static interface PermuteCallback{
        public void handle(Object[] snapshot);
    };

    public static void init(Object[] chars, int range) {
        PermuteCallback callback = new PermuteCallback() {

            @Override
            public void handle(Object[] snapshot) {
                String combination = "";
                for(int i = 0; i < snapshot.length; i ++){
                    combination += snapshot[i];
                }
                COMBINATIONS.add(combination);
            }
        };
        permute(chars, range, callback);
    }
}