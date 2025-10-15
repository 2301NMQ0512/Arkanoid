package collidable;

import geometry.Rect;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import ball.Ball;

public class Block implements Collidable {
    private Rect rect;
    private Color color;
    private boolean destroyed;
    private Runnable onDestroy;

    public Block(Rect rect, Color color) {
        this.rect = rect;
        this.color = color;
        this.destroyed = false;
    }

    @Override
    public Rect getCollisionRectangle() { return rect; }

    @Override
    public void onHit(Ball hitter) {
        this.destroyed = true;
        if (onDestroy != null) { onDestroy.run(); }
    }

    public boolean isDestroyed() { return destroyed; }

    public void drawOn(Graphics g) {
        if (destroyed) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
        g2.setColor(Color.BLACK);
        g2.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
    }

    public void setOnDestroy(Runnable r) { this.onDestroy = r; }
}
