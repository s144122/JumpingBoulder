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
    final static String serverIP = "10.16.172.118";
    final static int serverPort = 1302;
    public static int serverId = Integer.MIN_VALUE;

    private static InetAddress ip = null;
    private static DatagramSocket socket = null;

    public static void setUp() throws IOException {
        ip = InetAddress.getByName(serverIP);
        socket = new DatagramSocket(serverPort, ip);
        System.out.println("UDP - setUp - Socket established to server, getting id...");
        sendData(new byte[]{0,0,0,0});
        byte[] data = receiveData();
        serverId = ByteConversion.convertByteToInt(new byte[]{data[0],data[1],data[2],data[3]});
        System.out.println("UDP - setUp - New server id is:");

    }

    private static byte[] receiveData() throws IOException {
        byte[] data = new byte[1024];
        DatagramPacket recivePacket = new DatagramPacket(data, data.length);
        socket.receive(recivePacket);
        return recivePacket.getData();
    }


    private static void sendData(byte[] data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, serverPort);
        socket.send(sendPacket);

    }


}
