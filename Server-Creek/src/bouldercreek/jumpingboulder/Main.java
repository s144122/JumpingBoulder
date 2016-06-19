package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



public class Main {

    public static Map clients = new HashMap<Integer,Client>();
    private static int nextClientID = Integer.MIN_VALUE+1;
    private static GameThread newGameRoom = null;
    public static DatagramSocket socket = null;
    public final static int packetSize = 29;

    public static void main(String[] args) {
        final int serverPort = 7865;
        try {
            //Creating a server socket, parameter is local port number
            socket = new DatagramSocket(serverPort);

            //buffer to receive incoming data
            byte[] buffer = new byte[packetSize];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            while (true) {
                socket.receive(incoming);
                new Reciever(incoming).start();

            }
        }catch(IOException e){
            System.err.println("Main - main - IOException " + e);
        }
    }

    public static GameThread newGame(Client client) {
        if(newGameRoom.equals(null)){
            BlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();
            newGameRoom = new GameThread(queue, client);

        }
        return newGameRoom;
    }

    public static int getnextClientID() {
        nextClientID++;

        //this is to ensure the server never assigns a client with the default client id
        if (nextClientID == Integer.MIN_VALUE) {
            nextClientID++;
        }
        //Since it will take over 4 000 000 000 client connections, for the server to loop through,
        // it's not necessary to check if  the clientID is used elsewhere.
        return nextClientID;
    }
}
