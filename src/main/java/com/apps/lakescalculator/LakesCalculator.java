package com.apps.lakescalculator;

import java.util.*;

public class LakesCalculator {

    //TODO calculate depth, mirror, volume. Go 3d some day.

    public static List<Lake> calculate(int[] arr) {
        TreeSet<Surface> surfaces = convert(arr);
        List<List<Surface>> lakes = new ArrayList<>();
        buildLakes(surfaces, lakes);
        return calculateLakeData(lakes);
    }

    private static TreeSet<Surface> convert(int[] arr) {
        TreeSet<Surface> surfaces = new TreeSet<>(new IndexComparator());
        for (int i = 0; i < arr.length; i++) {
            surfaces.add(new Surface(arr[i], i));
        }
        return surfaces;
    }

    private static void buildLakes(NavigableSet<Surface> surfaces, List<List<Surface>> lakes) {
        List<Surface> vertexes = getVertexes(surfaces);
        if (vertexes.size() < 2) {
            return;
        }

        TreeSet<Surface> lakeBounds = new TreeSet<>(new IndexComparator()); //contains 2 highest vertexes
        lakeBounds.add(vertexes.get(0));
        lakeBounds.add(vertexes.get(1));

        lakes.add(new ArrayList<>(surfaces.subSet(lakeBounds.first(), true, lakeBounds.last(), true))); // highest lake with left to right borders

        buildLakes(surfaces.headSet(lakeBounds.first(), true), lakes); // everything on the left of highest lake go recursion DFS. Left will be calculated first.
        buildLakes(surfaces.tailSet(lakeBounds.last(), true), lakes); // everything on the the right of highest lake go recursion.
    }

    private static List<Surface> getVertexes(NavigableSet<Surface> surfaces) {
        List<Surface> vertexes = new ArrayList<>();
        if (surfaces.size() <= 2) {
            return vertexes;
        }
        List<Surface> surfacesArray = new ArrayList<>(surfaces);
        Surface vertex = new Surface(0, -1);// uncountable start
        surfacesArray.add(vertex);// uncountable end

        boolean goingUp = false;
        for (Surface surface : surfacesArray) {
            if (vertex.val < surface.val) {
                goingUp = true;
            } else if (vertex.val == surface.val) {
                //skip plato
            } else if (goingUp) {
                vertexes.add(vertex);
                goingUp = false;
            }
            vertex = surface;
        }
        Collections.sort(vertexes, new ValComparator());
        return vertexes;
    }

    private static List<Lake> calculateLakeData(List<List<Surface>> lakes) {
        List<Lake> calculatedLakes = new ArrayList<>();

        int totalVolume = 0;
        int totalMirror = 0;
        int totalMaxDepth = 0;
        int totalMaxHeight = 0;

        for(List<Surface> lake : lakes){
            int leftBank = lake.get(0).val;
            int rightBank = lake.get(lake.size() -1).val;
            int seaLevel = leftBank < rightBank ? leftBank : rightBank;
            int maxHeight = leftBank < rightBank ? rightBank : leftBank;
            totalMaxHeight = totalMaxHeight < maxHeight ? maxHeight : totalMaxHeight;
            int maxDepth = 0;
            int mirror = 0;
            int volume = 0;
            for (Surface surface : lake) {
                surface.depth = seaLevel > surface.val ? seaLevel - surface.val : 0;
                volume += surface.depth;
                totalVolume += surface.depth;
                if(surface.depth > 0){
                    mirror += 1;
                    totalMirror += 1;
                    if(surface.depth > maxDepth){
                        maxDepth = surface.depth;
                        if(surface.depth > totalMaxDepth){
                            totalMaxDepth = surface.depth;
                        }
                    }
                }
            }
            calculatedLakes.add(new Lake.Builder()
                    .withVolume(volume)
                    .withMirror(mirror)
                    .withSeaLevel(seaLevel)
                    .withMaxDepth(maxDepth)
                    .withMaxHeight(maxHeight)
                    .withAverageArithmeticDepth((float)volume / mirror)
                    .withFormFactor((float)mirror / volume)
                    .withLakeSurface(lake)
                    .build());
        }
        if(!calculatedLakes.isEmpty()){
            calculatedLakes.add(new Lake.Builder()
                    .withVolume(totalVolume)
                    .withMirror(totalMirror)
                    .withMaxDepth(totalMaxDepth)
                    .withMaxHeight(totalMaxHeight)
                    .withAverageArithmeticDepth((float)totalVolume / totalMirror)
                    .withFormFactor((float)totalMirror / totalVolume)
                    .setTotal(true)
                    .build());

        }
        return calculatedLakes;
    }

}