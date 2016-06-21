package bouldercreek.jumpingboulder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        if(ByteConversion.convertByteToInt(UDP.serverId) == Integer.MIN_VALUE) {
            System.out.println("Creating AsyncTask for connection");
            new serverSetUp().execute();
        }

        button = (Button) findViewById(R.id.BT);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UDP.startQuickGame();
                Intent startGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(startGame);
            }

        });
    }

    private class serverSetUp extends AsyncTask<Void,String,String>{


        @Override
        protected String doInBackground(Void... params) {
            //System.out.println("Async Task for network created");

            try {
                UDP.setUp();
                return "Connection successful";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Could not connect to server";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView text = (TextView) findViewById(R.id.ServerConnectionText);
            text.setText(s);
        }
    }

}


