package com.apps.relayr;

import org.testng.annotations.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.testng.Assert.*;

public class FinderTest {

    private final Random r = new Random();

    @Test
    public void testSimple() {
        String[] arr = {"aca", "Aac", "aaC", "aaa"};
        Finder f = new Finder(arr);
        assertEquals(1, f.find("caa").size());
        assertEquals(1, f.find("Caa").size());
        assertEquals(0, f.find("AAA").size());
    }

    @Test
    public void testSpecial() {
        String[] arr = {"µ \u0080", "\tbb ", "", "ûæ", ""};
        Finder f = new Finder(arr);
        assertEquals(2, f.find("").size());
        assertEquals(1, f.find(" \u0080µ").size());
        assertEquals(1, f.find("b b\t").size());
        assertEquals(0, f.find("   bb").size());
    }

    @Test
    public void testEmpty() {
        String[] arr = {};
        Finder f = new Finder(arr);
        assertEquals(0, f.find("caa").size());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullArr() {
        Finder f = new Finder(null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullVal() {
        String[] arr = {};
        Finder f = new Finder(arr);
        f.find(null);
    }

    private final int ITERATIONS = 100000;
    private final int DUPLICATES_RATE = 3;
    private final int SUCCESS_RATE = 2;

    @Test
    public void testDurabilityAndPerfomace() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < ITERATIONS; i++) {
            if (i > 0 && i % DUPLICATES_RATE == 0) {
                list.add(list.get(r.nextInt(i)));
            } else {
                list.add(generateRandomString());
            }
        }

        String[] arr = new String[list.size()];
        list.toArray(arr);

        long start = System.currentTimeMillis();
        Finder f = new Finder(arr);
        for (int i = 0; i < ITERATIONS; i++) {
            if (i > 0 && i % SUCCESS_RATE == 0) {
                f.find(arr[r.nextInt(arr.length)]);
            } else {
                f.find(generateRandomString());
            }
        }
        long end = System.currentTimeMillis();
        assertTrue((end - start) < 2000l, (end - start) + "milliseconds. You are using too slow CPU or you broke algorithm complexity.");
    }

    private String generateRandomString() {
        byte[] array = new byte[r.nextInt(100)];
        r.nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

}

