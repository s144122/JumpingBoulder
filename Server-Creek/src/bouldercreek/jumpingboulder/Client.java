package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;


public class Client {
    public GameThread game;
    private BlockingQueue<byte[]> queue;
    private final int clientId;
    private final int port;
    private final InetAddress ip;


    public Client(int clientId, InetAddress address, int port) {
        this.clientId = clientId;
        this.port = port;
        this.ip = address;
    }

    public void act(byte[] data){
        switch (data[4] & 0b01000000) {
            case 0b00000000: notInGame(data);
                break;
            case 0b01000000: inGame(data);
                break;
            default:
                System.out.println("Client - act - could not parse data: " + ByteConversion.printBytes(data));
        }
        byte[] lastRecievedData = data;
    }


    private void notInGame(byte[] data){
        switch (data[4] & 0b00100000){
            case 0b00100000:
                if(Main.waitingClient == null || Main.waitingClient.getClientId() != clientId) {
                    new readyToPlay().start();
                    Main.newQuickGame(this);
                }
                break;
            default:
                System.out.println("Client - notInGame - could not parse data: " + ByteConversion.printBytes(data));
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
            default:
                System.out.println("Client - inGame - could not parse data: "+ByteConversion.printBytes(data));
        }
    }



    private void gameEnded() {
        System.out.println("Client - gameEnded - broadcasting end game to clients");
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

    private class readyToPlay extends Thread{
        //This Thread removes waiting client, if the client stops sending a ready signal

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(2000);
                    Main.waitingClient = null;

                    System.out.println("Client: " + this + " left waiting room   - waitingClient should be null: " + Main.waitingClient);
                    break;
                } catch (InterruptedException e) {
                    if (game != null){
                        break;
                    }
                }
            }
        }
    }

    public void sendData(byte[] data) {
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

}
