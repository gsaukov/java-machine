package com.apps.amazon.attempt2;

import java.util.*;

public class Solution2 {

    public static void main(String[] args) {
        int[] A = {0, 9, 0, 2, 6, 8, 0, 8, 3, 0};
        Solution2 s = new Solution2();
        Node n = s.createGraph(A);
        System.out.println(s.getRouteLength(n, 1));
    }

    private int getRouteLength(Node root, int tickets) {
        int maxRoute = 0;
        if (root.getVal() % 2 != 0) {
            tickets = tickets - 1;
            if (tickets < 0) {
                return 0;
            }
        }
        if (root.getChildren().isEmpty()) {
            return 1;
        } else {
            for (Node node : root.getChildren()) {
                int curRoute = getRouteLength(node, tickets) + 1;
                maxRoute = Math.max(maxRoute, curRoute);
            }
        }
        return maxRoute;
    }

    public Node createGraph(int[] T) {
        Node[] nodes = new Node[T.length];
        for (int i = 0; i < T.length; i++) {
            nodes[i] = new Node(i);
        }
        //if C[P] = Q and Q != 0, then there is a direct road between cities P and Q
        Node root = nodes[0];
        for (int i = 0; i < T.length; ++i) {
            if (T[i] != i) {
                nodes[T[i]].addChild(nodes[i]);
            }
        }
        return root;
    }

    private class Node {
        private int val;
        private ArrayList<Node> children;

        public Node(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }

        public int getVal() {
            return this.val;
        }

        public void addChild(Node child) {
            this.children.add(child);
        }

        public ArrayList<Node> getChildren() {
            return this.children;
        }
    }
}
