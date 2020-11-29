package com.apps.sap;

import java.util.*;
// reimplement with duplicates, using priority que.
/**
 * Iterator A -> 1, 5, 6, 67, 100
 * Iterator B -> 1, 3, 4, 8, 1000, 2000
 * Iterator C -> 9000, 9001, 9003
 * <p>
 * MergeIterator -> 1, 3, 4, 5, 6, 8,67, 100, 2000, 9000, 9001, 9003
 */
class MergeIterator implements Iterator<Integer> {
    private final TreeMap<Integer, Iterator<Integer>> tree = new TreeMap<>();
    private Collection<Iterator<Integer>> sources;

    public MergeIterator(Collection<Iterator<Integer>> sources) {
        this.sources = sources;
        for (Iterator<Integer> iter : sources) {
            if (iter.hasNext()) {
                tree.put(iter.next(), iter);
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (tree.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer next() {
        Map.Entry<Integer, Iterator<Integer>> entry = tree.pollFirstEntry();
        Iterator<Integer> iter = entry.getValue();
        if (iter.hasNext()) {
            tree.put(iter.next(), iter);
        }
        return entry.getKey();
    }

    public static void main(String[] args) {
         MergeIteratorTest mergeIteratorTest = new MergeIteratorTest(MergeIterator::new);
         mergeIteratorTest.execute(21, 0, 3);
    }

}
