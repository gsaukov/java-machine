package com.apps.leetcode;

import java.util.*;

//https://leetcode.com/problems/sum-of-distances-in-tree/
public class SumOfDistancesInTree {

    Map<Integer, Node> tree = new HashMap<>();
    Set<Integer> visited = new HashSet<>();

    public static void main(String[] args) {
        int[][] A = {{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}};
        SumOfDistancesInTree s = new SumOfDistancesInTree();
        s.sumOfDistancesInTree(6, A);
    }

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        toTree(edges);
        int[] res = new int[tree.size()];
        int i = 0;
        for(Integer nodeId : tree.keySet()) {
            res[i] = sumOfNodeDistances(tree.get(nodeId));
            i++;
            visited = new HashSet<>();
        }
        return res;
    }

    private int sumOfNodeDistances(Node node) {
        Node parent = node.parent;
        visited.add(node.val);
        if (node.children == null && visited.contains(parent.val)) {
            return 0;
        }
        int res = 0;
        if(node.children != null) {
            for(Node child : node.children){
                res = res + sumOfNodeDistances(child) + 1;
            }
        }
        if(parent != null && !visited.contains(parent.val)) {
            res = res + sumOfNodeDistances(parent) + 1;
        }
        return res;
    }

    private void toTree(int[][] edges) {
        for (int[] r : edges) {
            int from = r[0];
            int to = r[1];
            addNodeIfMissing(from);
            addNodeIfMissing(to);
            addReferenceFromTo(from, to);
        }
    }

    private void addNodeIfMissing(int from) {
        if (!tree.containsKey(from)) {
            tree.put(from, new Node(from));
        }
    }

    private void addReferenceFromTo(int from, int to) {
        Node parent = tree.get(from);
        Node child = tree.get(to);
        parent.addChild(child);
        child.addParent(parent);
    }

    private static class Node {
        int val;
        Node parent;
        List<Node> children;

        public Node(int val) {
            this.val = val;
        }

        public void addChild(Node child) {
            if (children == null) {
                this.children = new ArrayList<>();
            }
            children.add(child);
        }

        public void addParent(Node parent) {
            this.parent = parent;
        }
    }

}
