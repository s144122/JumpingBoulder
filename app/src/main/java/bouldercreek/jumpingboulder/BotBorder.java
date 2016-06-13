package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Kristian on 08-06-2016.
 */
public class BotBorder extends GameObject {
    private Bitmap image;
    public BotBorder(Bitmap res, int x, int y){
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;
        // speed of bagground not importent.
//        dx = GamePanel.MOVESPEED;

        image = Bitmap.createBitmap(res,0,0,width,height);
    }
    public void draw(Canvas canvas){
        try{canvas.drawBitmap(image,x,y,null);}
        catch ( Exception e){};

    }
}
