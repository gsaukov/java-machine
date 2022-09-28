package com.apps.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://leetcode.com/problems/sum-of-distances-in-tree/
public class SumOfDistancesInTree {

    Map<Integer, Node> tree = new HashMap<>();

    public static void main(String[] args) {
        int[][] A = {{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}};
        SumOfDistancesInTree s = new SumOfDistancesInTree();
        s.sumOfDistancesInTree(6, A);
    }

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        toTree(edges);
        int[] res = new int[tree.size()];
        for(Integer nodeId : tree.keySet()) {
        }
        return null;
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
