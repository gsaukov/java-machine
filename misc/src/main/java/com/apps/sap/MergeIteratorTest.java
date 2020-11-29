package com.apps.sap;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toList;

public class MergeIteratorTest {
    private final Function<Collection<Iterator<Integer>>, Iterator<Integer>> constructor;

    public MergeIteratorTest( Function<Collection<Iterator<Integer>>, Iterator<Integer>> constructor ) {
        this.constructor = constructor;
    }

    public void execute( int numUnique, int numDuplicates, int numCollections ) {
        Collection<List<Integer>> testSet = newTestSet(numUnique, numDuplicates, numCollections);
        testSet.forEach(System.out::println);
        Iterator<Integer> mergeIterator =
                constructor.apply(testSet.stream().map(Collection::iterator).collect(toList()));
        Integer previous = null;
        int count = 0;
        boolean inOrder = true;
        System.out.print("Merge iterator:");
        while( mergeIterator.hasNext() ) {
            Integer next = mergeIterator.next();
            System.out.print(" " + next);
            inOrder = inOrder && (previous == null || next.compareTo(previous) >= 0);
            count++;
            previous = next;
        }
        System.out.println();
        if( inOrder && count == testSet.stream().map(Collection::size).reduce(0, Integer::sum) ) {
            System.out.println("SUCCESS!");
        } else {
            System.err.println("FAILURE!");
        }
    }
    private Collection<List<Integer>> newTestSet( int numUnique, int numDuplicates, int numCollections ) {
        // Create required number of unique elements
        List<Integer> elements = IntStream.range(0, numUnique).boxed().collect(toList());
        // Add required number of duplicates
        for( int i = 0; i < numDuplicates; i++ ) {
            elements.add(ThreadLocalRandom.current().nextInt(0, numUnique));
        }
        // Randomly split elements into `numCollections` parts
        int[] split = new int[numCollections + 1];
        split[0] = 0;
        split[split.length - 1] = elements.size();
        for( int i = 1; i < split.length - 1; i++ ) {
            split[i] = ThreadLocalRandom.current().nextInt(split[i - 1], elements.size());
        }
        List<List<Integer>> testSet = new ArrayList<>(numCollections);
        for( int i = 1; i < split.length; i++ ) {
            testSet.add(elements.subList(split[i - 1], split[i]));
        }
        testSet.forEach(Collections::sort);
        return testSet;
    }
}
