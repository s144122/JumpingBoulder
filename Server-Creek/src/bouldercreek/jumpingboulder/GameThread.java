package bouldercreek.jumpingboulder;


import java.util.concurrent.BlockingQueue;

/**
 * Created by jakob on 07-06-2016.
 */
public class GameThread extends Thread {
    private int client1;
    private int client2;
    private BlockingQueue<byte[]> queue;

    public GameThread(BlockingQueue<byte[]> queue, int firstClient) {
        this.queue = queue;
        client1 = firstClient;
    }

    public void run(){
        while (true) {
            try {
                byte[] data = queue.take();
                //handle the data
            } catch (InterruptedException e) {
                System.err.println("Error occurred:" + e);
            }
        }

    }


}
