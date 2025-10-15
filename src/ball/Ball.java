package ball;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import geometry.Point;
import geometry.Rect;
import game.Sprite;
import game.GameEnvironment;
import collidable.Collidable;

public class Ball implements Sprite {
    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private GameEnvironment environment;

    public Ball(Point center, int radius, Color color, Velocity v, GameEnvironment env) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.velocity = v;
        this.environment = env;
    }

    public Point getCenter() { return center; }
    public int getRadius() { return radius; }
    public Velocity getVelocity() { return velocity; }
    public void setVelocity(Velocity v) { this.velocity = v; }

    @Override
    public void drawOn(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillOval((int)(center.getX() - radius), (int)(center.getY() - radius), radius * 2, radius * 2);
    }

    @Override
    public void timePassed() {
        moveOneStep();
    }

    private void moveOneStep() {
        double nextX = center.getX() + velocity.getDx();
        double nextY = center.getY() + velocity.getDy();

        Collidable hit = environment.getCollidableAt(nextX, nextY, radius);
        if (hit != null) {
            Rect r = hit.getCollisionRectangle();
            boolean collidedVertically = (center.getY() <= r.getTop() && nextY >= r.getTop()) ||
                    (center.getY() >= r.getBottom() && nextY <= r.getBottom());
            boolean collidedHorizontally = (center.getX() <= r.getLeft() && nextX >= r.getLeft()) ||
                    (center.getX() >= r.getRight() && nextX <= r.getRight());


            hit.onHit(this);

            if (collidedVertically) {
                velocity.setDy(-velocity.getDy());
            } else if (collidedHorizontally) {
                velocity.setDx(-velocity.getDx());
            } else {
                velocity.setDy(-velocity.getDy());
                velocity.setDx(-velocity.getDx());
            }
            center.setX(center.getX() + velocity.getDx());
            center.setY(center.getY() + velocity.getDy());
        } else {
            double leftBound = environment.getLeftBound() + radius;
            double rightBound = environment.getRightBound() - radius;
            double topBound = environment.getTopBound() + radius;
            double bottomBound = environment.getBottomBound() - radius;

            if (nextX < leftBound) {
                velocity.setDx(-velocity.getDx());
                center.setX(leftBound);
            } else if (nextX > rightBound) {
                velocity.setDx(-velocity.getDx());
                center.setX(rightBound);
            } else {
                center.setX(nextX);
            }

            if (nextY < topBound) {
                velocity.setDy(-velocity.getDy());
                center.setY(topBound);
            } else if (nextY > bottomBound) {
                velocity.setDy(-velocity.getDy());
                center.setY(bottomBound);
            } else {
                center.setY(nextY);
            }
        }
    }
}
