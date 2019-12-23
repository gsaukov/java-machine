package com.apps.amazon;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Solution3 {
    private static final Pattern POINT = Pattern.compile("(\\d+)([A-Z])");

    public static void main(String[] args) {
        System.out.println(new Solution3().solution(4, "1B 2C,2D 4D", "2B 2D 3D 4D 4A"));

    }

    public String solution(int N, String S, String T) {
        Ship[] ships = Arrays.stream(S.split(",")).map(this::parseShip).toArray(Ship[]::new);
        Point[] points = Arrays.stream(T.split(" ")).map(this::parsePoint).toArray(Point[]::new);
        for (Point point : points) {
            for (Ship ship : ships) {
                ship.hit(point);
            }
        }
        int sink = 0;
        int hit = 0;
        for (Ship ship : ships) {
            if (ship.isSink()) {
                sink++;
            }
            if (ship.isHit()) {
                hit++;
            }
        }

        return sink + "," + hit;
    }

    public Point parsePoint(String s) {
        Matcher matcher = POINT.matcher(s);
        if (matcher.find()) {
            return new Point(Integer.parseInt(matcher.group(1)), matcher.group(2).charAt(0));
        } else {
            throw new RuntimeException("Invalid point: '" + s + "' ");
        }
    }


    public Ship parseShip(String s) {
        String[] points = s.split(" ");
        return new Ship(parsePoint(points[0]), parsePoint(points[1]));
    }

    public static class Ship {
        Point topLeft;
        Point bottomRight;
        int size;
        int hitCount = 0;

        public Ship(Point topLeft, Point bottomRight) {
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
            this.size = (this.bottomRight.column - this.topLeft.column + 1) * (this.bottomRight.row - this.topLeft.row + 1);
        }

        public boolean hit(Point point) {
            if (topLeft.column <= point.column && topLeft.row <= point.row
                    && point.column <= bottomRight.column && point.row <= bottomRight.row) {
                hitCount++;
                return true;
            } else {
                return false;
            }
        }

        public boolean isSink() {
            return hitCount >= size;
        }

        public boolean isHit() {
            return hitCount > 0 && hitCount < size;
        }
    }


    public static class Point {
        int row;
        char column;

        Point(int row, char column) {
            this.row = row;
            this.column = column;
        }

    }


}

