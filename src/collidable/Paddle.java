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
        // Vị trí va chạm
        double ballX = hitter.getCenter().getX();
        double paddleX = rect.getX();
        double paddleWidth = rect.getWidth();

        double hitPos = (ballX - paddleX) / paddleWidth; // 0..1
        if (hitPos < 0) hitPos = 0;
        if (hitPos > 1) hitPos = 1;

        // Góc từ 150° (trái mạnh) -> 90° (giữa) -> 30° (phải mạnh)
        double angleDeg = 150.0 - 120.0 * hitPos;

        // speed hiện tại
        double dxOld = hitter.getVelocity().getDx();
        double dyOld = hitter.getVelocity().getDy();
        double speed = Math.sqrt(dxOld * dxOld + dyOld * dyOld);
        if (speed < 0.0001) speed = 4.0; // phòng trường hợp tốc độ = 0

        // Đổi hướng giữ speed
        hitter.setVelocity(ball.Velocity.fromAngleAndSpeed(angleDeg, speed));

        // Đẩy bóng ra khỏi paddle (đặt ngay phía trên) để không dính hoặc xuyên
        hitter.getCenter().setY(rect.getY() - hitter.getRadius() - 1);
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

    public void expand(double extraWidth) {
        double newW = rect.getWidth() + extraWidth;
        double centerX = rect.getX() + rect.getWidth() / 2.0;
        double newX = centerX - newW / 2.0;
        if (newX < leftBound) newX = leftBound;
        if (newX + newW > rightBound) newX = rightBound - newW;
        rect = new Rect(newX, rect.getY(), newW, rect.getHeight());
    }

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public void update() {
        if (leftPressed) moveLeft();
        if (rightPressed) moveRight();
    }

    public KeyListener getKeyListener() {
        return new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
            }

            @Override
            public void keyTyped(KeyEvent e) {}
        };
    }


}
