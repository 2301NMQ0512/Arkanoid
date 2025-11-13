package powerup; // Or use your game's package, e.g., game

import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import java.awt.Color;

public class PowerUp {
    private int x, y, size = 18;
    private int vy = 3; // Speed the power-up falls
    private PowerUpType type;
    private boolean active = true;

    public PowerUp(int x, int y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        y += vy;
    }

    // Changed to use biuoop.DrawSurface
    public void drawOn(DrawSurface d) {
        Color c = Color.WHITE;
        if (type == PowerUpType.EXPAND_PADDLE) c = Color.GREEN;
        else if (type == PowerUpType.FAST_PADDLE) c = Color.BLUE;
        else if (type == PowerUpType.FAST_BALL) c = Color.RED;

        d.setColor(c.darker());
        d.fillRectangle(x, y, size, size); // Use DrawSurface's fillRectangle
        d.setColor(c);
        d.fillRectangle(x + 3, y + 3, size - 6, size - 6);
    }

    // Changed to return your game's Rectangle class
    public Rectangle getRect() {
        return new Rectangle(new Point(x, y), size, size);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public PowerUpType getType() {
        return type;
    }

    public int getY() {
        return y;
    }
}