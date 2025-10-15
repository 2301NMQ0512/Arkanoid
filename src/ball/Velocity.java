package ball;

public class Velocity {
    private double dx;
    private double dy;

    public Velocity(double dx, double dy) {
        this.dx = dx; this.dy = dy;
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    public static Velocity fromAngleAndSpeed(double angleDegrees, double speed) {
        double rad = Math.toRadians(angleDegrees);
        double dx = speed * Math.cos(rad);
        double dy = -speed * Math.sin(rad);
        return new Velocity(dx, dy);
    }
}
