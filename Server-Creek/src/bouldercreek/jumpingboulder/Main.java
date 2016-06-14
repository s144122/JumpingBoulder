package bouldercreek.jumpingboulder;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import static java.lang.Integer.BYTES;
import static java.lang.Integer.toBinaryString;


public class Main {

    public static int nextClientID = Integer.MIN_VALUE;

    public static void main(String[] args) {
        System.out.println(nextClientID);
        final int serverPort = 1302;
        try {
            //1. creating a server socket, parameter is local port number
            DatagramSocket socket = new DatagramSocket(serverPort);

            //buffer to receive incoming data
            byte[] buffer = new byte[2048];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            while(true){
                socket.receive(incoming);
                byte[] data = incoming.getData();
                int clientId = ByteConversion.convertByteToInt(new byte[]{data[0],data[1],data[2],data[3]});



                //echo the details of incoming data - client ip : client port - client message
                System.out.println(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + clientId);

                /*DatagramPacket dp = new DatagramPacket(Database.convertIntToByte(clientId) ,
                        Database.convertIntToByte(clientId).length ,
                        incoming.getAddress() ,
                        incoming.getPort());
                socket.send(dp);*/
            }


        }catch(IOException e){
            System.err.println("IOException " + e);
        }




    }



}
