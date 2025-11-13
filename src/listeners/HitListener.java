package listeners;

import collidable.Block;
import ball.Ball;


public interface HitListener {

    void hitEvent(Block beingHit, Ball hitter);
}