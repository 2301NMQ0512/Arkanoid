package collidable;

import ball.Velocity;
import ball.Ball;
import geometry.Point;
import geometry.Rectangle;

/**
 * The interface of Collidables.
 *
 *
 */
public interface Collidable {
    /**
     * Gets collision shape.
     *
     * @return Return the "collision shape" of the object.
     */
    Rectangle getCollisionRectangle();

    /**
     * Hit velocity.
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     *
     * @param hitter          the hitter - the ball that hit the block
     * @param collisionPoint  the collision point of the ball with the block
     * @param currentVelocity the current velocity of the ball
     * @return the new velocity of the ball
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
