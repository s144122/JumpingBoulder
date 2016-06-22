package bouldercreek.jumpingboulder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bagground {
    private final Bitmap image;
    private int x,y;

    public Bagground(Bitmap res){
        image = res;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

}
