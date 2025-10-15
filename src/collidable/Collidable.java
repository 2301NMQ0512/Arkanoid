package collidable;

import geometry.Rect;
import ball.Ball;

public interface Collidable {
    Rect getCollisionRectangle();
    void onHit(Ball hitter);
}
