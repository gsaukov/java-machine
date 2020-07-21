package com.apps.finapi.result;

import java.util.Collection;

public class CollectionUtils {

    private CollectionUtils() {}

    /**
     * Returns the count of (non-null) elements within a collection
     *
     * @param col the collection
     * @return count of (non-null) elements
     */
    public static int getElementCount(Collection col) {
        if (col == null) {
            return -1;
        }
        int count = 0;
        for (Object o : col) {
            if (o != null) {
                count++;
            }
        }
        return count;
    }
}
