package com.apps.hackerrank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FrequencyQueries {

    private static Map<Integer, Integer> intFreq = new HashMap<>();
    private static Map<Integer, Set<Integer>> freqInt = new HashMap<>();

    static List<Integer> freqQuery(List<List<Integer>> queries) {

        List<Integer> res = new ArrayList<>();
        for(int i = 0; i< queries.size(); i++){
            List<Integer> q = queries.get(i);
            Integer command = q.get(0);
            Integer val = q.get(1);
            if(command == 1) { //add
                if (intFreq.containsKey(val)){
                    updateFrequencies(val);
                } else {
                    addFrequencies(val);
                }
            } else if (command == 2) {//delete
                deleteFrequencies(val);
            } else if (command == 3) {//sho
                if(freqInt.get(val) != null){
//                    int ints = freqInt.get(val).size();
                    res.add(1);
                } else {
                    res.add(0);
                }
            }
        }

        return res;
    }

    private static void addFrequencies(int val) { //always one
        intFreq.put(val, 1);
        if(freqInt.containsKey(1)){
            freqInt.get(1).add(val);
        } else {
            Set<Integer> vals = new HashSet<>();
            vals.add(val);
            freqInt.put(1, vals);
        }
    }

    private static void deleteFrequencies(int val){
        if(intFreq.get(val) != null) { //dont try to remove what you dont have
            int oldFrequency = intFreq.get(val);
            int newFrequency = intFreq.get(val) - 1;
            freqInt.get(oldFrequency).remove(val);
            if(newFrequency > 0){
                intFreq.put(val, newFrequency);
                freqInt.get(newFrequency).add(val); //we always have new frequency because we had it once before.
            }
        }
    }

    private static void updateFrequencies(int val){
        int oldFrequency = intFreq.get(val);
        int newFrequency = intFreq.get(val) + 1;
        intFreq.put(val, newFrequency);
        freqInt.get(oldFrequency).remove(val);
        if(freqInt.containsKey(newFrequency)){
            freqInt.get(newFrequency).add(val);
        } else {
            Set<Integer> vals = new HashSet<>();
            vals.add(val);
            freqInt.put(newFrequency, vals);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<List<Integer>> A = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get("./misc/src/main/java/com/apps/hackerrank/frequencyQueries.txt"))) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
               String [] s = line.split(" ");
                A.add(addToA(Integer.valueOf(s[0]), Integer.valueOf(s[1])));
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

//        A.add(addToA(1, 5));
//        A.add(addToA(1, 6));
//        A.add(addToA(3, 2));
//        A.add(addToA(1, 10));
//        A.add(addToA(1, 10));
//        A.add(addToA(1, 6));
//        A.add(addToA(2, 5));
//        A.add(addToA(3, 2));

//        A.add(addToA(3, 4));
//        A.add(addToA(2, 1003));
//        A.add(addToA(1, 16));
//        A.add(addToA(3, 1));

        System.out.println(Arrays.toString(freqQuery(A).toArray()));
    }

    public static List<Integer> addToA(int c,int v){
        List<Integer> B = new ArrayList<>();
        B.add(c);
        B.add(v);
        return B;
    }

}
