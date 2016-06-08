package bouldercreek.jumpingboulder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn titel off
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        connectToServer();

    }


    public void quickGameButtonOnClick(View v){
        //startActivity(new Intent(this, QuickGameRoom.class));
        System.out.println(GlobalVariables.serverSocket);
    }

    private void connectToServer(){
        final String serverIP = "10.16.172.118";
        final int serverPort = 1302;

        new Thread() {
            public void run() {
                try {
                    System.out.println("Trying to connect to server " +
                            "IP: "+serverIP + " " +
                            "port: " + serverPort);
                    GlobalVariables.serverSocket = new Socket(serverIP,serverPort);
                    System.out.println("Server connection established");
                    BufferedReader bf = new BufferedReader(
                            new InputStreamReader(GlobalVariables.serverSocket.getInputStream()));
                    System.out.println(bf.readLine());

                } catch (IOException e) {
                    System.out.println("Could not connect to server on"
                            + " IP: " + serverIP
                            + " Port: "+serverPort
                    );
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
