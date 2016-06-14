package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Kristian on 08-06-2016.
 */
public class TopBorder extends GameObject {
    private Bitmap image;

    public TopBorder(Bitmap res,int x, int y,int h){
        height = h;
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
