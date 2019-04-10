package com.apps.lakescalculator3d.core;

import java.util.Objects;

public class Surface {

    final int val, x, y;

    int depth, depthX, depthY;

    Surface(int val, int x, int y) {
        this.val = val;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Surface surface = (Surface) o;
        return x == surface.x &&
                y == surface.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
