package collidable;

import ball.Velocity;
import ball.Ball;
import geometry.Point;
import geometry.Rectangle;

public interface Collidable {

    Rectangle getCollisionRectangle();

    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
