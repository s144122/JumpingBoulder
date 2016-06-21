package bouldercreek.jumpingboulder;

/**
 * Created by jakob on 20-06-2016.
 */
public class Move {
    public int x;
    public int y;
    public double dx;
    public double dy;
    public long gameTime;

    public Move(int x, int y, double dx, double dy, long gameTime){
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.gameTime = gameTime;
    }

}
