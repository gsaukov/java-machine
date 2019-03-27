package com.apps.lakescalculator.core;

import java.util.Objects;

public class Surface {

    final int val, index;

    int depth;

    Surface(int val, int index) {
        this.val = val;
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Surface surface = (Surface) o;
        return index == surface.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
