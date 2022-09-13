package com.apps.leetcode;

import java.util.*;

public class FlowerPlanting {

//    https://leetcode.com/problems/flower-planting-with-no-adjacent/

    public static void main(String[] args) {
        int[][] A = {{1,2},{2,3},{3,4},{4,1},{1,3},{2,4}};
        int[][] A2 = {{1,2},{2,3},{3,1}};
        FlowerPlanting flowerPlanting = new FlowerPlanting();
        System.out.println(Arrays.toString(flowerPlanting.gardenNoAdj(3, A2)));
    }

    public int[] gardenNoAdj(int n, int[][] paths) {
        Map<Integer, F> graph = new HashMap<>();
        for(int i = 1 ; i<=n; i++){
            graph.put(i, new F(0, new HashSet<>()));
        }
        for(int i = 0 ; i<paths.length; i++){
            if(paths[i][0] == paths[i][1]){
                throw new IllegalStateException("Path x == y.");
            }

            graph.get(paths[i][0]).paths.add(paths[i][1]); //add path parent to child
            graph.get(paths[i][1]).paths.add(paths[i][0]); //add path child to parent so we can track parents color.

            if(graph.get(paths[i][0]).paths.size() > 3){
                throw new IllegalStateException("More than 3 paths.");
            }
        }

        for(F f : graph.values()) {
            plant(f, graph);
        }

        int[] res = new int[graph.size()];
        for(int i = 1 ; i <= graph.size(); i++){
            res[i-1] = graph.get(i).flower;
        }
        return res;
    }

    public void plant(F f, Map<Integer, F> graph){
        Set<Integer> neighbourF = new HashSet<>();
        for(Integer e : f.paths){
            neighbourF.add(graph.get(e).flower);
        }

        for(int i = 1; i <= 4; i++){
            if(neighbourF.contains(i)){
                continue; //neighbor took that color
            } else {
                f.flower = i;
                return;
            }
        }
        throw new IllegalStateException("Algorithm is wrong all flowers taken by neighbours.");
    }

    public class F {
        public int flower;
        public Set<Integer> paths;

        public F(int flower, Set<Integer> paths) {
            this.flower = flower;
            this.paths = paths;
        }
    }

}


