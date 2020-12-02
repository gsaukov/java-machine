package com.apps.amazon.attempt2;

import java.util.*;

public class Solution2 {

    public static void main(String[] args) {
        int[] A = {0, 9, 0, 2, 6, 8, 0, 8, 3, 0};
        int[] B = {0, 0, 0, 1, 6, 1, 0, 0};
        Solution2 s = new Solution2();
        Node a = s.createGraph(A);
        Node b = s.createGraph(B);
        System.out.println(s.getRouteLength(a, 1));
        System.out.println(s.getRouteLength(b, 1));
    }

    private int getRouteLength(Node root, int tickets) {
        int maxRoute = 0;

        if (root.getVal() % 2 != 0) {
            tickets = tickets - 1;
        }

        if (tickets < 0) {
            return 0; //we didnt visited the node
        } else if (root.getChildren().isEmpty()) {
            return 1; //we have visited this node
        } else {
            for (Node node : root.getChildren()) {
                int curRoute = getRouteLength(node, tickets) + 1; //increment value on transit.
                maxRoute = Math.max(maxRoute, curRoute);
            }
            return maxRoute;
        }
    }

    public Node createGraph(int[] A) {
        //if C[X] = Y and Y != 0, then there is a path between nodes X and Y
        //1. Number of graph nodes equal to number of array elements.
        //2. One Array element is one graph node.
        //3. Array element index represent value of a node.
        //4. Array element value represent Index or reference of its parent node. This creates link between nodes in graph.
        Node[] nodes = new Node[A.length];
        for (int i = 0; i < A.length; i++) {
            nodes[i] = new Node(i);
        }
        Node root = nodes[0];
        for (int i = 0; i < A.length; ++i) {
            if (A[i] != i) { //you can not attach node to itself
                nodes[A[i]].addChild(nodes[i]);
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
