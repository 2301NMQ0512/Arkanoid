package geometry;

public class Rect {
    private double x, y, width, height;

    public Rect(double x, double y, double width, double height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public double getLeft() { return x; }
    public double getRight() { return x + width; }
    public double getTop() { return y; }
    public double getBottom() { return y + height; }

    public boolean contains(double px, double py) {
        return px >= getLeft() && px <= getRight() && py >= getTop() && py <= getBottom();
    }
}
