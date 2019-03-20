package com.apps.lakescalculator;

import java.util.Objects;

public class Surface {

    public int val, index, depth;

    public Surface(int val, int index) {
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
