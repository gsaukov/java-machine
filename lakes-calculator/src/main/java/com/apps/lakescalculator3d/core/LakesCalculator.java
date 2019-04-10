package com.apps.lakescalculator3d.core;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

@Service
public final class LakesCalculator {

    public void calculate(List<Surface> arr, boolean isX) {
        TreeSet<Surface> surfaces = new TreeSet<>(new IndexComparator());
        surfaces.addAll(arr);
        List<List<Surface>> lakes = new ArrayList<>();
        buildLakes(surfaces, lakes);
        calculateLakeData(lakes, isX);
    }

    private void buildLakes(NavigableSet<Surface> surfaces, List<List<Surface>> lakes) {
        List<Surface> vertexes = getVertexes(surfaces);
        if (vertexes.size() < 2) {
            return;
        }

        TreeSet<Surface> lakeBounds = new TreeSet<>(new IndexComparator());
        lakeBounds.add(vertexes.get(0));
        lakeBounds.add(vertexes.get(1));

        lakes.add(new ArrayList<>(surfaces.subSet(lakeBounds.first(), true, lakeBounds.last(), true)));

        buildLakes(surfaces.headSet(lakeBounds.first(), true), lakes);
        buildLakes(surfaces.tailSet(lakeBounds.last(), true), lakes);
    }

    private List<Surface> getVertexes(NavigableSet<Surface> surfaces) {
        List<Surface> vertexes = new ArrayList<>();
        if (surfaces.size() <= 2) {
            return vertexes;
        }
        List<Surface> surfacesArray = new ArrayList<>(surfaces);
        Surface vertex = new Surface(0, -1, -1);
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
        vertexes.sort(new ValComparator());
        return vertexes;
    }

    private void calculateLakeData(List<List<Surface>> lakes, boolean isX) {
        for(List<Surface> lake : lakes){
            int leftBank = lake.get(0).val;
            int rightBank = lake.get(lake.size() -1).val;
            int seaLevel = leftBank < rightBank ? leftBank : rightBank;
            for (Surface surface : lake) {
                int depth = seaLevel > surface.val ? seaLevel - surface.val : 0;
                if(isX){
                    surface.depthX = depth;
                } else {
                    surface.depthY = depth;
                }
            }
        }
    }
}