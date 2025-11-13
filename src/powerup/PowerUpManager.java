package powerup; // Or use your game's package, e.g., game

import biuoop.DrawSurface;
import game.GameLevel;
import game.Sprite;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// Implement Sprite to hook into the game loop
public class PowerUpManager implements Sprite {
    private final List<PowerUp> list = new ArrayList<PowerUp>();
    private final Random rand = new Random();
    private final int panelHeight;

    public PowerUpManager(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public void spawnRandomAt(int x, int y) {
        // 25% chance to spawn a power-up
        if (rand.nextDouble() > 0.25) return;
        PowerUpType[] types = PowerUpType.values();
        PowerUpType t = types[rand.nextInt(types.length)];
        list.add(new PowerUp(x, y, t));
    }

    public void update() {
        Iterator<PowerUp> it = list.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            if (!p.isActive()) {
                it.remove();
                continue;
            }
            p.update();
            // Remove if it falls off-screen
            if (p.getY() > panelHeight + 50) it.remove();
        }
    }

    // --- Sprite Interface Methods ---

    @Override
    public void timePassed() {
        this.update(); // Update all power-ups
    }

    @Override
    public void drawOn(DrawSurface d) {
        for (PowerUp p : list) {
            p.drawOn(d); // Draw all power-ups
        }
    }

    // Helper to add to the game
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    public List<PowerUp> getList() {
        return list;
    }
}