package listeners;

import game.Counter;
import collidable.Block;
import ball.Ball;

/**
 * HitListener called ScoreTrackingListener to update the game's
 * counter when blocks are being hit and removed.
 *
 * 
 */
public class ScoreTrackingListener implements HitListener {
    private Counter score;


    public ScoreTrackingListener(Counter scoreCounter) {
        score = scoreCounter;
    }


    public void hitEvent(Block beingHit, Ball hitter) {
        score.increase(5);
    }
}