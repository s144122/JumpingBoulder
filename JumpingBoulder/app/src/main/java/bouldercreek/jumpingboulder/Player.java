package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
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
    public int x;
    public int y;
    public int XL;
    public int XR;
    public int YUp;
    public int YDown;
    private GamePanel gamePanel;


    public Player(Bitmap res, int w, int h, int numFrames,boolean Me,GamePanel gamepanel) {

        if(Me) {
            x = 100;
            y = 388;
        }else {
            x = 400;
            y = 388;
        }
        dy = 0;
        dx = 0;
        height = h;
        width = w;
        XL = x - width/2;
        XR = x + width/2;
        YDown = y - height/2;
        YUp = y + height/2;

        this.gamePanel = gamepanel;

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

        XL = x - width/2-1;
        XR = x + width/2+1;
        YDown = y - height/2;
        YUp = y + height/2;
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
    public Rect getRectangle(){
        return new Rect(x,y, x+width, y+height);
    }

    DisplayMetrics displaymetrics = new DisplayMetrics();
    int Height = displaymetrics.heightPixels;
    int Width = displaymetrics.widthPixels;

    public void collision() {

        System.out.println("playerXL = " + XL);
        System.out.println("playerXR = " + XR);
        System.out.println("opponentXL = " + gamePanel.getOpponent().XL);
        System.out.println("opponentXR = " + gamePanel.getOpponent().XR);
        System.out.println("opponentX = "+ gamePanel.getOpponent().x);
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

        //player collision
        //Rigth side of player
        //System.out.println(inBetween(XR,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL));
        if(inBetween(XR,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)&& (inBetween(YDown,gamePanel.getOpponent().YDown ,gamePanel.getOpponent().YUp)) && (inBetween(YUp,gamePanel.getOpponent().YDown ,gamePanel.getOpponent().YUp))){
            dx = 0;
            //dy = 0;
        }
        //Left side of player
        if(inBetween(XL,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL) && (inBetween(YDown,gamePanel.getOpponent().YDown ,gamePanel.getOpponent().YUp)) && (inBetween(YUp,gamePanel.getOpponent().YDown ,gamePanel.getOpponent().YUp)) ){
            dx = 0;
            //dy = 0;
        }





        //if(x - width/2 == gamePanel.getOpponent().x + gamePanel.getOpponent().width/2 && y < gamePanel.getOpponent().y + gamePanel.getOpponent().height/2 && y > gamePanel.getOpponent().y - gamePanel.getOpponent().height/2){
        //    dx = 0;
            //dy = 0;
        //}
        //Top side of player
        //if(y + height/2 == gamePanel.getOpponent().y - gamePanel.getOpponent().height/2 && x < gamePanel.getOpponent().x + gamePanel.getOpponent().width/2 && x > gamePanel.getOpponent().x - gamePanel.getOpponent().width/2){
            //dx = 0;
        //    dy = 0;
        //}
        //Bottom side of player
        //if(inBetween(YDown,gamePanel.getOpponent().YDown ,gamePanel.getOpponent().YUp) && (inBetween(XL,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)) && (inBetween(XR,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)) ){
        //    dy = 0;
            //dy = 0;
        //}

        //if(inBetween(YUp,gamePanel.getOpponent().YDown ,gamePanel.getOpponent().YUp) && (inBetween(XR,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)) && (inBetween(XR,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)) ){
        //    dy = 0;
        //}
        //if(inBetween(YDown,gamePanel.getOpponent().YUp ,gamePanel.getOpponent().YDown)  && (inBetween(XL,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)) && (inBetween(XR,gamePanel.getOpponent().XR ,gamePanel.getOpponent().XL)) ){
        //    dy = 0;
        //}


        //System.out.println(gamePanel.getOpponent().x);
        //System.out.println(x);;
        //if (y + height/2 >= gamePanel.getOpponent().y + height/2 && x < gamePanel.getOpponent().x - gamePanel.getOpponent().width/2 && x > gamePanel.getOpponent().x + gamePanel.getOpponent().height/2){
        //    System.out.println("true");
        //}
        //System.out.println(y + height/2);
        //System.out.println(gamePanel.getOpponent().y - height/2);
        //System.out.println(y);
        //System.out.println(gamePanel.getOpponent().y);
        //if(y + height/2 == gamePanel.getOpponent().y - gamePanel.getOpponent().height/2 && (x - width/2 < gamePanel.getOpponent().x + gamePanel.getOpponent().height/2 && x - width/2 > gamePanel.getOpponent().x - gamePanel.getOpponent().height/2)&&
        //        (x + width/2 > gamePanel.getOpponent().x + gamePanel.getOpponent().height/2 && x + width/2 > gamePanel.getOpponent().x - gamePanel.getOpponent().height/2) ){
            //dx = 0;
        //    dy = 0;
        //}


    }

    public boolean inBetween(int a, int b1, int b2){
        if((b1 <= a && a <= b2) || (b2 <= a && a <= b1) ){
            return true;
        }
        return false;
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
        collision();
        ;
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
        collision();

    }

}
