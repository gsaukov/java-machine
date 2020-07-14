package com.apps.finapi.result;

import org.testng.annotations.Test;

import java.util.*;

import static com.apps.finapi.result.CollectionUtils.getElementCount;
import static org.testng.Assert.*;

public class CollectionUtilsTest {

    @Test
    public void testTwo() {
        assertEquals(getElementCount(createArray("", null, new Object())), 2);
    }

    @Test
    public void testNull() {
        assertEquals(getElementCount(null), -1);
    }

    private List<Object> createArray(Object... e) {
        return Arrays.asList(e);
    }
}
