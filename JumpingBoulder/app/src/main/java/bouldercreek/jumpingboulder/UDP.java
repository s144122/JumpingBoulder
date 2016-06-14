package bouldercreek.jumpingboulder;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by jakob on 08-06-2016.
 */
public class UDP{
    final String serverIP = "10.16.172.118";
    final int serverPort = 1302;
    public static int serverId = Integer.MIN_VALUE;
    private static DatagramSocket socket = null;
    private static DatagramPacket incoming = null;
    private static byte[] buffer = new byte[2048];


    public static boolean requestGame(){




        return false;
    }

    public static boolean sendCoordinates(){



        return false;
    }

    public static float[] recieveCoordinates(){
        float x = 0;
        float y = 0;

        try {
            socket.receive(incoming);
            byte[] data = incoming.getData();
            int clientId = Database.convertByteToInt(new byte[]{data[0], data[1], data[2], data[3]});


            //echo the details of incoming data - client ip : client port - client message
            System.out.println(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + clientId);

                /*DatagramPacket dp = new DatagramPacket(Database.convertIntToByte(clientId) ,
                        Database.convertIntToByte(clientId).length ,
                        incoming.getAddress() ,
                        incoming.getPort());
                socket.send(dp);*/

        } catch (IOException e) {
            e.printStackTrace();
        }


        return new float[]{x,y};
    }

}
