package com.apps.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;


//https://leetcode.com/problems/merge-k-sorted-lists/
public class MergeKSortedLists {

    private static ListNode[] toNodes(int[][] a) {
        ListNode[] nodes = new ListNode[a.length];
        for(int i = 0; i < a.length; i++) {
            nodes[i] = toNode(a[i]);
        }
        return nodes;
    }

    private static ListNode toNode(int[] a) {
        ListNode headNode = new ListNode(a[0]);
        ListNode nextHead = headNode;
        for(int i = 1; i < a.length; i++) {
            nextHead.next = new ListNode(a[i]);
            nextHead = nextHead.next;
        }
        return headNode;
    }

    private static void printNodes(ListNode mergeKLists) {
        while (mergeKLists != null) {
            System.out.print(mergeKLists.val + ", ");
            mergeKLists = mergeKLists.next;
        }
    }

    public static void main(String[] args) {
        int[][] A = {{1,2,3},{2,3,4},{3,4,8},{1,4},{1,3},{2,4,9}};
        int[][] A2 = {{1, 2, 3}};
//        int[][] A3 = {{}, {}};
        ListNode[] nodes = toNodes(A2);
        MergeKSortedLists mergeKSortedLists = new MergeKSortedLists();
        printNodes(mergeKSortedLists.mergeKLists(nodes));
    }

    public ListNode mergeKLists(ListNode[] lists) {
        TreeMap<Integer, Stack<ListNode>> heads = new TreeMap<>();
        if(lists.length == 0 ) {
            return null;
        }

        List<ListNode> cleanList = new ArrayList<>();

        for(int i = 0 ; i< lists.length; i++) {
            ListNode node = lists[i];
            if(node != null){
                cleanList.add(node);
            }
        }

        if(cleanList.size() == 1) {
            return cleanList.get(0);
        }

        for (ListNode l : cleanList) {
            addToHeads(l, heads);
        }

        if(heads.isEmpty()){
            return null;
        }

        Stack<ListNode> initTier = heads.firstEntry().getValue();
        ListNode resHead = initTier.pop();

        if (initTier.empty()) {
            deleteTier(resHead.val, heads);
        }

        if(resHead.next != null) {
            addToHeads(resHead.next, heads);
        }

        ListNode nextHead = resHead;
        resHead.next = nextHead;

        while (!heads.isEmpty()) {
            Stack<ListNode> tier = heads.firstEntry().getValue();
            nextHead.next = tier.pop();
            if (tier.empty()) {
                deleteTier(nextHead.next.val, heads);
            }
            nextHead = nextHead.next;
            if(nextHead != null && nextHead.next != null) {
                addToHeads(nextHead.next, heads);
            }

        }

        return resHead;
    }

    private void deleteTier(int i, TreeMap<Integer, Stack<ListNode>> heads) {
        heads.remove(i);
    }

    private void addToHeads(ListNode l, TreeMap<Integer, Stack<ListNode>> heads) {
        if (heads.containsKey(l.val)) {
            heads.get(l.val).add(l);
        } else {
            Stack s = new Stack<>();
            s.add(l);
            heads.put(l.val, s);
        }
    }

    public static class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
