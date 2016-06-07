package bouldercreek.jumpingboulder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 1669;
    public static final int HEIGHT = 769;
    public static final int MOVESPEED = -5;
    private MainTread thread;
    private Bagground bg;

    public GamePanel(Context context)
    {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainTread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();

            }catch(InterruptedException e){e.printStackTrace();}
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Bagground(BitmapFactory.decodeResource(getResources(),R.drawable.whitebagground));
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }
    //Rykker baggrunden
    public void update(){
        bg.update();
    }

    @Override
    public void draw(Canvas canvas){
        final float scaleFactor = Math.min( getWidth() / (WIDTH*1.f), getHeight() / (HEIGHT*1.f));
        final int savedState = canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        bg.draw(canvas);
        canvas.restoreToCount(savedState);

    }
}