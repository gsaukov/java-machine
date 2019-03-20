package com.apps.lakescalculator;

import java.util.Comparator;

public class IndexComparator implements Comparator<Surface> {

    @Override
    public int compare(Surface surface1, Surface surface2) {
        if (surface1.index < surface2.index) {
            return -1;
        } else if (surface1.index > surface2.index) {
            return 1;
        }
        return 0;
    }

}
