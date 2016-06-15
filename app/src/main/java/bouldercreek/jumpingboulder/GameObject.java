package bouldercreek.jumpingboulder;

import android.graphics.Rect;

/**
 * Created by Kristian on 07-06-2016.
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    protected double dx;
    protected double dy;
    protected int width;
    protected int height;


    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }


}
