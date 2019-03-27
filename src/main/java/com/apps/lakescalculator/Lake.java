package com.apps.lakescalculator;

import java.util.List;

public class Lake {

    private final int volume;
    private final int mirror;
    private final int seaLevel;
    private final int maxHeight;
    private final int maxDepth;
    private final double averageArithmeticDepth;
    private final double formFactor;
    private final boolean total;
    private final List<Surface> lakeSurface;


    private Lake(Builder builder){
        this.volume = builder.volume;
        this.seaLevel = builder.seaLevel;
        this.maxDepth = builder.maxDepth;
        this.lakeSurface = builder.lakeSurface;
        this.mirror = builder.mirror;
        this.formFactor = builder.formFactor;
        this.maxHeight = builder.maxHeight;
        this.total = builder.total;
        this.averageArithmeticDepth = builder.averageArithmeticDepth;

    }

    public int getVolume() {
        return volume;
    }

    public int getMirror() {
        return mirror;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public double getAverageArithmeticDepth() {
        return averageArithmeticDepth;
    }

    public double getFormFactor() {
        return formFactor;
    }

    public boolean isTotal() {
        return total;
    }

    public List<Surface> getLakeSurface() {
        return lakeSurface;
    }

    public static final class Builder {
        private int volume;
        private int mirror;
        private int seaLevel;
        private int maxHeight;
        private int maxDepth;
        private double averageArithmeticDepth;
        private double formFactor;
        private boolean total;
        private List<Surface> lakeSurface;


        public Builder() {
        }


        public Builder withVolume(int volume) {
            this.volume = volume;
            return this;
        }

        public Builder withMirror(int mirror) {
            this.mirror = mirror;
            return this;
        }

        public Builder withSeaLevel(int seaLevel) {
            this.seaLevel = seaLevel;
            return this;
        }

        public Builder withMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder withMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public Builder withAverageArithmeticDepth(double averageArithmeticDepth) {
            this.averageArithmeticDepth = averageArithmeticDepth;
            return this;
        }

        public Builder withFormFactor(double formFactor) {
            this.formFactor = formFactor;
            return this;
        }

        public Builder withLakeSurface(List<Surface> lakeSurface) {
            this.lakeSurface = lakeSurface;
            return this;
        }

        public Builder setTotal(boolean total) {
            this.total = total;
            return this;
        }

        public Lake build() {
            return new Lake(this);
        }
    }
}
