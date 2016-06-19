package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
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


    public Client(int clientId, InetAddress address, int port) {
        this.clientId = clientId;
        this.port = port;
        this.ip = address;
    }

    public void act(byte[] data){
        //Checker om den længst til venstre bit er 1 eller 0, i d. 5. byte i arrayet
        switch (data[4] & 0b10000000) {
            case 0b00000000: notInGame(data);
                break;
            case 0b10000000: inGame(data);
                break;
        }
        lastRecievedData = data;
    }


    private void notInGame(byte[] data){
        switch (data[4] & 0b01111111){
            case 0b00000001: newQuickGame();
                break;

        }
    }

    private void newQuickGame() {
        game = Main.newGame(this);

    }


    private void inGame(byte[] data) {
        switch (data[4] & 0b01111111){
            case 0b00000001: sendIfNewMove(data);
                break;
            case 0b00000010: gameEnded();
                break;

        }


    }

    private void gameEnded() {
        System.out.println("Client - gameEnded - game: " + game + " ended");
        game.interrupt();
    }

    private void sendIfNewMove(byte[] data) {
        int lastMoveTime = ByteConversion.convertByteToInt(new byte[]{data[13], data[14], data[15], data[16]});
        int lastMoveTimeFromErlierData = ByteConversion.convertByteToInt(new byte[]{data[13], data[14], data[15], data[16]});
        if (lastMoveTime != lastMoveTimeFromErlierData){
            queue.add(data);
        }
    }

    public void sendDate(byte[] data) {
        DatagramPacket outgoing = new DatagramPacket(data, Main.packetSize, ip, port);
        try {
            Main.socket.send(outgoing);
        } catch (IOException e) {
            System.out.println("Client - sendData - Client: " + this + " could not send data through socket: "+ Main.socket);
            e.printStackTrace();
        }
    }


    public int getClientId() {
        return clientId;
    }
}
