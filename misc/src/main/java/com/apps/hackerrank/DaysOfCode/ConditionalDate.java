package com.apps.hackerrank.DaysOfCode;

public class ConditionalDate {

    public static void main(String[] args) {
        D start = new D("31 12 2009".split(" "));
        D end = new D("1 1 2010".split(" "));
        System.out.print(start.calculateFine(end));
    }

    public static class D {
        int d;
        int m;
        int y;

        D(String[] A) {
            d = Integer.valueOf(A[0]);
            m = Integer.valueOf(A[1]);
            y = Integer.valueOf(A[2]);
        }

        public int calculateFine(D d) {
            if(this.y > d.y){
                return 10000;
            } else if (this.m > d.m && this.y == d.y)  {
                return (this.m - d.m) * 500;
            } else if (this.d > d.d && this.m == d.m && this.y == d.y) {
                return (this.d - d.d) * 15;
            }
            return 0;
        }
    }
}
