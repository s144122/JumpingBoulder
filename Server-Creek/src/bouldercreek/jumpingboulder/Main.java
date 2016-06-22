package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



class Main {

    public static final Map clients = new HashMap<Integer,Client>();
    private static int nextClientID = Integer.MIN_VALUE;
    public static Client waitingClient = null;
    public static DatagramSocket socket = null;
    public final static int packetSize = 64;

    public static void main(String[] args) {
        final int serverPort = 7888;
        try {
            //Creating a server socket, parameter is local port number
            socket = new DatagramSocket(serverPort);

            //buffer to receive incoming data
            byte[] buffer = new byte[packetSize];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            System.out.println("Main - main - server is ready");
            //noinspection InfiniteLoopStatement
            while (true) {
                socket.receive(incoming);
                //System.out.println("Main - main - recieved packet");
                Thread reciever = new Reciever(incoming.getData(), incoming.getAddress(), incoming.getPort());
                reciever.start();
            }
        }catch(IOException e){
            System.err.println("Main - main - IOException " + e);
        }
    }

    public static void newQuickGame(Client client) {
        if(waitingClient == null){
            waitingClient = client;
            System.out.println("Client: " + client + " is now waiting for an opponent");
        }else{
            BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
            GameThread game = new GameThread(queue,waitingClient,client);
            waitingClient.game = game;
            client.game = game;
            game.start();
            waitingClient.setQueue(queue);
            client.setQueue(queue);
            waitingClient = null;

        }

    }

    public static int getNextClientID() {
        nextClientID++;

        //this is to ensure the server never assigns a client with the default client id
        if (nextClientID == Integer.MIN_VALUE) {
            nextClientID++;
        }
        //Since it will take over 4 000 000 000 client connections, for the server to loop through,
        // it's not necessary to check if  the clientID is used elsewhere.
        return nextClientID;
    }


    public static void sendData(byte[] data, InetAddress ip, int port) throws IOException {
        byte[] packedData = new byte[packetSize];
        System.arraycopy(data, 0, packedData, 0, data.length);
        DatagramPacket sendPacket = new DatagramPacket(packedData, packedData.length, ip, port);
        socket.send(sendPacket);
        /*
        System.out.println("Main - sendData - data send to: "+ sendPacket.getAddress()
                +" : "+ sendPacket.getPort()
                + " - " + ByteConversion.printBytes(data);
        */
    }
}
