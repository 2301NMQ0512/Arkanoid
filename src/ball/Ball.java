package ball;

import java.awt.*;

public class Ball {
    private int x, y, size;
    private int vx = 0, vy = 0;
    private double speedMultiplier = 1.0;
    private long fastExpireAt = 0;

    public Ball(int x, int y, int size) {
        this.x = x; this.y = y; this.size = size;
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (fastExpireAt > 0 && now > fastExpireAt) {
            speedMultiplier = 1.0;
            fastExpireAt = 0;
        }
        x += (int)Math.round(vx * speedMultiplier);
        y += (int)Math.round(vy * speedMultiplier);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, size, size);
    }

    public Rectangle getRect() { return new Rectangle(x, y, size, size); }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getSize(){ return size; }
    public int getVx(){ return vx; }
    public int getVy(){ return vy; }

    public void setX(int v){ x = v; }
    public void setY(int v){ y = v; }
    public void setVx(int v){ vx = v; }
    public void setVy(int v){ vy = v; }

    public void applyFast(double mult, int millis) {
        this.speedMultiplier = mult;
        this.fastExpireAt = System.currentTimeMillis() + millis;
    }

    public long getFastRemainingMillis() {
        if (fastExpireAt == 0) return 0;
        long rem = fastExpireAt - System.currentTimeMillis();
        return rem > 0 ? rem : 0;
    }

    public void reset() {
        this.speedMultiplier = 1.0;
        this.fastExpireAt = 0;
    }
}
