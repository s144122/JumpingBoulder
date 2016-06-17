package bouldercreek.jumpingboulder;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by jakob on 08-06-2016.
 */
public class UDP{
    final static String serverIP = "10.16.169.37";
    final static int serverPort = 7865;
    public static int serverId = Integer.MIN_VALUE;

    private static InetAddress ip = null;
    private static DatagramSocket socket = null;

    public static void setUp() throws IOException {
        System.out.println("Setting up connection IP: "+serverIP + " port: "+serverPort);
        ip = InetAddress.getByName(serverIP);
        System.out.println("InetAddress created: " + ip);
        socket = new DatagramSocket(null);
        socket.connect(ip,serverPort);
        System.out.println("UDP - setUp - Socket established to server, getting id...");
        sendData(new byte[]{0,0,0,0});
        byte[] data = receiveData();
        serverId = ByteConversion.convertByteToInt(new byte[]{data[0],data[1],data[2],data[3]});
        System.out.println("UDP - setUp - New server id is:" + serverId);

    }



    private static byte[] receiveData() throws IOException {
        byte[] data = new byte[128];
        DatagramPacket recivePacket = new DatagramPacket(data, data.length);
        socket.receive(recivePacket);
        return recivePacket.getData();
    }


    private static void sendData(byte[] data) throws IOException {
        byte[] packedData = new byte[128];
        for(int i=0; i<data.length; i++){
            packedData[i] = data[i];
        }
        DatagramPacket sendPacket = new DatagramPacket(packedData, packedData.length, ip, serverPort);
        socket.send(sendPacket);

    }


}
