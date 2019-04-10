package com.apps.lakescalculator.core;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

@Service
public final class LakesCalculator {

    //TODO Go 3d some day.

    // For dividing the surface array on to left, middle, right lakes, TreeSet and its tail/head/subset is better implementation
    // than ArayList.sublist that has concurent modigfication exceptions https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html#subList-int-int-
    // no inclusive split etc.

    public List<Lake> calculate(int[] arr) {
        TreeSet<Surface> surfaces = convert(arr);
        List<List<Surface>> lakes = new ArrayList<>();
        buildLakes(surfaces, lakes);
        return calculateLakeData(lakes);
    }

    private TreeSet<Surface> convert(int[] arr) {
        TreeSet<Surface> surfaces = new TreeSet<>(new IndexComparator());
        for (int i = 0; i < arr.length; i++) {
            surfaces.add(new Surface(arr[i], i));
        }
        return surfaces;
    }

    private void buildLakes(NavigableSet<Surface> surfaces, List<List<Surface>> lakes) {
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

    private List<Surface> getVertexes(NavigableSet<Surface> surfaces) {
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
                continue; //skip plato
            } else if (goingUp) {
                vertexes.add(vertex);
                goingUp = false;
            }
            vertex = surface;
        }
        vertexes.sort(new ValComparator()); // at first i used TreeSet to store vertexes and it was beautiful, but wrong
        return vertexes;                    // since it does not allow duplicates and we may have vertexes of the same height.
    }

    private List<Lake> calculateLakeData(List<List<Surface>> lakes) {
        List<Lake> calculatedLakes = new ArrayList<>();

        int totalVolume = 0;
        int totalMirror = 0;
        int totalMaxDepth = 0;
        int totalMaxHeight = 0;
        int totalMinSeaLevel = Integer.MAX_VALUE;

        for(List<Surface> lake : lakes){
            int leftBank = lake.get(0).val;
            int rightBank = lake.get(lake.size() -1).val;
            int seaLevel = leftBank < rightBank ? leftBank : rightBank;
            int maxHeight = leftBank < rightBank ? rightBank : leftBank;
            totalMaxHeight = totalMaxHeight < maxHeight ? maxHeight : totalMaxHeight;
            totalMinSeaLevel = totalMinSeaLevel > seaLevel ? seaLevel : totalMinSeaLevel;
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
                    .withLakeSurface(lake)
                    .build());
        }
        if(!calculatedLakes.isEmpty()){
            calculatedLakes.add(new Lake.Builder()
                    .withVolume(totalVolume)
                    .withMirror(totalMirror)
                    .withMaxDepth(totalMaxDepth)
                    .withSeaLevel(totalMinSeaLevel)
                    .withMaxHeight(totalMaxHeight)
                    .withAverageArithmeticDepth((float)totalVolume / totalMirror)
                    .setTotal(true)
                    .build());

        }
        return calculatedLakes;
    }
}