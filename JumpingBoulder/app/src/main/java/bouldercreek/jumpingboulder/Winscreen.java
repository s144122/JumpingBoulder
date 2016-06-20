package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Kristian on 07-06-2016.
 */
public class Winscreen extends GameObject {
    private Bitmap image;
    private int x,y,dx;
    private Animation animation = new Animation();

    public Winscreen(Bitmap res,int x, int y){
        image = res;
        image = Bitmap.createBitmap(res,0,0,x,y);
    }

    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}
    }
    public void update()
    {
        animation.update();
    }
}
