package bouldercreek.jumpingboulder;

import android.content.Context;
import android.net.ConnectivityManager;
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
    final static int serverPort = 7888;
    public static int serverId = Integer.MIN_VALUE;
    public static final int packetSize = 29;
    private static InetAddress ip = null;
    private static DatagramSocket socket = null;

    public static void setUp() throws IOException {
        System.out.println("UDP - setUp -Setting up connection IP: "+serverIP + " port: "+serverPort);
        ip = InetAddress.getByName(serverIP);
        System.out.println("UDP - setUp -InetAddress created: " + ip);
        socket = new DatagramSocket(null);
        socket.connect(ip,serverPort);
        System.out.println("UDP - setUp - Socket established to server, getting id...");
        sendData(ByteConversion.convertToByte(Integer.MIN_VALUE));
        byte[] data = receiveData();
        if(data[0] == 0b00100000) {
            serverId = ByteConversion.convertByteToInt(new byte[]{data[1], data[2], data[3], data[4]});
            System.out.println("UDP - setUp - New server id is:" + serverId);
        }else{
            throw new IOException("UDP - setUp - Got wrong cmd data from server, expected 32 but got: " + data[0]);
        }

    }

    public static void startQuickGame(){
        byte[] data = 
    }


    private static byte[] receiveData() throws IOException {
        byte[] data = new byte[packetSize];
        DatagramPacket recivePacket = new DatagramPacket(data, data.length);
        System.out.println("UDP - recieveData - ready to recieve data");
        socket.receive(recivePacket);
        return recivePacket.getData();
    }


    private static void sendData(byte[] data) throws IOException {
        byte[] packedData = new byte[packetSize];
        for(int i=0; i<data.length; i++){
            packedData[i] = data[i];
        }
        DatagramPacket sendPacket = new DatagramPacket(packedData, packedData.length, ip, serverPort);
        socket.send(sendPacket);
        System.out.println("UDP - sendData - data send to: "+ sendPacket.getAddress() +" : "+ sendPacket.getPort() );
    }
}
