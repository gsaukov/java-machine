package com.apps.lakescalculator;

import java.util.List;

public class Lake {

    private final int mirror;
    private final int maxDepth;
    private final double averageDepth;
    private final double formFactor;
    private final List<Surface> lakeSurface;

    Lake(int mirror, int maxDepth, double averageDepth, double formFactor, List<Surface> lakeSurface) {
        this.mirror = mirror;
        this.maxDepth = maxDepth;
        this.averageDepth = averageDepth;
        this.formFactor = formFactor;
        this.lakeSurface = lakeSurface;
    }

    public int getMirror() {
        return mirror;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public double getAverageDepth() {
        return averageDepth;
    }

    public double getFormFactor() {
        return formFactor;
    }

    public List<Surface> getLakeSurface() {
        return lakeSurface;
    }

}
