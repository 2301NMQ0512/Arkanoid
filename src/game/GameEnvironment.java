package game;

import collidable.CollisionInfo;
import geometry.Line;
import geometry.Point;
import collidable.Collidable;

import java.util.ArrayList;
import java.util.List;

/**
 * The type mainGame.Game environment.
 *
 * 
 */
public class GameEnvironment {
    private List<Collidable> collisionsList = new ArrayList<Collidable>();

    /**
     * adds the given collidable to the environment.
     *
     * @param c the collidable.
     */

    public void addCollidable(Collidable c) {
        this.collisionsList.add(c);
    }

    /**
     * removes the given collidable from the environment.
     *
     * @param c the c
     */
    public void removeCollidable(Collidable c) {
        this.collisionsList.remove(c);
    }

    /**
     * Gets closest collision point of an object with a collidable.
     * An object is moving from line.start() to line.end().
     * If this object will not collide with any of the collidables
     * in this collection, return null. Else, returns the information
     * about the closest collision that is going to occur.
     *
     * @param trajectory the trajectory line
     * @return the collision point or null
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        Point closestPoint = null;
        Collidable closestCollidable = null;
        for (Collidable c : collisionsList) {
            Point p = trajectory.closestIntersectionToStartOfLine(c.getCollisionRectangle());
            if (p != null) {
                if (closestPoint == null) {
                    closestPoint = p;
                    closestCollidable = c;
                } else {
                    if (trajectory.start().distance(p) < trajectory.start().distance(closestPoint)) {
                        closestPoint = p;
                        closestCollidable = c;
                    }
                }
            }
        }
        if (closestPoint == null) {
            return null;
        } else {
            return new CollisionInfo(closestPoint, closestCollidable);
        }
    }

}
