package collidable;

import geometry.Rect;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import ball.Ball;
import game.GameLevel;
import powerup.PowerUp;
import powerup.PowerUpType;
import geometry.Point;

import java.util.Random;

public class Block implements Collidable {
    private Rect rect;
    private Color color;
    private boolean destroyed;
    private Runnable onDestroy;
    private GameLevel level; // optional link to level for spawning power-ups and scoring

    private static final Random RNG = new Random();

    public Block(Rect rect, Color color) {
        this.rect = rect;
        this.color = color;
        this.destroyed = false;
    }

    public void setGameLevel(GameLevel level) { this.level = level; }

    @Override
    public Rect getCollisionRectangle() { return rect; }

    @Override
    public void onHit(Ball hitter) {
        if (destroyed) return;
        this.destroyed = true;
        if (onDestroy != null) { onDestroy.run(); }

        // add score if level available
        if (level != null) {
            level.addScore(100);
            // spawn power-up with 25% chance
            double chance = 0.25;
            if (RNG.nextDouble() < chance) {
                PowerUpType[] types = PowerUpType.values();
                PowerUpType chosen = types[RNG.nextInt(types.length)];
                double pw = 20, ph = 20;
                double px = rect.getX() + rect.getWidth() / 2.0 - pw / 2.0;
                double py = rect.getY() + rect.getHeight() / 2.0 - ph / 2.0;
                powerup.PowerUp p = new powerup.PowerUp(new Rect(px, py, pw, ph), chosen, 3.0, level);
                level.addPowerUp(p);
            }
        }
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
