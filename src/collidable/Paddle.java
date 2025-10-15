package collidable;

import geometry.Rect;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import ball.Ball;

public class Paddle implements Collidable {
    private Rect rect;
    private Color color;
    private int moveSpeed;
    private int leftBound, rightBound;

    public Paddle(Rect rect, Color color, int moveSpeed, int leftBound, int rightBound) {
        this.rect = rect;
        this.color = color;
        this.moveSpeed = moveSpeed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    @Override
    public Rect getCollisionRectangle() { return rect; }

    @Override
    public void onHit(Ball hitter) {
    }

    public void drawOn(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
    }

    public void moveLeft() {
        double newX = rect.getX() - moveSpeed;
        if (newX < leftBound) newX = leftBound;
        rect = new Rect(newX, rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void moveRight() {
        double newX = rect.getX() + moveSpeed;
        if (newX + rect.getWidth() > rightBound) newX = rightBound - rect.getWidth();
        rect = new Rect(newX, rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public KeyListener getKeyListener() {
        return new KeyListener() {
            private boolean left = false;
            private boolean right = false;

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_LEFT) left = true;
                if (code == KeyEvent.VK_RIGHT) right = true;
                if (left) moveLeft();
                if (right) moveRight();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_LEFT) left = false;
                if (code == KeyEvent.VK_RIGHT) right = false;
            }
        };
    }
}
