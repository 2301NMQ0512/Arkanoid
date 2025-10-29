package collidable;

import java.awt.*;

public class Block {
    private int x, y, width, height;
    private Color color;
    private boolean destroyed = false;

    public Block(int x, int y, int w, int h, Color color) {
        this.x = x; this.y = y; this.width = w; this.height = h; this.color = color;
    }

    public void draw(Graphics2D g) {
        if (destroyed) return;
        g.setColor(color.darker());
        g.fillRect(x, y, width, height);
        g.setColor(color);
        g.fillRect(x+3, y+3, width-6, height-6);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, width, height);
    }

    public Rectangle getRect() { return new Rectangle(x, y, width, height); }
    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean d) { this.destroyed = d; }
    public int getX(){ return x; }
    public int getY(){ return y; }
}
