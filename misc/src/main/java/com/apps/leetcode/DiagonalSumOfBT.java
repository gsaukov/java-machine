package com.apps.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiagonalSumOfBT {

    public static List<Integer> diagonals = new ArrayList<>();

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.right.left = new Node(5);
        root.right.right = new Node(6);
        root.right.left.left = new Node(7);
        root.right.left.right = new Node(8);

        diagonals.add(root.val);
        diagonalSum(root, 0);

        for (Integer sum : diagonals) {
            System.out.println(sum + ", ");
        }
    }

    private static void diagonalSum(Node root, int diagonal) {
        if(root.left != null) {
            diagonals.add(root.left.val);
            diagonalSum(root.left, diagonal + 1);
        }

        if(root.right != null) {
            int incVal = diagonals.get(diagonal) + root.right.val;
            diagonals.set(diagonal, incVal);
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
