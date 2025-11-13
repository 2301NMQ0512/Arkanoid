package geometry;

public class Point {
    private final double x;
    private final double y;



    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double distance(Point other) {
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        return Math.sqrt((dx * dx) + (dy * dy));
    }


    public double getX() {
        return this.x;
    }


    public double getY() {
        return this.y;
    }
}
