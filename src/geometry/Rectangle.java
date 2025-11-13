package geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * The type geometry.Rectangle.
 *
 * 
 */
public class Rectangle {

    private double width;
    private double height;
    private Point upperLeft;

    /**
     * Instantiates a new Rectangle.
     *
     * @param upperLeft the upper left corner point (x and y coordinates)
     * @param width     the width
     * @param height    the height
     */
// Create a new rectangle with location and width/height.
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }


    /**
     * Return a (possibly empty) List of intersection points
     * with the specified line.
     *
     * @param line a given line
     * @return the list
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        List<Point> intersectionPointsList = new ArrayList<Point>();
        Line[] rectangleSides = new Line[4];
        Point[] corners = new Point[4];
        rectangleCorners(corners);
        rectangleSides(rectangleSides, corners);
        for (int i = 0; i < 4; i++) {
            Point p = line.intersectionWith(rectangleSides[i]);
            if (p != null && !intersectionPointsList.contains(p)) {
                intersectionPointsList.add(p);
            }
        }
        return intersectionPointsList;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return the width
     */
// Return the width and height of the rectangle
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the rectangle.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the upper left corner point of the rectangle.
     *
     * @return the upper left point
     */
// Returns the upper-left point of the rectangle.
    public Point getUpperLeft() {
        return this.upperLeft;
    }

    /**
     * geometry.Rectangle corners.
     *
     * @param corners the corners
     */
    public void rectangleCorners(Point[] corners) {
        double theWidth = this.getWidth();
        double theHeight = this.getHeight();
        double x = this.upperLeft.getX();
        double y = this.upperLeft.getY();
        //upper left
        corners[0] = this.getUpperLeft();
        //upper right
        corners[1] = new Point(x + width, y);
        //down right
        corners[2] = new Point(x + width, y + height);
        //down left
        corners[3] = new Point(x, y + height);
    }

    /**
     * rectangleSides.
     * Saves the lines in the 4 rectangle edges.
     *
     * @param sides   - an empty array of lines
     * @param corners - an array of edge points.
     */
    public void rectangleSides(Line[] sides, Point[] corners) {
        // up
        sides[0] = new Line(corners[0], corners[1]);
        // right
        sides[1] = new Line(corners[1], corners[2]);
        // down
        sides[2] = new Line(corners[2], corners[3]);
        // left
        sides[3] = new Line(corners[3], corners[0]);
    }
}