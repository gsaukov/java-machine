package com.apps.hackerrank;

import java.util.*;

public class ClimbingLeaderBoard {

    public static List<Integer> climbingLeaderboard(List<Integer> ranked, List<Integer> player) {
        TreeSet<Integer> tree = new TreeSet<>();
        tree.addAll(ranked);
        List<Integer> res = new ArrayList<>();
        for(Integer score : player) {
            res.add(tree.tailSet(score, false).size() + 1);
        }
        return res;
    }

    public static void main(String[] args)  {
        List<Integer> ranked = Arrays.asList(100, 100, 50, 40, 40, 20, 10);
        List<Integer> player = Arrays.asList(5, 25, 50, 120);
        System.out.println(Arrays.toString(climbingLeaderboard(ranked, player).toArray()));
    }
}
