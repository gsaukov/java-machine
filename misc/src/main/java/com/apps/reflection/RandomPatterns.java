package com.apps.reflection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RandomPatterns {

    private static final List<RandomPattern> randomPatterns = new CopyOnWriteArrayList<>();

    public static void addRandomPattern(RandomPattern pattern) {
        randomPatterns.add(pattern);
    }

    public static void addRandomPattern(List<RandomPattern> patterns) {
        randomPatterns.addAll(patterns);
    }

    public static List<RandomPattern> get() {
        return randomPatterns;
    }


}
