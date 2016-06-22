package bouldercreek.jumpingboulder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kristian on 22-06-2016.
 */
public class newGame extends AppCompatActivity {

    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_main);
    //}

    public void onClick(Boolean click){
        Intent newGame = new Intent(newGame.this, MainActivity.class);
        startActivity(newGame);
    }

};


