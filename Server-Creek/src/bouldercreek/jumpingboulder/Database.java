package bouldercreek.jumpingboulder;

import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Created by jakob on 10-06-2016.
 */
public class Database {
    private static int numberOfHashes = 32;
    private static ArrayList<GameThread>[] clients = new ArrayList[numberOfHashes];

    public static void addClient(int clientId){

    }

    private static int hashFunc(int clientId){
        return Math.abs(clientId%numberOfHashes);
    }


 }
