package com.apps.amazon;

public class DivideArrayOnThreeEqualParts {

    public static void main(String[] args) {
        int[] A = {0, 9, 0, 2, 3, 8, 0, 8, 3, 0};
        int[] B = {0, 0, 0, 1, 6, 1, 0, 0};
        System.out.println(devide(B));
    }

    //1. Create Algorithm
    //2. Edgecases/validations.
    //3. Improve clear.
    public static boolean devide(int[] A) {
        int totalSum = 0;
        for(int e : A) {
            totalSum = totalSum + e;
        }


        int leftPointer = 0;
        int rightPointer = A.length - 1;
        int leftSum = A[leftPointer];
        int rightSum = A[rightPointer];
        while (leftPointer < rightPointer) {
            if(leftSum>rightSum) {//left greater
                rightPointer--;
                rightSum = rightSum + A[rightPointer];
            }
            else if(leftSum<rightSum) {//right greater
                leftPointer++;
                leftSum = leftSum + A[leftPointer];
            }
            else { //even
                if((totalSum - leftSum - rightSum) == rightSum) {
                    return true;
                } else {
                    leftPointer++;
                    leftSum = leftSum + A[leftPointer];
                }
            }
        }
        return false;
    }

}
