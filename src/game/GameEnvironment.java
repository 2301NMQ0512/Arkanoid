package game;

import collidable.CollisionInfo;
import geometry.Line;
import geometry.Point;
import collidable.Collidable;

import java.util.ArrayList;
import java.util.List;


public class GameEnvironment {
    private final List<Collidable> collisionsList = new ArrayList<>();


    public void addCollidable(Collidable c) {
        this.collisionsList.add(c);
    }

    public void removeCollidable(Collidable c) {
        this.collisionsList.remove(c);
    }

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
