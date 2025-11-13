package listeners;

import collidable.Block;
import ball.Ball;

/**
 * The interface Hit listener.
 * Objects that want to be notified of hit events,
 * should implement the HitListener interface
 *
 * 
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     *
     * @param beingHit the block that being hit
     * @param hitter   the hitter is the Ball that's doing the hitting.
     */
    void hitEvent(Block beingHit, Ball hitter);
}

