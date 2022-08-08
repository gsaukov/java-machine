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


        List<String> path = dfsReq("A", "E", new HashSet<>());
        for(String node : path) {
            System.out.println(node);
        }
    }

    private static List<List<String>> bfsAllPaths(HashMap<String, Set<String>> graph, String start, String target) {
//
//        List<List<String>> res = new ArrayList<>();
//        LinkedList<QueueItem> queue = new LinkedList<>();
//        queue.push(new QueueItem(start, new HashSet<>(asList(start))));

//        graph.get()
//        (vertex, path) = queue.pop(0)
//        for next in graph[vertex] - set(path):
//        while (!queue.isEmpty()) {
//            QueueItem item = queue.pop();
//            item.ribs.removeAll()
//            for next in graph[vertex] - set(path):
//                if next == goal:
//                    yield path + [next]
//                else:
//                    queue.append((next, path + [next]))
//
//        }
        return null;
    }

    private static List<String> dfsReq(String current, String end, Set<String> visited) {
        Set<String> nodes = graph.get(current);
        nodes.removeAll(visited);
        for(String node: nodes) {
            visited.add(node);
            if(node.equals(end)) {
                List<String> res = new ArrayList<>();
                res.add(end);
                return res;
            } else {
                List<String> res = dfsReq(node, end, visited);
                if(res!=null) {
                    res.add(node);
                }
                return res;
            }
        }
        return null;
    }




}
