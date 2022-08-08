package com.apps.codility;

import java.util.*;

import static java.util.Arrays.asList;

public class DFSAllPaths {


    private static HashMap<String, Set<String>> graph = new HashMap<>();

    public static void main(String[] args) {
        graph.put("A", new HashSet<>(asList("B", "C")));                  //     A
        graph.put("B", new HashSet<>(asList("A", "D", "E")));             //   /  \
        graph.put("C", new HashSet<>(asList("A", "F")));                  //  C    B       H
        graph.put("D", new HashSet<>(asList("B")));                       //  |   / \    /  \
        graph.put("E", new HashSet<>(asList("B", "F", "G")));             //  |  D   E--G----I
        graph.put("F", new HashSet<>(asList("C", "E", "F1")));            //  |     /    \    \
        graph.put("G", new HashSet<>(asList("E", "H", "J", "I")));        //   \   /      J----K
        graph.put("H", new HashSet<>(asList("G", "I")));                  //     F---F1
        graph.put("I", new HashSet<>(asList("H", "G", "K")));             //          \
        graph.put("J", new HashSet<>(asList("G", "K")));                  //           F2
        graph.put("K", new HashSet<>(asList("J", "I")));
        graph.put("F1", new HashSet<>(asList("F", "F2")));
        graph.put("F2", new HashSet<>(asList("F", "F1")));

        List<String> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        visited.add("A");
        path.add("A");
        dfsReq("A", "K", visited, path);
    }

    private static boolean dfsReq(String current, String end, Set<String> visited, List<String> path) {
        Set<String> nodes = new HashSet<>(graph.get(current));
        nodes.removeAll(visited);
        boolean found = false;
        for(String node: nodes) {
            if(node.equals(end)) {
                found = true;
                path.add(node);
                System.out.println("----------------------------");
                for(String nodeP : path) {
                    System.out.println(nodeP);
                }
            } else {
                Set<String> currentVisited = new HashSet<>(visited);
                currentVisited.add(node);
                List<String> currentPath = new ArrayList<>(path);
                currentPath.add(node);
                if(dfsReq(node, end, currentVisited, currentPath)) {
                    found = true;
                }
            }
        }
        return found;
    }



}
