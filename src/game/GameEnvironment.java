package game;

import collidable.Collidable;
import collidable.Block;
import collidable.Paddle;
import java.util.List;
import java.util.ArrayList;
import geometry.Rect;
import ball.Ball;

public class GameEnvironment {
    private List<Collidable> collidables = new ArrayList<>();
    private int leftBound = 0, rightBound = 800, topBound = 0, bottomBound = 600;

    public void addCollidable(Collidable c) { collidables.add(c); }
    public void removeCollidable(Collidable c) { collidables.remove(c); }

    public Collidable getCollidableAt(double x, double y, int radius) {
        for (Collidable c : new ArrayList<>(collidables)) {
            Rect r = c.getCollisionRectangle();
            double left = r.getX() - radius;
            double right = r.getX() + r.getWidth() + radius;
            double top = r.getY() - radius;
            double bottom = r.getY() + r.getHeight() + radius;
            if (x >= left && x <= right && y >= top && y <= bottom) {
                return c;
            }
        }
        return null;
    }

    public List<Collidable> getCollidables() { return collidables; }

    public int getLeftBound() { return leftBound; }
    public int getRightBound() { return rightBound; }
    public int getTopBound() { return topBound; }
    public int getBottomBound() { return bottomBound; }

    public void setBounds(int left, int right, int top, int bottom) {
        this.leftBound = left; this.rightBound = right; this.topBound = top; this.bottomBound = bottom;
    }
}
