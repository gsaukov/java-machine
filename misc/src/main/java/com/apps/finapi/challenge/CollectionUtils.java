package com.apps.finapi.challenge;

import java.util.ArrayList;

// TODO: Review this code and comment on it: Is it correct? Is there something that could be improved?

public class CollectionUtils {

    /**
     * Returns the count of (non-null) elements within a collection
     *
     * @param list the collection
     * @return count of (non-null) elements
     */
    public long getElementCount(ArrayList<Object> list) {
        if (list == null) {
            return -1L;
        }
        long count = 0L;
        for (Object o : list) {
            if (o == null) {
                break;
            }
            count++;
        }
        return count;
    }
}
