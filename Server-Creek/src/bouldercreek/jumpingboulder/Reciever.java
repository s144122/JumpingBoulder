package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Map;

import static bouldercreek.jumpingboulder.Main.clients;

/**
 * Created by jakob on 17-06-2016.
 */
public class Reciever extends Thread {
    DatagramPacket incoming;

    public Reciever(DatagramPacket incoming){
        this.incoming = incoming;

    }

    @Override
    public void run(){
        byte[] data = incoming.getData();
        int clientId = ByteConversion.convertByteToInt(new byte[]{data[0], data[1], data[2], data[3]});
        //echo the details of incoming data - client ip : client port - client message
        System.out.println("Reciever - run - Incomming data from ip: " + incoming.getAddress().getHostAddress()
                + " : " + incoming.getPort()
                + " - " + clientId);

        if (clientId == Integer.MIN_VALUE) {
            Client client = new Client(Main.getnextClientID(), incoming.getAddress(), incoming.getPort());
            Main.clients.put(client.getClientId(), client);
            DatagramPacket outgoing = new DatagramPacket(ByteConversion.convertToByte(Main.getnextClientID()), 1024);
            try {
                Main.socket.send(outgoing);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Client client = (Client) Main.clients.get(clientId);
            client.act(data);
        }

    }
}
