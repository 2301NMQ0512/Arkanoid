package powerup;

import java.awt.*;

public class PowerUp {
    private int x, y, size = 18;
    private int vy = 3;
    private PowerUpType type;
    private boolean active = true;

    public PowerUp(int x, int y, PowerUpType type) {
        this.x = x; this.y = y; this.type = type;
    }

    public void update() { y += vy; }

    public void draw(Graphics2D g) {
        Color c = Color.WHITE;
        if (type == PowerUpType.EXPAND_PADDLE) c = Color.GREEN;
        else if (type == PowerUpType.FAST_PADDLE) c = Color.BLUE;
        else if (type == PowerUpType.FAST_BALL) c = Color.RED;

        g.setColor(c.darker());
        g.fillRect(x, y, size, size);
        g.setColor(c);
        g.fillRect(x+3, y+3, size-6, size-6);
    }

    public java.awt.Rectangle getRect() { return new java.awt.Rectangle(x, y, size, size); }
    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public PowerUpType getType() { return type; }
    public int getY() { return y; }
}
