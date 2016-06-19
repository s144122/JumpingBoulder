package bouldercreek.jumpingboulder;


import java.util.concurrent.BlockingQueue;

/**
 * Created by jakob on 07-06-2016.
 */
public class GameThread extends Thread {
    private Client client1;
    private Client client2;
    private BlockingQueue<byte[]> queue;

    public GameThread(BlockingQueue<byte[]> queue, Client firstClient) {
        this.queue = queue;
        client1 = firstClient;
    }

    public void run(){

        while (true) {

            Thread killer = new threadKiller(this);

            try {
                byte[] data = queue.take();
                killer.interrupt();
                int id = ByteConversion.convertByteToInt(new byte[]{data[0], data[1], data[2], data[3]});
                if( id == client1.getClientId()){
                    client2.sendDate(data);
                }else if(id == client2.getClientId()){
                    client1.sendDate(data);
                }
            } catch (InterruptedException e) {
                System.out.println("GameThread - run - Game: " + this + " stopped");
            }
        }

    }

    private class threadKiller extends Thread{
        //This thread will interupt the game thread, if no data is sent to the queue in 10 minutes

        public threadKiller(GameThread game){
            try {
                Thread.sleep(600000);
                System.out.println("GameThread - threadKiller - No game move happened in 10 minutes, game stopped: " + game);
                game.interrupt();
            } catch (InterruptedException e) {

            }

        }
    }


}
