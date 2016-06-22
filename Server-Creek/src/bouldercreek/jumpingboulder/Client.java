package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

/**
 * Created by jakob on 17-06-2016.
 */
public class Client {
    public GameThread game;
    private BlockingQueue<byte[]> queue;
    private int clientId;
    private byte[] lastRecievedData = new byte[Main.packetSize];
    private int port;
    private InetAddress ip;
    private int timeSinceLastMove = 0;


    public Client(int clientId, InetAddress address, int port) {
        this.clientId = clientId;
        this.port = port;
        this.ip = address;
    }

    public void act(byte[] data){
        //Checker om den længst til venstre bit er 1 eller 0, i d. 5. byte i arrayet
        switch (data[4] & 0b01000000) {
            case 0b00000000: notInGame(data);
                break;
            case 0b01000000: inGame(data);
                break;
        }
        lastRecievedData = data;
    }


    private void notInGame(byte[] data){
        switch (data[4] & 0b00100000){
            case 0b00100000:
                if(Main.waitingClient == null || !Main.waitingClient.equals(clientId)) {
                    new readyToPlay();
                    Main.newQuickGame(this);
                }
                break;
        }
    }



    private void inGame(byte[] data) {
        switch (data[4] & 0b00110000){
            case 0b00100000:
                queue.add(data);
                //System.out.println("Client - inGame - gameThread: "+game + " queue: "+ queue);
                break;
            case 0b00010000: gameEnded();
                break;
            case 0b00110000: readyToPlay.interrupted();
                break;
        }
    }



    private void gameEnded() {
        game.client1.sendData(new byte[]{0b01010000});
        game.client2.sendData(new byte[]{0b01010000});

        System.out.println("Client - gameEnded - game: " + game + " ended");
        game.interrupt();
        game = null;
        queue = null;
    }

    public void setQueue(BlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    public class readyToPlay extends Thread{
        //This Thread removes waiting client, if the client stops sending a ready signal

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(500);
                    Main.waitingClient = null;

                    System.out.println("Client: " + this + " left waiting room   - waitingclient should be null: " + Main.waitingClient);
                    break;
                } catch (InterruptedException e) {
                    if (!game.equals(null)){
                        break;
                    }
                }
            }
        }
    }

    public void sendData(byte[] data) {
        //System.out.println("Client - sendData - sending data to client: " + ByteConversion.printBytes(data));
        try {
            Main.sendData(data, ip, port);
        } catch (IOException e) {
            System.out.println("Client - sendData - Client: " + this + " could not send data through socket: "+ Main.socket);
            e.printStackTrace();
        }
    }


    public int getClientId() {
        return clientId;
    }

    public boolean equals(Client client){
        return this.clientId == client.clientId;
    }
}
