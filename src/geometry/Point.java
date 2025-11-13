package geometry;

/**
 * This class defines a point.
 * It defines how a point is constructed and how to calculate a distance between 2 points.
 *
 * 
 */
public class Point {
    private double x;
    private double y;

    /**
     * Instantiates a new Point.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
// constructor
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * distance -- return the distance of this point to the other point.
     *
     * @param other the other point
     * @return the distance
     */
    public double distance(Point other) {
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    /**
     * equals -- return true is the points are equal, false otherwise.
     *
     * @param other the other point
     * @return true or falls
     */
    public boolean equals(Point other) {
        if (distance(other) == 0) {
            return true;
        }
        return false;
    }

    /**
     * Gets x coordinate of a point.
     *
     * @return the x coordinate of a point
     */
// Return the x and y values of this point
    public double getX() {
        return this.x;
    }

    /**
     * Gets y coordinate of a point.
     *
     * @return the y coordinate of a point
     */
    public double getY() {
        return this.y;
    }
}
