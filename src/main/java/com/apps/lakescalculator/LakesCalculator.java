package com.apps.lakescalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

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
        NavigableSet<Surface> vertexes = getVertexes(surfaces);
        if (vertexes.size() < 2) {
            return;
        }

        TreeSet<Surface> lakeBounds = new TreeSet<>(new IndexComparator()); //contains 2 highest vertexes
        lakeBounds.add(vertexes.pollFirst());
        lakeBounds.add(vertexes.pollFirst());

        lakes.add(new ArrayList<>(surfaces.subSet(lakeBounds.first(), true, lakeBounds.last(), true))); // highest lake with left to right borders

        buildLakes(surfaces.headSet(lakeBounds.first(), true), lakes); // everything on the left of highest lake go recursion DFS. Left will be calculated first.
        buildLakes(surfaces.tailSet(lakeBounds.last(), true), lakes); // everything on the the right of highest lake go recursion.
    }

    private static NavigableSet<Surface> getVertexes(NavigableSet<Surface> surfaces) {
        TreeSet<Surface> vertexes = new TreeSet<>(new ValComparator());
        if (surfaces.size() <= 2) {
            return vertexes;
        }
        List<Surface> surfacesArray = new ArrayList<>(surfaces);
        Surface vertex = new Surface(0, -1);// uncountable start
        surfacesArray.add(vertex);// uncountable end

        boolean goingUp = true;
        for (int i = 0; i < surfacesArray.size(); i++) {
            Surface surface = surfacesArray.get(i);
            if (vertex.val <= surface.val) {
                goingUp = true;
            } else if (goingUp) { //this will produce dry lakes on downhill ex: {2, 1, 1} will be removed during depth calculation.
                vertexes.add(vertex);
                goingUp = false;
            }
            vertex = surface;
        }
        return vertexes;
    }

    private static List<Lake> calculateLakeData(List<List<Surface>> lakes) {
        List<Lake> calculatedLakes = new ArrayList<>();

        int aggregatedVolume = 0;
        int aggregatedMirror = 0;
        int overallMaxDepth = 0;
        int overallMaxHeight = 0;

        for(List<Surface> lake : lakes){
            int leftBank = lake.get(0).val;
            int rightBank = lake.get(lake.size() -1).val;
            int seaLevel = leftBank < rightBank ? leftBank : rightBank;
            int maxHeight = leftBank < rightBank ? rightBank : leftBank;
            int maxDepth = 0;
            int mirror = 0;
            int volume = 0;
            for (Surface surface : lake) {
                surface.depth = seaLevel > surface.val ? seaLevel - surface.val : 0;
                volume += surface.depth;
                aggregatedVolume += surface.depth;
                if(surface.depth > 0){
                    mirror += 1;
                    aggregatedMirror += 1;
                    if(surface.depth > maxDepth){
                        maxDepth = surface.depth;
                        if(surface.depth > overallMaxDepth){
                            overallMaxDepth = surface.depth;
                        }
                    }
                }
                if(overallMaxHeight < maxHeight){
                    overallMaxHeight = maxHeight;
                }
            }
            if(volume > 0){// bye bye right downhill dry lake
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
        }
        if(!calculatedLakes.isEmpty()){
            calculatedLakes.add(new Lake.Builder()
                    .withVolume(aggregatedVolume)
                    .withMirror(aggregatedMirror)
                    .withMaxDepth(overallMaxDepth)
                    .withMaxHeight(overallMaxHeight)
                    .withAverageArithmeticDepth((float)aggregatedVolume / aggregatedMirror)
                    .withFormFactor((float)aggregatedMirror / aggregatedVolume)
                    .setTotal(true)
                    .build());

        }
        return calculatedLakes;
    }

}