package bouldercreek.jumpingboulder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Kristian on 06-06-2016.
 */
public class GameActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(new GamePanel(this));
    }
}