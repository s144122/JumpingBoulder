package bouldercreek.jumpingboulder;


import java.util.concurrent.BlockingQueue;

/**
 * Created by jakob on 07-06-2016.
 */
public class GameThread extends Thread {
    private Client client1;
    private Client client2;
    private BlockingQueue<byte[]> queue;

    public GameThread(BlockingQueue<byte[]> queue, Client firstClient, Client secondClient) {
        this.queue = queue;
        client1 = firstClient;
        client2 = secondClient;
    }

    public void run(){
        byte[] cmdData = {0b01010000};
        byte[] player1DataX = ByteConversion.convertToByte(100);
        byte[] player1DataY = ByteConversion.convertToByte(388);

        byte[] player2DataX = ByteConversion.convertToByte(700);
        byte[] player2DataY = ByteConversion.convertToByte(388);

        for (byte i=3 ; i>=0; i--){
            client1.sendData(new byte[]{(byte) (cmdData[0]+i),
                    player1DataX[0], player1DataX[1], player1DataX[2], player1DataX[3],
                    player1DataY[0], player1DataY[1], player1DataY[2], player1DataY[3]});
            client2.sendData(new byte[]{(byte) (cmdData[0]+i),
                    player2DataX[0], player2DataX[1], player2DataX[2], player2DataX[3],
                    player2DataY[0], player2DataY[1], player2DataY[2], player2DataY[3]});
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {

            Thread killer = new threadKiller(this);

            try {
                byte[] data = queue.take();
                killer.interrupt();
                int id = ByteConversion.convertByteToInt(new byte[]{data[0], data[1], data[2], data[3]});
                if (data.length<8){
                    byte[] data2 = new byte[8];
                    for (int i=0 ; i<data.length; i++){
                        data2[i] = data[i];
                    }
                    data = data2;
                }
                data[0] = 0b01100000;
                for (int i=1; i<data.length-3; i++){
                    data[i] = data[i+3];
                    data[i+3] = 0;
                }
                if( id == client1.getClientId()){
                    client2.sendData(data);
                }else if(id == client2.getClientId()){
                    client1.sendData(data);
                }
            } catch (InterruptedException e) {
                System.out.println("GameThread - run - Game: " + this + " stopped");
            }
        }

    }

    public void add(Client client) {
        client2 = client;
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
