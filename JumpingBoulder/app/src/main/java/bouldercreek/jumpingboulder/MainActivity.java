package bouldercreek.jumpingboulder;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        new serverSetUp().execute();

        button = (Button) findViewById(R.id.BT);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(startGame);
            }

        });
    }

    private class serverSetUp extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... params) {
            try {
                UDP.setUp();
            } catch (IOException e) {
                e.printStackTrace();
                return "Could not connect to server";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView errorMsg = (TextView) findViewById(R.id.ServerConnectionError);
            errorMsg.setText(s);
        }
    }

}


