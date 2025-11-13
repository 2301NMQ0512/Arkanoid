package geometry;

import static java.lang.Math.min;

/**
 * This class defines what constructs a line and how to create and draw it.
 *
 * 
 */
public class Line {
    /**
     * The Start.
     */
    private Point start;
    /**
     * The End.
     */
    private Point end;
    private static final double EPSILON = 1E-5;

    /**
     * Instantiates a new Line.
     *
     * @param start the start
     * @param end   the end
     */
// constructors
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * creates 2 points by 2 pairs of (x,y) coordinates.
     *
     * @param x1 a given x coordinate of a point
     * @param y1 a given y coordinate of a point
     * @param x2 a given x coordinate of a point
     * @param y2 a given y coordinate of a point
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     * Return the length of the line.
     *
     * @return the length
     */
    public double length() {
        return start.distance(end);
    }

    /**
     * Returns the middle point of the line.
     *
     * @return the point
     */
    public Point middle() {
        double middleX = (start.getX() + end.getX()) / 2;
        double middleY = (start.getY() + end.getY()) / 2;
        return new Point(middleX, middleY);
    }

    /**
     * Returns the start point of the line.
     *
     * @return the point
     */
    public Point start() {
        return this.start;
    }

    /**
     * Returns the end point of the line.
     *
     * @return the point
     */
    public Point end() {
        return this.end;
    }

    /**
     * Checks if double is close to zero, considering precision issues.
     *
     * @param v the given double
     * @return true of false
     */
    boolean closeToZero(double v) {
        return Math.abs(v) <= 0.0000000000001;
    }


    /**
     * Returns true if the lines intersect, false otherwise
     * takes 2 lines, and checks whether their slopes are even or not by the coordinates of theirs
     * starting and ending points. It also checks if they cross each other, continue each other or
     * any other case that may indicate on having an intersection point or not.
     *
     * @param other line
     * @return true of false - whether their is an intersection point with the other line or not.
     */
    public boolean isIntersecting(Line other) {
        double x1 = this.start.getX();
        double y1 = this.start.getY();
        double x2 = this.end.getX();
        double y2 = this.end.getY();

        double x3 = other.start.getX();
        double y3 = other.start.getY();
        double x4 = other.end.getX();
        double y4 = other.end.getY();

        if (Math.abs(x1 - x2) < EPSILON && Math.abs(x3 - x4) < EPSILON) {
            // Both segments are vertical
            if (x1 != x3) {
                return false;
            }
            if (min(y1, y2) < min(y3, y4)) {
                return Math.max(y1, y2) >= min(y3, y4);
            } else {
                return Math.max(y3, y4) >= min(y1, y2);
            }
        }
        if (Math.abs(x1 - x2) < EPSILON) {
            // Only segment "this" is vertical. Does segment "other" cross it? y = a*x + b
            double a34 = (y4 - y3) / (x4 - x3);
            double b34 = y3 - a34 * x3;
            double y = a34 * x1 + b34;
            return y >= min(y1, y2) && y <= Math.max(y1, y2) && x1 >= min(x3, x4) && x1 <= Math.max(x3, x4);
        }
        if (Math.abs(x3 - x4) < EPSILON) {
            // Only "other" is vertical. Does "this" cross it? y = a*x + b
            double a12 = (y2 - y1) / (x2 - x1);
            double b12 = y1 - a12 * x1;
            double y = a12 * x3 + b12;
            return y >= min(y3, y4) && y <= Math.max(y3, y4) && x3 >= min(x1, x2) && x3 <= Math.max(x1, x2);
        }
        double a12 = (y2 - y1) / (x2 - x1);
        double b12 = y1 - a12 * x1;
        double a34 = (y4 - y3) / (x4 - x3);
        double b34 = y3 - a34 * x3;
        if (closeToZero(a12 - a34)) {
            // Parallel lines
            return closeToZero(b12 - b34);
        }
        // Non parallel non vertical lines intersect at x. Is x part of both segments?
        double x = -(b12 - b34) / (a12 - a34);
        return x >= min(x1, x2) && x <= Math.max(x1, x2) && x >= min(x3, x4) && x <= Math.max(x3, x4);
    }

    /**
     * Returns the intersection point if the lines intersect and null otherwise.
     * takes 2 lines, and checks whether their slopes are even or not by the coordinates of theirs
     * starting and ending points. It also checks if they cross each other, continue each other or
     * any other case that may indicate on having an intersection point or not.
     *
     * @param other line
     * @return the intersection's point coordinates or null.
     */
    public Point intersectionWith(Line other) {
        if (!isIntersecting(other)) {
            return null;
        } else {
            double x11 = this.start.getX();
            double y11 = this.start.getY();
            double x12 = this.end.getX();
            double y12 = this.end.getY();

            double x21 = other.start.getX();
            double y21 = other.start.getY();
            double x22 = other.end.getX();
            double y22 = other.end.getY();

            if (Math.abs(x12 - x11) < EPSILON && Math.abs(x22 - x21) < EPSILON) {  // both lines are constant x
                // no intersection point
                return null;

            } else if (Math.abs(x12 - x11) < EPSILON || Math.abs(x22 - x21) < EPSILON) { // either line is constant x
                double x;
                double m;
                double b;
                if (Math.abs(x12 - x11) < EPSILON) { // first line is constant x, second is sloped
                    x = x11;
                    m = (y22 - y21) / (x22 - x21);
                    b = (x22 * y21 - x21 * y22) / (x22 - x21);
                } else { // second line is constant x, first is sloped
                    x = x21;
                    m = (y12 - y11) / (x12 - x11);
                    b = (x12 * y11 - x11 * y12) / (x12 - x11);
                }
                double y = m * x + b;
                return new Point(x, y);

            } else { // both lines are sloped
                double m1 = (y12 - y11) / (x12 - x11);
                double b1 = (x12 * y11 - x11 * y12) / (x12 - x11);

                double m2 = (y22 - y21) / (x22 - x21);
                double b2 = (x22 * y21 - x21 * y22) / (x22 - x21);

                if (Math.abs(m2 - m1) < EPSILON) {
                    // no intersection point
                    return null;
                }
                // calculating intersection coordinates
                double x = (b2 - b1) / (m1 - m2);
                double y = m1 * x + b1;  // or m2 * x + b2
                return new Point(x, y);
            }
        }
    }

    /**
     * return true is the lines are equal, false otherwise
     * checks whether the starting and ending points of the lines are the same, which can indicate they are both
     * equal lines.
     *
     * @param other line
     * @return true of flase - whether the lines are equal or not
     */
    public boolean equals(Line other) {
        if (this.start.equals(other.start) && this.end.equals(other.end)
                || this.start.equals(other.end) && this.end.equals(other.start)) {
            return true;
        }
        return false;
    }

    /**
     * If a given line does not intersect with the rectangle, return null.
     * Otherwise, return the closest intersection point to the
     * start of the line.
     *
     * @param rect the rectangle
     * @return null or the closest intersection point.
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        Line[] rectLines = new Line[4];
        Point[] intersectionPoints = new Point[4];
        double minDistance = 0;
        boolean foundIntersection = false;
        //up
        rectLines[0] = new Line(rect.getUpperLeft().getX(), rect.getUpperLeft().getY(),
                rect.getWidth() + rect.getUpperLeft().getX(), rect.getUpperLeft().getY());
        //right
        rectLines[1] = new Line(rect.getWidth() + rect.getUpperLeft().getX(), rect.getUpperLeft().getY(),
                rect.getUpperLeft().getX() + rect.getWidth(), rect.getHeight() + rect.getUpperLeft().getY());
        //down
        rectLines[2] = new Line(rect.getUpperLeft().getX() + rect.getWidth(),
                rect.getHeight() + rect.getUpperLeft().getY(),
                rect.getUpperLeft().getX(), rect.getUpperLeft().getY() + rect.getHeight());
        //left
        rectLines[3] = new Line(rect.getUpperLeft().getX(), rect.getUpperLeft().getY() + rect.getHeight(),
                rect.getUpperLeft().getX(), rect.getUpperLeft().getY());

        for (int i = 0; i < 4; i++) {
            if (intersectionWith(rectLines[i]) != null) {
                intersectionPoints[i] = intersectionWith(rectLines[i]);
                foundIntersection = true;
            }
        }
        Point pointClosestIntersectionToStartOfLine = null;
        boolean flag = true;
        if (foundIntersection) {

            for (int i = 0; i < intersectionPoints.length; i++) {
                if (intersectionPoints[i] == null) {
                    continue;
                }
                if (flag) {
                    minDistance = rect.getUpperLeft().distance(intersectionPoints[i]);
                    pointClosestIntersectionToStartOfLine = intersectionPoints[i];
                    flag = false;
                } else {
                    if (rect.getUpperLeft().distance(intersectionPoints[i]) < minDistance) {
                        pointClosestIntersectionToStartOfLine = intersectionPoints[i];
                    }
                }
            }
            return pointClosestIntersectionToStartOfLine;
        } else {
            return null;
        }
    }
}