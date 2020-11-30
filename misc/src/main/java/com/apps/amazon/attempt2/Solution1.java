package com.apps.amazon.attempt2;

class Solution1 {

    public int solution(int[] A, int[] B) {
        if (!isValid(A, B)) {
            return 0; // can throw an error
        }

        int totalSum = getArrSum(A);

        int fairIndex = 0;
        int curSumA = 0;
        int curSumB = 0;
        for(int i = 0; i < A.length; i++){
            curSumA = curSumA + A[i];
            curSumB = curSumB + B[i];
            if((curSumA == curSumB) &&
               (curSumA == totalSum-curSumA) &&
               (curSumB == totalSum-curSumB) &&
               (i != A.length - 1)
            ) {
                fairIndex++;
            }
        }

        return fairIndex;
    }

    private boolean isValid(int[] A, int[] B) {
        if(A.length != B.length){
            return false;
        }
        if(getArrSum(A) != getArrSum(B)){
            return false;
        }
        return true;
    }

    private int getArrSum(int[] A){
        int totalSum = 0;
        for(int i = 0; i < A.length; i++){
            totalSum = totalSum + A[i];
        }
        return totalSum;
    }

    public static void main(String[] args) {
        int [] A = {1, 4, 2, -2, 5};
        int [] B = {7, -2, -2, 2, 5};
        Solution1 s = new Solution1();
        System.out.println(s.solution(A, B));
    }
}

