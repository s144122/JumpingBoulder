package bouldercreek.jumpingboulder;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainTread extends Thread
{
    private final SurfaceHolder surfaceHolder;
    private final GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;
    private long timeMillis;

    public MainTread(SurfaceHolder surfaceHolder, GamePanel gamePanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }
    @Override
    public void run()
    {
        long startTime;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        int FPS = 30;
        long targetTime = 1000/ FPS;


        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception ignored) {
            }
            finally{
                if(canvas!=null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                sleep(waitTime);
            }catch(Exception ignored){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS)
            {
                double averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount =0;
                totalTime = 0;
                System.out.println("MainThread - run - FPS = " + averageFPS + " on MainThread: " + this);
            }
        }
    }
    public void setRunning(boolean b){
        running=b;
    }


    public void closeThread(){
        Thread.interrupted();
    }
}
