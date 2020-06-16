package com.apps.codility;

import org.python.antlr.ast.Str;

import java.util.*;

import static java.util.Arrays.asList;

public class DFSGraph {
    public static void main(String[] args) {
        HashMap<String, List<String>> graph = new HashMap<>();
        graph.put("A", asList("B", "C"));                  //     A
        graph.put("B", asList("A", "D", "E"));             //   /  \
        graph.put("C", asList("A", "F"));                  //  C    B       H
        graph.put("D", asList("B"));                       //  |   / \    /  \
        graph.put("E", asList("B", "F", "G"));             //  |  D   E--G----I
        graph.put("F", asList("C", "E", "F1"));            //  |     /    \    \
        graph.put("G", asList("E", "H", "J", "I"));        //   \   /      J----K
        graph.put("H", asList("G", "I"));                  //     F---F1
        graph.put("I", asList("H", "G", "K"));             //          \
        graph.put("J", asList("G", "K"));                  //           F2
        graph.put("K", asList("J", "I"));
        graph.put("F1", asList("F", "F2"));
        graph.put("F2", asList("F", "F1"));

        dfs(graph, "B");
//        dfsrec(graph, null, "B");
    }

    private static Set<String> dfs(HashMap<String, List<String>> graph, String start) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        stack.push(start);
        visited.add(start);
        while (!stack.empty()){
            String elem = stack.pop();
            List<String> nodes = graph.get(elem);
            for(String node: nodes) {
                if(!visited.contains(node)) {
                    stack.push(node);
                    visited.add(node);
                }
            }
            //do what you need here with the node
            System.out.println(elem);
        }
        return visited;
    }

    private static Set<String> dfsrec(HashMap<String, List<String>> graph, Set<String> visited, String start) {
        if(visited == null){
            visited = new HashSet<>();
        }
        //do what you need here with the node
        System.out.println(start);
        visited.add(start);
        List<String> nodes = graph.get(start);

        for (String node : nodes){
            if(!visited.contains(node)) {
                dfsrec(graph, visited, node);
            }
        }

        return visited;
    }

}
