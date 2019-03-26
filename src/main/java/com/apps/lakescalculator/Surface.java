package com.apps.lakescalculator;

import java.util.Objects;

public class Surface {

    public final int val, index;

    private int depth;

    Surface(int val, int index) {
        this.val = val;
        this.index = index;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
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
