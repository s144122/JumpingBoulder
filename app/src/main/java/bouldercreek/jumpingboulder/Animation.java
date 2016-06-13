package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;

/**
 * Created by Kristian on 08-06-2016.
 */
public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    public void setFrames(Bitmap[] frames){
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }
    public void setDelay(){delay = (long) 10;}
    //public void setFrame(int i){currentFrame = i;}

    public void update(){
        long elapsed = (System.nanoTime()-startTime/100000);

        if (elapsed>delay){
            currentFrame++;
            startTime = System.nanoTime();
        }


        if(currentFrame==frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }
    public Bitmap getImage(){
        return frames[currentFrame];
    }
    public int getCurrentFrame(){return currentFrame;}
    private boolean isPlayedOnce(){return playedOnce;}




}
