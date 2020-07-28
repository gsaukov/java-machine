package com.apps.hackerrank.DaysOfCode;

import java.util.HashSet;
import java.util.Set;

public class LinkedListNodes {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        int [] A = {5,7,8,2,1};
        Node head = null;
        for(int e : A){
            head = insert(head,e);
        }

        Node start = head;
        while(start != null) {
            System.out.print(start.data + " ");
            start = start.next;
        }
    }

    public static  Node insert(Node head,int data) {
        Node n = new Node(data);

        Node last = head;
        while(last != null) {
            //iterate to last node
            if(last.next == null) {
                last.next = n;
                break;
            }
            last = last.next;
        }
        if (head == null){
            return n;
        } else {
            return head;
        }
    }

    public static Node removeDuplicates(Node head) {
        Set<Integer> s = new HashSet<>();
        Node previous = head;
        Node current = head;
        while (current != null) {
            if(!s.contains(current.data)){
                s.add(current.data);
                previous = current;
                current = current.next;
            } else {
                previous.next = current.next;
                current = current.next;
            }
        }

        return head;
    }

}

class Node {
    int data;
    Node next;
    Node(int d) {
        data = d;
        next = null;
    }
}


