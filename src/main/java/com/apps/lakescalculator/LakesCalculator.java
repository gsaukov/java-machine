package com.apps.lakescalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class LakesCalculator {

    //TODO calculate depth, mirror, volume. Go 3d some day.

    public static List<List<Surface>> calculate(int[] arr) {
        TreeSet<Surface> surfaces = convert(arr);
        List<List<Surface>> lakes = new ArrayList<>();
        buildLake(surfaces, lakes);
        return lakes;
    }

    private static TreeSet<Surface> convert(int[] arr) {
        TreeSet<Surface> surfaces = new TreeSet<>(new IndexComparator());
        for (int i = 0; i < arr.length; i++) {
            surfaces.add(new Surface(arr[i], i));
        }
        return surfaces;
    }

    private static void buildLake(NavigableSet<Surface> surfaces, List<List<Surface>> lakes) {
        NavigableSet<Surface> vertexes = getVertexes(surfaces);
        if (vertexes.size() < 2) {
            return;
        }

        TreeSet<Surface> lakeBounds = new TreeSet<>(new IndexComparator()); //contains 2 highest vertexes
        lakeBounds.add(vertexes.pollFirst());
        lakeBounds.add(vertexes.pollFirst());

        lakes.add(new ArrayList<>(surfaces.subSet(lakeBounds.first(), true, lakeBounds.last(), true))); // highest lake with left to right borders

        buildLake(surfaces.headSet(lakeBounds.first(), true), lakes); // everything on the left of highest lake go recursion DFS. Left will be calculated first.
        buildLake(surfaces.tailSet(lakeBounds.last(), true), lakes); // everything on the the right of highest lake go recursion.
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

}