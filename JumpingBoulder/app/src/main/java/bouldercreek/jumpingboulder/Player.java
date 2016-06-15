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
    public boolean isMe;
    private int lastXL;
    private int lastXR;
    private int lastYDown;
    private int lastYUp;


    public Player(Bitmap res, int w, int h, int numFrames,boolean Me,GamePanel gamepanel) {
        isMe = Me;
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
        XL = getXL(x);
        XR = getXR(x);
        YDown = getYDown(y);
        YUp = getYUp(y);

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

    public void update() {
        int lastX = x;
        int lastY = y;

        XL = getXL(x);
        XR = getXR(x);
        YDown = getYDown(y);
        YUp = getYUp(y);

        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed > 100) {
            startTime = System.nanoTime();
        }
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
        System.out.println("player YUp: " + YUp);
        System.out.println("player YDown: " + YDown);

        System.out.println("player lastXL: " + lastXL);
        System.out.println("player lastXR: " + lastXR);
        System.out.println("player lastYUp: " + lastYUp);
        System.out.println("player lastYDown: " + lastYDown);


        System.out.println("opponentXL = " + gamePanel.getOpponent(isMe).XL);
        System.out.println("opponentXR = " + gamePanel.getOpponent(isMe).XR);
        System.out.println("opponentYUp = " + gamePanel.getOpponent(isMe).YUp);
        System.out.println("opponentYDown = " + gamePanel.getOpponent(isMe).YDown);

        System.out.println("opponent lastXL = " + gamePanel.getOpponent(isMe).lastXL);
        System.out.println("opponent lastXR = " + gamePanel.getOpponent(isMe).lastXR);
        System.out.println("opponent lastYUp = " + gamePanel.getOpponent(isMe).lastYUp);
        System.out.println("opponent lastYDown = " + gamePanel.getOpponent(isMe).lastYDown);
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
            System.out.println("Player right side jumped into opponent, current x:"+ x);
            x = getXL(gamePanel.getOpponent(isMe).XL)-2;
            System.out.println("Player x-speed pre collission: "+dx);
            dx = dx*(-1);
            System.out.println("Player x-speed post collission: "+dx);
            System.out.println("Player right side jumped into opponent, current x:"+ x);
        }
        //Left side of player
        if(inBetween(XL,OXR,OXL) && !inBetween(lastXL,OLXL,OLXR) && (inBetween(YUp,OYDown,OYUp) || inBetween(YDown,OYDown,OYUp))){
            System.out.println("Player left side jumped into opponent, current x:"+ x);
            x = getXR(gamePanel.getOpponent(isMe).XR)+2;
            System.out.println("Player x-speed pre collission: "+dx);
            dx = dx*(-1);
            System.out.println("Player x-speed post collission: "+dx);
            System.out.println("Player left side jumped into opponent, current x:"+ x);
        }

        if(inBetween(YUp,OYDown,OYUp) && !inBetween(lastYUp,OLYUp,OLYDown) && (inBetween(XL,OXL,OXR) || inBetween(XR,OXL,OXR))){
            y = getYDown(gamePanel.getOpponent(isMe).YDown)+1;
            dy = dy*(0);

        }
        if(inBetween(YDown,OYDown,OYUp) && !inBetween(lastYDown,OLYUp,OLYDown) && (inBetween(XL,OXL,OXR) || inBetween(XR,OXL,OXR)) ){
            y = getYUp(gamePanel.getOpponent(isMe).YUp)-1;
            dy = 0;
            System.out.println("Player came from top, current y: " +y);
            setPlaying(false);
            System.out.println("Game STOP");
        }
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
        //collision();
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
