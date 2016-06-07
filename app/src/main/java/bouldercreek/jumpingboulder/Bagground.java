package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Kristian on 07-06-2016.
 */
public class Bagground {
    private Bitmap image;
    private int x,y,dx;

    public Bagground(Bitmap res){
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    //Update bagground each loop, and move the bagground dx so the bagground moves
    public void update(){
        x+= dx;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

}
