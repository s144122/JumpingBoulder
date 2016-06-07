package bouldercreek.jumpingboulder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn titel off
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
    }


    button = (Button) findViewById(R.id.button);

    button.setOnClickListener(new View.OnClickListener(){
        @Override;
        public void OnClick(View v){

    }



    public void quickGameButtonOnClick(View v){
        Intent startGame = new Intent(this, GameActivity.class);
        startActivity(startGame);
    }
}
