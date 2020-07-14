package com.apps.finapi.result;

import org.testng.annotations.Test;


import java.util.Arrays;
import java.util.List;

import static com.apps.finapi.result.StringUtils.stringsModifier;
import static org.testng.Assert.*;

public class StringUtilsTest {

    @Test
    public void testStringModifier() {
        List<String> strings = createArray("aaa", null, "", " cccc", "b  b ", "    ", "1$");
        List<String> res = stringsModifier(strings);
        assertEquals(res.size(), 4);
        assertEquals(res.get(0), "1$");
        assertEquals(res.get(1), "AAA");
        assertEquals(res.get(2),  "B  B");
        assertEquals(res.get(3), "CCCC");
    }

    private List<String> createArray(String... e) {
        return Arrays.asList(e);
    }

}
