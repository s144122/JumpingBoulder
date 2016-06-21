package bouldercreek.jumpingboulder;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivity;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1669;
    public static final int HEIGHT = 769;
    private MainTread thread;
    private Bagground bg;
    private Player player;
    private Player opponent;
    private Winscreen winscreen;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;

    //Connection variables
    private long gameTime = 0;
    private long gameStartTime = 0;
    private int timeTillStart = Integer.MAX_VALUE;
    private long opponentLastGameTime;


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

        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.kristian), 30, 30, 1,true,this);
        opponent = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.jakob),30, 30,1,false,this);

        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();
        this.updateBottomBorder();
        this.updateTopBorder();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    public void winScreen(Canvas canvas){
        super.draw(canvas);
        winscreen = new Winscreen(BitmapFactory.decodeResource(getResources(), R.drawable.winscreen),593,150);
        winscreen.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //player.setScreenTouch(false);
        player.setScreenTouchEnd(false);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //System.out.println("Aktion Down = true");
            player.setScreenTouch(true);
            player.setScreenTouchEnd(false);
            player.setClickY(event.getY());
            player.setClickX(event.getX());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (player.getScreenTouch() && !player.getScreenTouchEnd()) {
                //System.out.println("Aktion UP = true");
                player.setScreenTouchEnd(true);
                player.setScreenTouch(false);
                return true;
            }

        }
        return super.onTouchEvent(event);
    }


    public void update() {
        if (timeTillStart == 0){
            if(gameStartTime == 0){
                gameStartTime = System.currentTimeMillis();
            }
            opponent.update();
            player.update();

            this.updateWinScreen();
            gameTime = System.currentTimeMillis()-gameStartTime;


        } else {
            timeTillStart--;
        }
        if (player.getPlaying()) {
            player.update();

        }

    }
    public void movementOpponent(float x, float y,int time){
        opponent.setClickX(x);
        opponent.setClickY(y);
    }


    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        final float scaleFactor = Math.min( getWidth()*2 / (WIDTH*1.f), getHeight()*2 / (HEIGHT*1.f));
        final int savedState = canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        bg.draw(canvas);
        player.draw(canvas);
        opponent.draw(canvas);
        canvas.restoreToCount(savedState);

        //draw collision
        for(BotBorder bb: botborder){
            bb.draw(canvas);
        }
        for(TopBorder tb: topborder){
            tb.draw(canvas);
        }

    }


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
    public void updateWinScreen(){
        new Winscreen(BitmapFactory.decodeResource(getResources(),R.drawable.winscreen),593,150);
    }

    public Player getOpponent(boolean isPlayer) {
        if(isPlayer) {
            return opponent;
        }
        return player;
    }

    public long getGameTime() {
        return gameTime;
    }


    private class Listener extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                byte[] data = UDP.receiveData();
                switch (data[0] & 0b01000000){
                    case 0b00000000:
                        System.out.println("GamePanel - Listener - Server says we are not in game");
                        return null;
                    case 0b01000000 :inGame(data);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


            return null;
        }

        private void inGame(byte[] data) {
            switch (data[0] & 0b00100000){
                case 0b00000000: gameCountingDown(data);
                    break;
                case 0b00100000: gameRunning(data);
                    break;
            }
        }

        private void gameCountingDown(byte[] data) {
            int time = data[0] & 0b00000011;
            if(timeTillStart >0) {
                player.setPlaying(false);
                //Updates timeTillStart if it's ahead of current timeTillStart
                //since it can never get in front of server, but might recieve a packet way later,
                //it always chooses smalles number
                if(timeTillStart > time * 30) {
                    timeTillStart = time * 30;
                }
                player.x = ByteConversion.convertByteToInt(new byte[]{data[1],data[2],data[3],data[4]});
                player.y = ByteConversion.convertByteToInt(new byte[]{data[5],data[6],data[7],data[8]});


                opponent.x = ByteConversion.convertByteToInt(new byte[]{data[9],data[10],data[11],data[12]});
                opponent.y = ByteConversion.convertByteToInt(new byte[]{data[13],data[14],data[15],data[16]});
            }

        }

        /*
        public void newGame(){
            Intent newgame;
            newgame = new Intent(GameActivity.class,MainActivity.class);
            startActivity(newgameame);
        }
        */
        private void gameRunning(byte[] data) {


            long gameTime = ByteConversion.convertByteToLong(ByteConversion.subByte(data, 25,33));

            if(gameTime > opponentLastGameTime) {
                opponent.x = ByteConversion.convertByteToInt(ByteConversion.subByte(data, 1, 5));
                opponent.y = ByteConversion.convertByteToInt(ByteConversion.subByte(data, 5, 9));
                opponent.dx = ByteConversion.convertByteToDouble(ByteConversion.subByte(data, 9, 17));
                opponent.dy = ByteConversion.convertByteToDouble(ByteConversion.subByte(data, 17, 25));
                opponentLastGameTime = gameTime;
            }


        }
    }

}