package ball;

import geometry.Point;


public class Velocity {
    /**
     * The Dy.
     */
    private double theDy;
    /**
     * The Dx.
     */
    private double theDx;

    /**
     * Instantiates a new ball.Velocity.
     *
     * @param dx the dx
     * @param dy the dy
     */
// ball.Velocity specifies the change in position on the `x` and the `y` axes.
    // constructor
    public Velocity(double dx, double dy) {
        this.theDx = dx;
        this.theDy = dy;
    }

    /**
     * gets and sets velocity From angle and speed.
     *
     * @param angle the angle
     * @param speed the speed
     * @return the velocity
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double dx;
        double dy;
        if (angle > 360) {
            angle -= 360;
        }
        //the movement is in the first quarter.
        if ((angle >= 0) && (angle <= 90)) {
            dx = speed * Math.sin(Math.toRadians(angle));
            dy = -speed * Math.cos(Math.toRadians(angle));
            return new Velocity(dx, dy);
            //the movement is in the fourth quarter.
        } else if ((angle > 270) && (angle <= 360)) {
            angle = angle - 270;
            dx = -speed * Math.cos(Math.toRadians(angle));
            dy = -speed * Math.sin(Math.toRadians(angle));
            return new Velocity(dx, dy);
            //the movement is in the third quarter.
        } else if ((angle > 180) && (angle <= 270)) {
            angle = angle - 180;
            dx = -speed * Math.sin(Math.toRadians(angle));
            dy = speed * Math.cos(Math.toRadians(angle));
            return new Velocity(dx, dy);

        } else {
            //the movement is in the second quarter.
            angle = angle - 90;
            dx = speed * Math.cos(Math.toRadians(angle));
            dy = speed * Math.sin(Math.toRadians(angle));
            return new Velocity(dx, dy);
        }
    }

    /**
     * Take a point with position (x,y) and return a new point.
     * with position (x+dx, y+dy)
     *
     * @param p the given point
     * @return the new point
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + theDx, p.getY() + theDy);
    }

    /**
     * getDX.
     * bring us the dx value of the velocity.
     *
     * @return the dx value of this velocity.
     */
    public double getDx() {
        return this.theDx;
    }

    /**
     * Sets dx.
     *
     * @param dx the dx
     */
    public void setDx(double dx) {
        this.theDx = dx;
    }

    /**
     * getDY.
     * bring us the dy value of the velocity.
     *
     * @return the dy value of the velocity.
     */
    public double getDy() {
        return this.theDy;
    }

    /**
     * Sets dy.
     *
     * @param dy the dy
     */
    public void setDy(double dy) {
        this.theDy = dy;
    }
}
