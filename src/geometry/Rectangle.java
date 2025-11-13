package geometry;


public class Rectangle {

    private final double width;
    private final double height;
    private final Point upperLeft;


    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }




    public double getWidth() {
        return width;
    }


    public double getHeight() {
        return height;
    }


    public Point getUpperLeft() {
        return this.upperLeft;
    }

}