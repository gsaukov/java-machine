package com.apps.lakescalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class LakesCalculator {

    //TODO calculate depth, mirror, volume. Go 3d some day.

    public static void main(String[] args) {
        int[] arr = {2, 1, 2, 3, 4, 3, 3, 5, 5, 2, 3, 2, 7, 3, 5};
        TreeSet<Surface> surfaces = convert(arr);
        List<List<Surface>> lakes = new ArrayList<>();
        buildLake(surfaces, lakes);
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
        TreeSet<Surface> lakeBounds = new TreeSet<>(new IndexComparator());
        lakeBounds.add(vertexes.pollFirst());
        lakeBounds.add(vertexes.pollFirst());

        lakes.add(new ArrayList<>(surfaces.subSet(lakeBounds.first(), true, lakeBounds.last(), true)));

        buildLake(surfaces.headSet(lakeBounds.first(), true), lakes);
        buildLake(surfaces.tailSet(lakeBounds.last(), true), lakes);
    }

    private static NavigableSet<Surface> getVertexes(NavigableSet<Surface> surfaces) {
        TreeSet<Surface> vertexes = new TreeSet<>(new ValComparator());
        if (surfaces.size() <= 2) {
            return vertexes;
        }
        List<Surface> surfacesArray = new ArrayList<>(surfaces);
        Surface vertex = new Surface(0, -1);

        for (int i = 0; i < surfacesArray.size(); i++) {
            Surface surface = surfacesArray.get(i);
            if (vertex.val <= surface.val) {
                vertex = surface;
            } else {
                vertexes.add(vertex);
                vertex = surface;
            }

            if(i == surfacesArray.size() - 1 && surface.val >= surfacesArray.get(i - 1).val) {
                vertexes.add(vertex);
            }
        }
        return vertexes;
    }

}