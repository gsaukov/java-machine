package com.apps.hackerrank.testdome;

import java.util.*;

public class RoutePlanner {

    public static boolean routeExists(int fromRow, int fromColumn, int toRow, int toColumn,
                                      boolean[][] mapMatrix) {
        P start = new P(fromRow, fromColumn);
        P end = new P(toRow, toColumn);

        Set<P> visited = new HashSet<>();
        visited.add(start);
        Stack<P> moves = new Stack<>();
        moves.push(start);

        while (!moves.isEmpty()) {
            P cell = moves.pop();
            if(cell.equals(end)){
                return true;
            }
            visited.add(cell);
            addMoves(new P(cell.x - 1, cell.y), moves, visited, mapMatrix);
            addMoves(new P(cell.x, cell.y - 1), moves, visited, mapMatrix);
            addMoves(new P(cell.x + 1, cell.y), moves, visited, mapMatrix);
            addMoves(new P(cell.x, cell.y + 1), moves, visited, mapMatrix);
        }
        return false;
    }

    //getMoves
    private static void addMoves(P cell, Stack<P> moves, Set<P> visited, boolean[][] mapMatrix) {
        if(visited.contains(cell)){
            return;
        }
        if (cell.y < 0 || cell.y > mapMatrix[0].length - 1) {
            return;
        }
        if (cell.x < 0 || cell.x > mapMatrix.length - 1) {
            return;
        }
        if(mapMatrix[cell.x][cell.y]){
            moves.push(cell);
        }
    }


    public static void main(String[] args) {
        boolean[][] mapMatrix = {
                {true,  false, false},
                {true,  true,  false},
                {false, true,  true}
        };

        System.out.println(routeExists(0, 0, 2, 2, mapMatrix));
    }

    private static class P {
        int x;
        int y;

        P (int x, int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            P p = (P) o;
            return x == p.x &&
                    y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
