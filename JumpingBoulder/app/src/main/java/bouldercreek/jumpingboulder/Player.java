package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Kristian on 07-06-2016.
 */
public class Player extends GameObject{

    private Bitmap spritesheet;
    private int score;
    private double dya;
    private double dxa;
    private boolean screentoch;
    private boolean playing;
    private float clickX;
    private float clickY;
    private Animation animation = new Animation();
    private long startTime;


    public Player(Bitmap res, int w, int h, int numFrames){
        x = 100;
        y = 400;


        //GamePanel.HEIGHT/2;
        dy = 0;
        dx = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet,i*width , 0, width,height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }


    public void screenTouch(boolean b){
        screentoch = b;}

    public void getClickX(float f){clickX = f;}
    public void getClickY(float f){clickY = f;}




    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100){
            score++;
            startTime = System.nanoTime();
        }

        if (screentoch) {
            if (clickX > GamePanel.WIDTH / 2) {
                dy = (int) (dya -= 0.5);
                dx = (int) (dxa += 1);

            } else if (clickX < GamePanel.WIDTH / 2) {
                dy = (int) (dya -= 0.5);
                dx = (int) (dxa -= 1);
            }

        }
        else {
            dy = (int) (dya += 0.1);
        }
//        if(dy > 14){dy = 14;}
//        if(dy < -14){dy = -14;}
//        if(dx > 14){dx = 14;}
//        if(dx < -14){dx = 14;}

        y += dy;
        dy = 0;
        x += dx;
        dx = 0;

        if (y > 400){
            dy = 0;
            dya = -1;

        }
        if (y < 24){
            dy = 0;
            dya = 1;

        }
        if (x > 810){
            dx = 0;
            dxa = -1;
        }

        if (x < 5){
            dx = 0;
            dxa = 1;
        }

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}
}
