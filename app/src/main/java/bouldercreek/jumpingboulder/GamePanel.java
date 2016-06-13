package bouldercreek.jumpingboulder;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1669;
    public static final int HEIGHT = 769;
    private MainTread thread;
    private Bagground bg;
    private Player player;
    private Player opponent;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;

    public GamePanel(Context context) {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainTread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Bagground(BitmapFactory.decodeResource(getResources(), R.drawable.whitebagground));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.jakob), 30, 30, 1);
        opponent = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.kristian),30, 30,1);
        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();
        this.updateBottomBorder();
        this.updateTopBorder();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //System.out.println("getX = " + event.getX());
            //System.out.println("getY = " + event.getY());
            player.screenTouch(true);
            player.setClickY(event.getY());
            player.setClickX(event.getX());

            return true;

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.screenTouch(false);
            return true;
        }

        return super.onTouchEvent(event);

    }


    public void update() {
        if (player.getPlaying()) {
            player.update();
        }
    }


    @Override
    public void draw(Canvas canvas){
        final float scaleFactor = Math.min( getWidth()*2 / (WIDTH*1.f), getHeight()*2 / (HEIGHT*1.f));
        final int savedState = canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        bg.draw(canvas);
        player.draw(canvas);
        opponent.draw(canvas);
        canvas.restoreToCount(savedState);

        //draw borders
        for(BotBorder bb: botborder){
            bb.draw(canvas);
        }
        for(TopBorder tb: topborder){
            tb.draw(canvas);
        }

    }

//    public boolean collision(GameObject a, GameObject b) {
//        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
//            return true;
//        }
//        return false;
//    }

    public void updateBottomBorder(){
        //Create bottom border
        for(int i = 0; i*20 <WIDTH*2; i++){
            if(i == 0){
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,HEIGHT+HEIGHT/4));
            }
            else {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,HEIGHT+HEIGHT/4));
            }
        }
    }

    public void updateTopBorder(){
        for(int i = 0; i*20 <WIDTH*2; i++){
            if(i == 0){
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,0,50));
            }
            else {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,0,50));
            }
        }
    }

}