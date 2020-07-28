package com.apps.hackerrank.DaysOfCode;

import java.util.*;

public class BinaryTree {

    public static void main(String args[]){

        Node root=null;
        int height=getHeight(root);
        System.out.println(height);
    }

    public static int getHeight(Node root){

        int left = 0;
        int right = 0;

        if(root.left != null) {
            left = getHeight(root.left) + 1;
        }

        if(root.right != null) {
            right = getHeight(root.right) + 1;
        }

        return Math.max(left, right);
    }

    static void levelOrder(Node root){
        List<Integer> res = new ArrayList<>();
        LinkedList<Node> s = new LinkedList<>();
        s.push(root);
        res.add(root.data);
        while(s.size()>0){
            Node n = s.pollFirst();
            if (n.left != null) {
                s.addLast(n.left);
                res.add(n.left.data);
            }
            if (n.right != null) {
                s.addLast(n.right);
                res.add(n.right.data);
            }
        }
        for(Integer e : res) {
            System.out.print(e + " ");
        }
    }

    public static class Node {
        Node left, right;
        int data;

        Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

    }

    public static Node insert(Node root,int data){
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
