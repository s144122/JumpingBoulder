package bouldercreek.jumpingboulder;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by jakob on 08-06-2016.
 */
public class UDP extends AsyncTask<Void, Float[], Boolean>{
    final String serverIP = "10.16.172.118";
    final int serverPort = 1302;
    public static int serverId = Integer.MIN_VALUE;
    private static DatagramSocket socket = null;

    @Override
    protected Boolean doInBackground(Void... params) {



        return null;
    }
}
