package com.apps.codility;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Fish {

    public static void main(String[] args) {
        int[] A = new int[]{4,3,2,1,9,7,5,8};
        int[] B = new int[]{1,0,0,1,1,1,1,0};

        System.out.println(solution(A, B));
    }

    static List<Integer> dead = new ArrayList<>();

    public static int solution(int[] A, int[] B){
        Stack<F> counter = new Stack<>();

        for(int i=0; i<B.length; i++) {
            if(B[i]==1){
                counter.push(new F(i, A[i]));
            }else{
                while (!counter.empty()){
                    F c = counter.pop();
                    if(A[i]<c.val){
                        dead.add(i);
                        counter.push(c);
                        break;
                    } else {
                        dead.add(c.pos);
                    }
                }
            }
        }

        return A.length - dead.size();
    }

    public static class F {
        int pos;
        int val;
        F(int pos, int val){
            this.pos = pos;
            this.val = val;
        }
    }

}
