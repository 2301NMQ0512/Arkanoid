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
        double dx = velocity.getDx();
        double dy = velocity.getDy();

        // Sub-stepping để tránh xuyên vật khi dx/dy lớn
        double maxStep = Math.max(Math.abs(dx), Math.abs(dy));
        int steps = (int) Math.ceil(maxStep / (double) Math.max(1, radius)); // cứ mỗi bán kính 1 bước
        if (steps < 1) steps = 1;

        double stepDx = dx / steps;
        double stepDy = dy / steps;

        for (int s = 0; s < steps; s++) {
            double testX = center.getX() + stepDx;
            double testY = center.getY() + stepDy;

            Collidable hit = environment.getCollidableAt(testX, testY, radius);
            if (hit != null) {
                Rect r = hit.getCollisionRectangle();

                // detect approximate side based on this small step
                boolean collidedVertically = (center.getY() <= r.getTop() && testY >= r.getTop()) ||
                        (center.getY() >= r.getBottom() && testY <= r.getBottom());
                boolean collidedHorizontally = (center.getX() <= r.getLeft() && testX >= r.getLeft()) ||
                        (center.getX() >= r.getRight() && testX <= r.getRight());

                // let the object handle any game-specific logic (block destroyed, power-up spawn, ...)
                hit.onHit(this);

                // Special handling for paddle: paddle decides direction — push ball above paddle
                if (hit instanceof collidable.Paddle) {
                    // ensure velocity already set by paddle.onHit; place ball above paddle
                    center.setY(r.getTop() - radius - 1);
                    // small x adjustment so it doesn't repeatedly collide at same pixel
                    center.setX(center.getX() + velocity.getDx() * 0.5);
                    return;
                }

                // For other collidables (blocks, walls), reflect velocity depending on side
                if (collidedVertically) {
                    velocity.setDy(-velocity.getDy());
                } else if (collidedHorizontally) {
                    velocity.setDx(-velocity.getDx());
                } else {
                    // ambiguous corner — invert both
                    velocity.setDx(-velocity.getDx());
                    velocity.setDy(-velocity.getDy());
                }

                // move a bit along new velocity so we are outside the object
                center.setX(center.getX() + velocity.getDx());
                center.setY(center.getY() + velocity.getDy());
                return;
            } else {
                // no collision for this sub-step: commit it
                center.setX(testX);
                center.setY(testY);

                // bounds check (walls)
                double leftBound = environment.getLeftBound() + radius;
                double rightBound = environment.getRightBound() - radius;
                double topBound = environment.getTopBound() + radius;
                double bottomBound = environment.getBottomBound() - radius;

                if (center.getX() < leftBound) {
                    velocity.setDx(-velocity.getDx());
                    center.setX(leftBound);
                } else if (center.getX() > rightBound) {
                    velocity.setDx(-velocity.getDx());
                    center.setX(rightBound);
                }

                if (center.getY() < topBound) {
                    velocity.setDy(-velocity.getDy());
                    center.setY(topBound);
                } else if (center.getY() > bottomBound) {
                    // ball lost
                    environment.notifyBallLost();
                    return;
                }
            }
        }
    }

}
