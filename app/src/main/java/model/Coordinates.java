package model;

/**
 * Created by sapi9 on 04/07/2017.
 */

public class Coordinates {
    private int top;
    private int left;
    private int bottom;
    private int right;

    private float x;
    private float y;

    public Coordinates(/*int top, int left, int bottom, int right*/ float x, float y) {
       /* this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;*/
       this.x = x;
        this.y=y;
    }

    public int getTop() {
        return this.top;
    }

    public int getBottom() {
        return this.bottom;
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.right;
    }

    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
}
