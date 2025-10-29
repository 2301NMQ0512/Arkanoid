package collidable;

import java.awt.*;

public class Paddle {
    private int x, y, width, height, panelWidth;
    private boolean movingLeft = false, movingRight = false;
    private final int baseSpeed = 8;
    private double speedMultiplier = 1.0;
    private int speed = baseSpeed;
    private final int baseWidth;
    private long expandExpireAt = 0;
    private long fastExpireAt = 0;

    public Paddle(int x, int y, int w, int h, int panelWidth) {
        this.x = x; this.y = y; this.width = w; this.baseWidth = w; this.height = h; this.panelWidth = panelWidth;
    }

    public void setMovingLeft(boolean b){ this.movingLeft = b; }
    public void setMovingRight(boolean b){ this.movingRight = b; }

    public void update() {
        long now = System.currentTimeMillis();
        if (expandExpireAt > 0 && now > expandExpireAt) {
            this.width = baseWidth;
            expandExpireAt = 0;
        }
        if (fastExpireAt > 0 && now > fastExpireAt) {
            this.speedMultiplier = 1.0;
            fastExpireAt = 0;
        }
        this.speed = (int)Math.round(baseSpeed * speedMultiplier);
        if (movingLeft) x -= speed;
        if (movingRight) x += speed;
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(x, y, width, height, 8, 8);
        g.setColor(Color.WHITE);
        g.drawRoundRect(x, y, width, height, 8, 8);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }

    // powerup methods
    public void applyExpand(int millis) {
        this.width = (int)Math.round(baseWidth * 1.5);
        this.expandExpireAt = System.currentTimeMillis() + millis;
    }

    public void applyFast(int millis) {
        this.speedMultiplier = 1.5;
        this.fastExpireAt = System.currentTimeMillis() + millis;
    }

    public long getExpandRemainingMillis() {
        if (expandExpireAt == 0) return 0;
        long rem = expandExpireAt - System.currentTimeMillis();
        return rem > 0 ? rem : 0;
    }
    public long getFastRemainingMillis() {
        if (fastExpireAt == 0) return 0;
        long rem = fastExpireAt - System.currentTimeMillis();
        return rem > 0 ? rem : 0;
    }

    public void reset() {
        this.width = baseWidth;
        this.speedMultiplier = 1.0;
        this.expandExpireAt = 0;
        this.fastExpireAt = 0;
    }
}
