package listeners;

import ball.Ball;
import collidable.Block;
import geometry.Point;
import powerup.PowerUpManager; // Make sure to import

public class PowerUpSpawner implements HitListener {
    private PowerUpManager powerUpManager;

    public PowerUpSpawner(PowerUpManager manager) {
        this.powerUpManager = manager;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // Spawn a power-up at the block's top-left corner
        Point spawnPoint = beingHit.getCollisionRectangle().getUpperLeft();
        powerUpManager.spawnRandomAt((int) spawnPoint.getX(), (int) spawnPoint.getY());
    }
}