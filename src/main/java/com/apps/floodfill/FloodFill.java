package com.apps.floodfill;

public class FloodFill {

    public static int VOID = -1;
    private int newColor = VOID;

    public void fill(int[][] picture, int x, int y, int colorToReplace, int colorToPaint) {
        int currentColor = getValueAt(picture, x, y);

        if (currentColor == VOID){
            return;
        }

        if (currentColor == colorToReplace) {
            picture[x][y] = colorToPaint;
            fill(picture, x + 1, y, colorToReplace, colorToPaint);
            fill(picture, x - 1, y, colorToReplace, colorToPaint);
            fill(picture, x, y + 1, colorToReplace, colorToPaint);
            fill(picture, x, y - 1, colorToReplace, colorToPaint);
        } else if(currentColor != colorToPaint && newColor == VOID) {
            newColor = currentColor;
        }
    }

    public int nextColorToPaint(){
        int nextColorToPaint = newColor;
        newColor = VOID;
        return nextColorToPaint;
    }

    private int getValueAt(int[][] picture, int x, int y) {
        if (x < 0 || y < 0 || x >= picture.length || y >= picture[x].length) {
            return VOID;
        } else {
            return picture[x][y];
        }
    }
}
