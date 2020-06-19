package com.apps.hackerrank;

import java.util.HashMap;


public class RansomeNoteHashMap {

    static void checkMagazine(String[] magazine, String[] note) {

        HashMap<String, Integer> m = create(magazine);
        HashMap<String, Integer> n = create(note);

        for(String w : n.keySet()){
            if(m.get(w) == null || m.get(w) < n.get(w)){
                System.out.println("No");
                return;
            }
        }
        System.out.println("Yes");
    }

    public static HashMap<String, Integer> create(String[] words){
        HashMap<String, Integer> m = new HashMap<>();
        for(String w : words){
            if(m.get(w)!=null){
                m.put(w, m.get(w) + 1);
            } else {
                m.put(w, 1);
            }
        }
        return m;
    }




    public static void main(String[] args) {

        String[] A = new String[]{"two", "times", "three", "is", "not", "four"};
        String[] B = new String[]{"two", "times", "two", "is", "four"};

        checkMagazine(A, B);
    }
}
