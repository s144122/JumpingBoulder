package bouldercreek.jumpingboulder;

import java.net.InetAddress;
import java.util.Arrays;


class Reciever extends Thread {
    private final byte[] data;
    private final InetAddress address;
    private final int port;

    public Reciever(byte[] data, InetAddress address, int port) {
        this.data = data;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run(){
        int clientId = ByteConversion.convertByteToInt(new byte[]{data[0], data[1], data[2], data[3]});

        //print the details of incoming data - client ip : client port - client message
        /*
        System.out.println("Reciever - run - Incomming data from ip: " + address
                + " : " + port
                + " - " + clientId);
        */
        if (clientId == Integer.MIN_VALUE) {
            Client client = new Client(Main.getNextClientID(), address, port);
            Main.clients.put(client.getClientId(), client);

            byte[] id = ByteConversion.convertToByte(client.getClientId());
            client.sendData(new byte[]{0b00100000, id[0], id[1], id[2], id[3]});
            System.out.println("new client connected: "+ Arrays.toString(id));


        } else {
            Client client = (Client) Main.clients.get(clientId);
            client.act(data);
        }

    }
}
