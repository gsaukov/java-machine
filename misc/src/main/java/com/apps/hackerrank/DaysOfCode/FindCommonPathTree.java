package com.apps.hackerrank.DaysOfCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class FindCommonPathTree {
    //find common path in tree for two elements
    public static void main(String[] args) {
        int [] A = {5,8,7,2,1,};
        Node root = new Node(4);
        for(int e : A){
            root.insert(root, e);
        }
        findCommonPath(root, 2,3);
    }

    public static List<Node> findCommonPath(Node root, int val1, int val2) {
        List<Node> res = new ArrayList<>();
        Stack<Node> queue = new Stack();
        queue.add(root);
        while(!queue.isEmpty()) {
            Node node = queue.pop();
            res.add(node);
            if(node.data > val1 && node.data > val2){
                if(node.left!=null){
                    queue.add(node.left);
                }
            } else if (node.data < val1 && node.data < val2) {
                if(node.right!=null) {
                    queue.add(node.right);
                }
            } else {
                return res;
            }
        }
        return res;
    }

    public static class Node {
        Node left, right;
        int data;

        Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        public Node insert(Node root, int data){
            if(root==null){
                return new Node(data);
            }
            else{
                Node cur;
                if(data<=root.data){
                    cur=insert(root.left,data);
                    root.left=cur;
                }
                else{
                    cur=insert(root.right,data);
                    root.right=cur;
                }
                return root;
            }
        }
    }
}
