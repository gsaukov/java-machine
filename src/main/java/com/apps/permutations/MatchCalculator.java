package com.apps.permutations;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MatchCalculator {

    public static final Float magic24 = new Float("24");

    static String A = "30";
    static String B = "3";
    static String C = "2";
    static String D = "7";

    static Map<Float, List<String>> results = new HashMap();

    public static void main(String[] args) throws Exception {
        Permutations.init(new Object[]{ '+', '-', '*', '/' });
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        for (String c : Permutations.COMBINATIONS){
            String f = A + c.charAt(0) + B  + c.charAt(1) + C + c.charAt(2) + D;
            Float res = new Float(engine.eval(f).toString());
            if(results.containsKey(res)){
                results.get(res).add(f);
            } else {
                results.put(res, new ArrayList<>(Arrays.asList(f)));
            }
        }

        if(results.containsKey(magic24)){
            for(String s : results.get(magic24)){
                System.out.println(s + " = 24");
            }
        }
    }

}
