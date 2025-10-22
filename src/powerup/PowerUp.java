package powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import game.Sprite;
import geometry.Rect;
import game.GameLevel;

public class PowerUp implements Sprite {
    private Rect rect;
    private PowerUpType type;
    private double speedY;
    private Color color;
    private GameLevel level;
    private boolean collected = false;

    public PowerUp(Rect rect, PowerUpType type, double speedY, GameLevel level) {
        this.rect = rect;
        this.type = type;
        this.speedY = speedY;
        this.level = level;
        switch (type) {
            case EXPAND: color = Color.GREEN; break;
            case FASTBALL: color = Color.MAGENTA; break;
            case EXTRA_LIFE: color = Color.CYAN; break;
            default: color = Color.GRAY; break;
        }
    }

    public Rect getRect() { return rect; }
    public PowerUpType getType() { return type; }

    @Override
    public void drawOn(Graphics g) {
        if (collected) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillOval((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
        g2.setColor(Color.BLACK);
        g2.drawOval((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
    }

    @Override
    public void timePassed() {
        if (collected) return;
        rect = new Rect(rect.getX(), rect.getY() + speedY, rect.getWidth(), rect.getHeight());

        // check if out of screen bottom => remove from level
        if (rect.getY() > level.getHeight()) {
            level.removePowerUp(this);
            return;
        }

        // check collision with paddle
        Rect paddleRect = level.getPaddle().getCollisionRectangle();
        if (rectOverlaps(rect, paddleRect)) {
            collected = true;
            level.onPowerUpCollected(this);
            level.removePowerUp(this);
        }
    }

    private boolean rectOverlaps(Rect a, Rect b) {
        return !(a.getRight() < b.getLeft() || a.getLeft() > b.getRight() || a.getBottom() < b.getTop() || a.getTop() > b.getBottom());
    }
}
