package listeners;

import collidable.Block;
import ball.Ball;

/**
 * The type Printing hit listener.
 */
public class PrintingHitListener implements HitListener {
    /**
     * lmao.
     *
     * @param beingHit the block that being hit
     * @param hitter   the hitter is the ball.Ball that's doing the hitting.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        System.out.println("A collidable.Block was hit (" + beingHit.getCollisionRectangle().getUpperLeft().getX() + ","
                + beingHit.getCollisionRectangle().getUpperLeft().getY() + ")");
    }
}