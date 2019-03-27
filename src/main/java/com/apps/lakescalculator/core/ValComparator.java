package com.apps.lakescalculator.core;

import java.util.Comparator;

final class ValComparator implements Comparator<Surface> {

    @Override
    public int compare(Surface surface1, Surface surface2) {
        if (surface1.val < surface2.val) {
            return 1;
        } else if (surface1.val > surface2.val) {
            return -1;
        }
        return 0;
    }

}
