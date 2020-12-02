package com.apps.amazon.attempt2;

import java.util.*;

public class Solution2 {

    public static void main(String[] args) {
        int [] A = {0, 9, 0, 2, 6, 8, 0, 8, 3, 0};
        Solution2 s = new Solution2();
        Node n = s.createGraph(A);
        System.out.println(n);
    }

//    private static Set<String> dfsRouteLength(Node root) {
//        int tickets = getTickets(root);
//        Set<Node> visited = new HashSet<>();
//        Stack<Node> stack = new Stack<>();
//        stack.push(root);
//        visited.add(root);
//        while (!stack.empty()){
//            Node node = stack.pop();
//            List<Node> nodes = node.getChildren();
//            for(Node childNode : nodes) {
//                if(!visited.contains(childNode)) {
//                    stack.push(childNode);
//                    visited.add(childNode);
//                }
//            }
//            //do what you need here with the node
////            System.out.println(elem);
//        }
//        return visited;
//    }
//
//    private static int getTickets(Node root) {
//        return root.getVal() % 2 == 0? 1 : 0;
//    }


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
