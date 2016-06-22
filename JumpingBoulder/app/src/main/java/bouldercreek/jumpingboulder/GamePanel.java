package bouldercreek.jumpingboulder;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;



public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1669;
    public static final int HEIGHT = 769;
    private final MainTread thread;
    private Bagground bg;
    private Player player;
    private Player opponent;
    private ArrayList<TopBorder> topBorder;
    private ArrayList<BotBorder> botBorder;

    //Connection variables
    private long gameTime = 0;
    private long gameStartTime = 0;
    private int timeTilStart = Integer.MAX_VALUE;
    private long opponentLastGameTime;

    //For bug fixing
    private int updatesBetweenPrints = 10;


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

        topBorder = new ArrayList<TopBorder>();
        botBorder = new ArrayList<BotBorder>();
        this.updateBottomBorder();
        this.updateTopBorder();


        System.out.println("GamePanel - GamePanel - making new listener");
        new Listener().execute();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("GamePanel - onTouchEvent - click registered on GamePanel: " + this);
        if(timeTilStart == -1) {
//            Intent endGame = new Intent(MainActivity.class);
//            startActivity(endGame);
            thread.setRunning(false);
        }


        player.setScreenTouchEnd(false);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.setScreenTouch(true);
            player.setScreenTouchEnd(false);
            player.setClickX(event.getX());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (player.getScreenTouch() && !player.getScreenTouchEnd()) {
                player.setScreenTouchEnd(true);
                player.setScreenTouch(false);
                return true;
            }

        }
        return super.onTouchEvent(event);
    }


    public void update() {
        if(updatesBetweenPrints == 0) {
            System.out.println("GamePanel - update - gamePanel is updating - timeTilStart: " + timeTilStart);
            updatesBetweenPrints = 10;
        }else{
            updatesBetweenPrints--;
        }
        if (timeTilStart == 0){
            if(gameStartTime == 0){
                gameStartTime = System.currentTimeMillis();
            }
            opponent.update();
            player.update();

            gameTime = System.currentTimeMillis()-gameStartTime;


        } else if(timeTilStart > 0){
            if(timeTilStart > 500){
                UDP.readyToPlay();
            }
            timeTilStart--;
        }

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
        for(BotBorder bb: botBorder){
            bb.draw(canvas);
        }
        for(TopBorder tb: topBorder){
            tb.draw(canvas);
        }

    }


    public void updateBottomBorder(){
        //Create bottom border
        for(int i = 0; i*20 <WIDTH*2; i++){
            if(i == 0){
                botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,HEIGHT+HEIGHT/4));
            }
            else {
                botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,HEIGHT+HEIGHT/4));
            }
        }
    }

    public void updateTopBorder(){
        for(int i = 0; i*20 <WIDTH*2; i++){
            if(i == 0){
                topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,0,50));
            }
            else {
                topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.border),i*20,0,50));
            }
        }
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

    public void endGame(){
        UDP.endGame();
        timeTilStart = -1;
    }

    private class Listener extends AsyncTask<Void, byte[], Void> {


        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("GamePanel - Listener - listener constructed: " + this);
            while (timeTilStart >= 0) {
                try {
                    byte[] data = UDP.receiveData();
                    publishProgress(data);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;

        }


        @Override
        protected void onProgressUpdate(byte[]... values) {
            super.onProgressUpdate(values);
            byte[] data = values[0];
            switch (data[0] & 0b01000000){
                case 0b00000000:
                    System.out.println("GamePanel - Listener - Server says we are not in game");
                    break;
                case 0b01000000 :inGame(data);
                    break;
                default:
                    System.out.println("GamePanel - Listener - onProgressUpdate - could not parse data: " + ByteConversion.printBytes(data));
            }

        }

        private void inGame(byte[] data) {
            switch (data[0] & 0b01110000){
                case 0b01000000: gameCountingDown(data);
                    break;
                case 0b01010000:
                    System.out.println("GamePanel - Listener - inGame - should end the game");
                    timeTilStart = -1;
                    break;
                case 0b01100000: gameRunning(data);
                    break;
                default:
                    System.out.println("GamePanel - Listener - inGame - could not parse data: " + ByteConversion.printBytes(data));
            }
        }

        private void gameCountingDown(byte[] data) {
            System.out.println("GamePanel - Listener - Counting down");
            player.isWaiting = false;
            int time = data[0] & 0b00000011;
            System.out.println("GamePanel - Listener - gameCountingDown - time: " + time);
            if(timeTilStart > 0) {
                player.setPlaying(false);
                //Updates timeTilStart if it's ahead of current timeTilStart
                //since it can never get in front of server, but might recieve a packet way later,
                //it always chooses smalles number
                if(timeTilStart > time * 30) {
                    timeTilStart = time * 30;
                }
                player.x = ByteConversion.convertByteToInt(new byte[]{data[1],data[2],data[3],data[4]});
                player.y = ByteConversion.convertByteToInt(new byte[]{data[5],data[6],data[7],data[8]});


                opponent.x = ByteConversion.convertByteToInt(new byte[]{data[9],data[10],data[11],data[12]});
                opponent.y = ByteConversion.convertByteToInt(new byte[]{data[13],data[14],data[15],data[16]});
            }

        }

        private void gameRunning(byte[] data) {


            long gameTime = ByteConversion.convertByteToLong(ByteConversion.subByte(data, 25,4));

            if(gameTime > opponentLastGameTime) {
                opponent.x = ByteConversion.convertByteToInt(ByteConversion.subByte(data, 1, 4));
                opponent.y = ByteConversion.convertByteToInt(ByteConversion.subByte(data, 5, 4));
                opponent.dx = ByteConversion.convertByteToDouble(ByteConversion.subByte(data, 9, 8));
                opponent.dy = ByteConversion.convertByteToDouble(ByteConversion.subByte(data, 17, 8));
                opponentLastGameTime = gameTime;
            }


        }

    }

}