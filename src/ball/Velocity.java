package ball;

import geometry.Point;

public class Velocity {
    /**
     * The Dy.
     */
    private final double theDy;
    /**
     * The Dx.
     */
    private final double theDx;

    /**
     * Instantiates a new ball.Velocity.
     *
     * @param dx the dx
     * @param dy the dy
     */
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
            angle = 360 - angle;
            dx = speed * Math.sin(Math.toRadians(angle));
            dy = speed * Math.cos(Math.toRadians(angle));
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


    public double getDx() {
        return this.theDx;
    }

    public double getDy() {
        return this.theDy;
    }


    public double getSpeed() {
        return Math.sqrt((this.theDx * this.theDx) + (this.theDy * this.theDy));
    }

}