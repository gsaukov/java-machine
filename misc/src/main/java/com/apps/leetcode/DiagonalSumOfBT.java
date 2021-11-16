package com.apps.leetcode;

import java.util.*;

public class DiagonalSumOfBT {

    public static Map<Integer, Integer> diagonals = new HashMap();

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.right.left = new Node(5);
        root.right.right = new Node(6);
        root.right.left.left = new Node(7);
        root.right.left.right = new Node(8);

        diagonalSum(root, 0);

        for (Integer sum : diagonals.values()) {
            System.out.println(sum + ", ");
        }
    }

    private static void diagonalSum(Node root, int diagonal) {
        if(diagonals.containsKey(diagonal)) {
            int incVal = diagonals.get(diagonal) + root.val;
            diagonals.put(diagonal, incVal);
        } else {
            diagonals.put(diagonal, root.val);
        }

        if(root.left != null) {
            diagonalSum(root.left, diagonal + 1);
        }

        if(root.right != null) {
            diagonalSum(root.right, diagonal);
        }
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }

}
