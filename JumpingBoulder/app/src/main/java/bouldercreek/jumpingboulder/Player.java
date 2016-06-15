package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

/**
 * Created by Kristian on 07-06-2016.
 */
public class Player extends GameObject {
    private boolean screentoch;
    private boolean playing = true;
    private float clickX;
    private float clickY;
    private Animation animation = new Animation();
    private long startTime;
    private int touchDelay;


    public Player(Bitmap res, int w, int h, int numFrames) {
        x = 100;
        y = 400;

        dy = 0;
        dx = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        Bitmap spritesheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay();
        startTime = System.nanoTime();
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 100) {
            startTime = System.nanoTime();
        }
        playerMovment();
        if(touchDelay > 0){
            touchDelay--;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public boolean getPlaying() {
        return playing;
    }
    public void setPlaying(boolean b) {
        playing = b;
    }
    public void setClickX(float f) {
        clickX = f;
    }
    public void setClickY(float f) {
        clickY = f;
    }
    public void screenTouch(boolean b) {
        screentoch = b;
    }

    DisplayMetrics displaymetrics = new DisplayMetrics();
    int Height = displaymetrics.heightPixels;
    int Width = displaymetrics.widthPixels;

    public void borders() {
        //Bottom border
        if (y >= 388 && dy>0) {
            y = 388;
            dy = 0;
            dx = dx * 0.5;
        }
        //Top border
        if (y < 24) {
            y = 24;
            dy = 0;
            touchDelay = 25;
        }
        //Rigth border
        if (x > 805) {
            x = 805;
            dx = dx *-0.5;
        }
        //Left border
        if (x < 5) {
            x = 5;
            dx = dx * -0.5;
        }
    }

    public void playerMovment() {

        //Touch click
        if (screentoch && touchDelay == 0) {
            touchDelay = 15;
            if (clickX > x * 2.5) { //GamePanel.WIDTH / 2) {
                dy = -15;
                dx = 10;

            } else if (clickX < x * 2.5) {//GamePanel.WIDTH / 2) {
                dy = -15;
                dx = -10;
            }
        }
        borders();
        y += dy;
        dy += 1;
        x += dx;

        //Gravity
        //dy = (int) (dya += 0.1);
        dy += 0.1;

        //Deceleration in x
        if (dx > 0) {
            dx -= 0.2;
        }
        if (dx < 0) {
            dx += 0.2;

        }
        borders();

    }

}
