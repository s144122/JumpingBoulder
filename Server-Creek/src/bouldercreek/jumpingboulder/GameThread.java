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
        System.out.println("GameThread - run - New game has started between: "
                + client1.getClientId()
                + " and " + client2.getClientId());
        byte[] cmdData = {0b01000000};
        byte[] player1DataX = ByteConversion.convertToByte(100);
        byte[] player1DataY = ByteConversion.convertToByte(388);

        byte[] player2DataX = ByteConversion.convertToByte(700);
        byte[] player2DataY = ByteConversion.convertToByte(388);

        for (byte i=3 ; i>=0; i--){
            client1.sendData(ByteConversion.combine(cmdData,player1DataX,player1DataY,player2DataX,player2DataY));
            client2.sendData(ByteConversion.combine(cmdData,player2DataX,player2DataY,player1DataX,player1DataY));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            //System.out.println("GameThread - run - making thread killer");
            Thread killer = new threadKiller(this);
            killer.start();
            //System.out.println("GameThread - run - kiler made, waiting for data");

            try {
                //System.out.println("GameThread - run - thread ready to recieve moves");
                byte[] data = queue.take();
                System.out.println("GameThread - run - recieved data with: "+data.length+" length");
                //System.out.println("GameThread - run - took data from queue");
                killer.interrupt();
                int id = ByteConversion.convertByteToInt(new byte[]{data[0], data[1], data[2], data[3]});
                System.out.println("GameThread - run - " + id);
                if (data.length<8){
                    byte[] data2 = new byte[8];
                    for (int i=0 ; i<data.length; i++){
                        data2[i] = data[i];
                    }
                    data = data2;
                }

                //This loop removes the clientID from the data
                for (int i=1; i<data.length-3; i++){
                    data[i] = data[i+3];
                    data[i+3] = 0;
                }
                //System.out.println("GameThread - run - sending move to client("+id+") : " + ByteConversion.printBytes(data));
                //This sends the data to the client who did not send it
                if( id == client1.getClientId()){
                    client2.sendData(data);
                }else if(id == client2.getClientId()){
                    client1.sendData(data);
                }
            } catch (InterruptedException e) {
                System.out.println("GameThread - run - Game: " + this + " stopped");
                return;
            }
        }

    }


    private class threadKiller extends Thread{
        private final GameThread game;
        //This thread will interupt the game thread, if no data is sent to the queue in 1 minute

        public threadKiller(GameThread game){
            this.game = game;

        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(60000);
                System.out.println("GameThread - threadKiller - No game move happened in 1 minute, game stopped: " + game);
                game.interrupt();
            } catch (InterruptedException e) {

            }
        }
    }


}
