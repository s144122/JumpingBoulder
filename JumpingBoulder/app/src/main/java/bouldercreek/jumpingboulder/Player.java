package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

/**
 * Created by Kristian on 07-06-2016.
 */
public class Player extends GameObject {
    private boolean screentouch;
    private boolean screentouchEnd;
    private boolean playing = true;
    private float clickX;
    private float clickY;
    private Animation animation = new Animation();
    private long startTime;
    private int touchDelay;
    private double jumpForce;
    public int x;
    public int y;
    public int XL;
    public int XR;
    public int YUp;
    public int YDown;
    private GamePanel gamePanel;
    public boolean isMe;
    private int lastXL;
    private int lastXR;
    private int lastYDown;
    private int lastYUp;
    private Winscreen winscreen;
    public static Canvas canvas;
    public boolean screen;

    //Variables used for sending data to server
    public boolean isWaiting = true;




    public Player(Bitmap res, int w, int h, int numFrames,boolean Me,GamePanel gamepanel) {
        isMe = Me;
        if(Me) {
            x = 100;
            y = 388;
        }else {
            x = 700;
            y = 388;
        }
        dy = 0;
        dx = 0;
        height = h;
        width = w;
        XL = getXL(x);
        XR = getXR(x);
        YDown = getYDown(y);
        YUp = getYUp(y);

        jumpForce = 0.5;

        lastXL = XL;
        lastXR = XR;
        lastYDown = YDown;
        lastYUp = YUp;

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

    //Updates in every game loop.
    public void update() {
        int lastX = x;
        int lastY = y;

        XL = getXL(x);
        XR = getXR(x);
        YDown = getYDown(y);
        YUp = getYUp(y);

        //KRISTIAN###############################################################################
        //BLIVER DETTE BRUGT?####################################################################
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 100) {
            startTime = System.nanoTime();
        }
        //#######################################################################################
        playerJump();
        playerMovment();
        if(touchDelay > 0){
            touchDelay--;
        }

        XL = getXL(x);
        XR = getXR(x);
        YDown = getYDown(y);
        YUp = getYUp(y);

        lastXL = getXL(lastX);
        lastXR = getXR(lastX);
        lastYDown = getYDown(lastY);
        lastYUp = getYUp(lastY);

        if (isWaiting){
            UDP.readyToPlay();
        }else {
            UDP.sendMove(x, y, dx, dy, gamePanel.getGameTime());
        }

    }
    private int getXL(int X){
        return X - width/2;
    }
    private int getXR(int X){
        return X + width/2;
    }
    private int getYDown(int Y){
        return Y + height/2;
    }
    private int getYUp(int Y){
        return Y - height/2;
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
    public void setScreenTouch(boolean b) {
        screentouch = b;
    }
    public void setScreenTouchEnd(boolean b) {
    screentouchEnd = b;
    }
    public boolean getScreenTouch(){return screentouch;}
    public boolean getScreenTouchEnd(){return screentouchEnd;}


    public Rect getRectangle(){
        return new Rect(x,y, x+width, y+height);
    }

    DisplayMetrics displaymetrics = new DisplayMetrics();
    int Height = displaymetrics.heightPixels;
    int Width = displaymetrics.widthPixels;

    public void collision() {
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

        int OXR = gamePanel.getOpponent(isMe).XR;
        int OXL = gamePanel.getOpponent(isMe).XL;
        int OYDown = gamePanel.getOpponent(isMe).YDown;
        int OYUp = gamePanel.getOpponent(isMe).YUp;
        int OLXR = gamePanel.getOpponent(isMe).lastXR;
        int OLXL = gamePanel.getOpponent(isMe).lastXL;
        int OLYDown = gamePanel.getOpponent(isMe).lastYDown;
        int OLYUp = gamePanel.getOpponent(isMe).lastYUp;

        //player collision
        //Rigth side of player
        if(inBetween(XR,OXR,OXL) && !inBetween(lastXR,OLXL,OLXR) && (inBetween(YUp,OYDown,OYUp) || inBetween(YDown,OYDown,OYUp))){
            x = getXL(gamePanel.getOpponent(isMe).XL)-2;
            dx = dx*(-1);
        }
        //Left side of player
        if(inBetween(XL,OXR,OXL) && !inBetween(lastXL,OLXL,OLXR) && (inBetween(YUp,OYDown,OYUp) || inBetween(YDown,OYDown,OYUp))){
            x = getXR(gamePanel.getOpponent(isMe).XR)+2;
            dx = dx*(-1);
        }

        if(inBetween(YUp,OYDown,OYUp) && !inBetween(lastYUp,OLYUp,OLYDown) && (inBetween(XL,OXL,OXR) || inBetween(XR,OXL,OXR))){
            y = getYDown(gamePanel.getOpponent(isMe).YDown)+1;
            dy = dy*(0);
            System.out.println("Loser");
            setPlaying(false);

        }
        if(inBetween(YDown,OYDown,OYUp) && !inBetween(lastYDown,OLYUp,OLYDown) && (inBetween(XL,OXL,OXR) || inBetween(XR,OXL,OXR)) ){
            y = getYUp(gamePanel.getOpponent(isMe).YUp)-1;
            dy = 0;
            System.out.println("Winner");
            setPlaying(false);
            screen = true;
            winscreen.update();

        }
    }

    public boolean inBetween(int a, int b1, int b2){
        if((b1 <= a && a <= b2) || (b2 <= a && a <= b1) ){
            return true;
        }
        return false;
    }

    //Set the speed of the player jump
    public void playerJump(){
        if (screentouch) {
            if (jumpForce > 1.5) {
                jumpForce = 1.5;

            }else{
                jumpForce = jumpForce + 0.1;

            }
        }
    }

    public void playerMovment() {
        //Touch click
        if (screentouchEnd && touchDelay == 0) {
            //System.out.println("Touch click");
            touchDelay = 15 + (int)(4* (jumpForce));
            if (clickX > x * 2.5) { //GamePanel.WIDTH / 2) {
                dy = -15 * jumpForce;
                dx = 10 * jumpForce;
                setScreenTouchEnd(false);
                jumpForce = 0.5;

            } else if (clickX < x * 2.5) {//GamePanel.WIDTH / 2) {
                dy = -15 * jumpForce;
                dx = -10 * jumpForce;
                setScreenTouchEnd(false);
                jumpForce = 0.5;

            }
        }else if (touchDelay != 0){
            setScreenTouch(false);
        }

        y += dy;
        dy += 1;
        x += dx;

        //Gravity
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
