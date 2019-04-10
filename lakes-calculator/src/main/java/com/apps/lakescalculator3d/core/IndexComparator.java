package com.apps.lakescalculator3d.core;

import java.util.Comparator;

final class IndexComparator implements Comparator<Surface> {

    @Override
    public int compare(Surface surface1, Surface surface2) {
        if (surface1.x * surface1.y  < surface2.x * surface2.y) {
            return -1;
        } else if (surface1.x * surface1.y > surface2.x * surface2.y) {
            return 1;
        }
        return 0;
    }

}
