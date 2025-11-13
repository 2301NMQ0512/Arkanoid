package collidable;

import geometry.Point;

/**
 * The type Collision info.
 *
 *
 */
public class CollisionInfo {
    private Point collisionPoint;
    private Collidable collisionObject;

    /**
     * collidable.CollisionInfo.
     * saves a collision information.
     *
     * @param collisionPoint  - the collision point.
     * @param collisionObject - the collision object (paddle/block).
     */
    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionObject = collisionObject;
        this.collisionPoint = collisionPoint;
    }

    /**
     * collisionPoint.
     * returns the collision point.
     *
     * @return collisionPoint. point
     */
    public Point collisionPoint() {
        return this.collisionPoint;
    }

    /**
     * collisionObject.
     * returns the collision object.
     *
     * @return the collision object.
     */
    public Collidable collisionObject() {
        return this.collisionObject;
    }
}