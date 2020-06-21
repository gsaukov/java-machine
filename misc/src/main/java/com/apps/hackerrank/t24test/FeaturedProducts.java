package com.apps.hackerrank.t24test;

import java.util.*;

public class FeaturedProducts {

    public static String featuredProduct(List<String> A) {
        HashMap<String, P> products = new HashMap<>();
        int max = 0;
        for(int i = 0; i<A.size(); i++){
            P p;
            if(products.containsKey(A.get(i))){
                p = products.get(A.get(i));
                p.val += 1;
                products.put(A.get(i),  p);
            } else {
                p = new P(i, 1);
                products.put(A.get(i), p);
            }
            max = Math.max(p.val, max);
        }

        TreeMap<Integer, P> featured = new TreeMap<>();
        for (String k : products.keySet()){
            P p = products.get(k);
            if(p.val == max){
                featured.put(p.pos, p);
            }
        }
        return A.get(featured.firstEntry().getValue().pos);
    }

    public static class P {
        int pos;
        int val;

        public P(int pos, int val) {
            this.pos = pos;
            this.val = val;
        }
    }

    public static void main(String[] args)  {
        List<String> A = Arrays.asList("yellowShirt", "redHat", "blackShirt", "bluePants", "redHat", "pinkHat", "blackShirt", "yellowShirt", "greenPants", "greenPants");

        System.out.println(featuredProduct(A));

    }


}
