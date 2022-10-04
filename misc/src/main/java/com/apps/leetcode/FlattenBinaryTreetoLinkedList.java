package com.apps.leetcode;

import java.util.HashSet;
import java.util.Set;

//
public class FlattenBinaryTreetoLinkedList {

    public static void main(String[] args) {
        /*    1
            /   \
           2     5
          / \     \
         3   4     6 */

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.right.right = new TreeNode(6);
        FlattenBinaryTreetoLinkedList s = new FlattenBinaryTreetoLinkedList();
        s.flatten(root);
    }

    private TreeNode newRoot;
    private TreeNode activeNode;
    private TreeNode previousActiveNode;
    private Set<Integer> visited;

    public void flatten(TreeNode root) {
        if (root == null) return;
        visited = new HashSet<>();
        activeNode = new TreeNode();
        newRoot = previousActiveNode = activeNode;
        iterateTree(root);
        previousActiveNode.right = null;
        root = newRoot;
    }

    private void iterateTree(TreeNode source){
        visited.add(source.val);
        activeNode.val = source.val;
        activeNode.left = null;
        activeNode.right = new TreeNode();
        previousActiveNode = activeNode;
        activeNode = activeNode.right;
        while(source.left != null && !visited.contains(source.left.val)) {
            iterateTree(source.left);
        }
        while(source.right != null && !visited.contains(source.right.val)) {
            iterateTree(source.right);
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
