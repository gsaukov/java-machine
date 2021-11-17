package com.apps.leetcode;

public class DoubeLinkedListReserve {

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6};
        Node n = new Node(0, null);
        for (int val : arr) {
            n.add(val);
        }

        reverse(n);

        while (n != null) {
            System.out.println(n.val);
            n = n.left; //you reversed id so now you have to traverse ti the left. before it was to the right.
        }
    }

    public static void reverse (Node node) {
        while (node != null) {
            Node oldLeft = node.left;
            Node oldRight = node.right;
            node.right = oldLeft;
            node.left = oldRight;
            node = node.left;
        }
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val, Node left) {
            this.val = val;
            this.left = left;
            this.right = null;
        }
        
        public void add(int val) {
            if (right == null) {
                right = new Node(val, this);
            } else {
                right.add(val);
            }
        }

    }
}
