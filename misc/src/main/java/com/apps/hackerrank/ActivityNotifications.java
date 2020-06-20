package com.apps.hackerrank;


import java.util.Arrays;

public class ActivityNotifications {

    static int activityNotifications(int[] e, int d) {
        int num = 0;
        int[] A = Arrays.copyOf(e, d);
        Arrays.sort(A);
        for(int i = d; i<e.length; i++){
            if(e[i]>=(median(A)*2)){
                num++;
            }
            change(A, e[i]);
        }
        return num;
    }

    private static int[] change(int[] A, int e){
        A[0] = e;
        for(int i = 1; i<A.length; i++){
            if(A[i-1]>A[i]){
                int t = A[i];
                A[i] = A[i-1];
                A[i-1] = t;
            } else {
                return A;
            }
        }
        return A;
    }

    private static double median(int[] A){
        int middle = A.length / 2;
        if (A.length % 2 == 0) {
            double left = A[middle - 1];
            double right = A[middle];
            return (left + right) / 2;
        } else {
            return A[middle];
        }
    }

    public static void main(String[] args)  {
        int[] A = new int[]{1, 2, 3, 4, 4,5,7,8,9,9,3,3,4,5};
        activityNotifications(A, 4);
    }

}
