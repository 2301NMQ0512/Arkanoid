package collidable;

import ball.Ball;
import ball.Velocity;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import game.Sprite;

import java.awt.Color;

public class Paddle implements Sprite, Collidable {
    private int thespeed;
    private final Color color;
    private Rectangle Paddle;
    private static final double guiWidthLeft = 10;


    private static final double guiWidthRight = 790; // <-- Đã sửa từ 780


    private final biuoop.KeyboardSensor keyboard;
    private final double[] regionBorders = new double[4];


    private final int originalSpeed;
    private final double originalWidth;
    private long expandTimer;
    private long speedTimer;
    private static final long POWERUP_DURATION = 3000;

    public Paddle(Rectangle Paddle, Color color, biuoop.KeyboardSensor keyboard, int thespeed) {
        this.color = color;
        this.Paddle = Paddle;
        this.keyboard = keyboard;
        this.thespeed = thespeed;
        this.originalSpeed = thespeed;
        this.originalWidth = Paddle.getWidth();
        this.expandTimer = 0;
        this.speedTimer = 0;
        this.updateRegionBorders();
    }

    public void moveLeft() {
        if (this.Paddle.getUpperLeft().getX() > guiWidthLeft) {
            double newX = this.Paddle.getUpperLeft().getX() - this.thespeed;
            if (newX < guiWidthLeft) {
                newX = guiWidthLeft;
            }
            this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                    this.Paddle.getWidth(), this.Paddle.getHeight());
        }
    }

    public void moveRight() {
        // (Logic di chuyển sang phải giờ sẽ hoạt động chính xác đến 790)
        if (this.Paddle.getUpperLeft().getX() + this.Paddle.getWidth() < guiWidthRight) {
            double newX = this.Paddle.getUpperLeft().getX() + this.thespeed;
            if (newX + this.Paddle.getWidth() > guiWidthRight) {
                newX = guiWidthRight - this.Paddle.getWidth();
            }
            this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                    this.Paddle.getWidth(), this.Paddle.getHeight());
        }
    }

    private void updateRegionBorders() {
        double x = this.Paddle.getUpperLeft().getX();
        double width = this.Paddle.getWidth();
        this.regionBorders[0] = x + (width / 5);
        this.regionBorders[1] = x + (width / 5) * 2;
        this.regionBorders[2] = x + (width / 5) * 3;
        this.regionBorders[3] = x + (width / 5) * 4;
    }


    public void timePassed() {
        this.updateRegionBorders();
        checkTimers();
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    public Rectangle getCollisionRectangle() {
        return this.Paddle;
    }

    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double colX = collisionPoint.getX();
        double speed = currentVelocity.getSpeed();
        if (colX < this.regionBorders[0]) {
            return Velocity.fromAngleAndSpeed(-60, speed);
        } else if (colX < this.regionBorders[1]) {
            return Velocity.fromAngleAndSpeed(-30, speed);
        } else if (colX < this.regionBorders[2]) {
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        } else if (colX < this.regionBorders[3]) {
            return Velocity.fromAngleAndSpeed(30, speed);
        } else {
            return Velocity.fromAngleAndSpeed(60, speed);
        }
    }

    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }


    @Override
    public void drawOn(DrawSurface d) {
        double totalX = this.Paddle.getUpperLeft().getX();
        double totalY = this.Paddle.getUpperLeft().getY();
        double totalWidth = this.Paddle.getWidth();
        double totalHeight = this.Paddle.getHeight();

        Color sideColor = Color.GRAY;
        double sideWidth = 20;

        if (sideWidth * 2 > totalWidth) {
            sideWidth = totalWidth / 3;
        }

        double centerWidth = totalWidth - (2 * sideWidth);

        Rectangle leftRect = new Rectangle(new Point(totalX, totalY), sideWidth, totalHeight);
        Rectangle centerRect = new Rectangle(new Point(totalX + sideWidth, totalY), centerWidth, totalHeight);
        Rectangle rightRect = new Rectangle(new Point(totalX + sideWidth + centerWidth, totalY), sideWidth, totalHeight);

        draw3DBlock(d, leftRect, sideColor);
        draw3DBlock(d, centerRect, this.color);
        draw3DBlock(d, rightRect, sideColor);
    }


    private void draw3DBlock(DrawSurface d, Rectangle rect, Color mainColor) {
        int x = (int) rect.getUpperLeft().getX();
        int y = (int) rect.getUpperLeft().getY();
        int width = (int) rect.getWidth();
        int height = (int) rect.getHeight();
        Color highlightColor = mainColor.brighter();
        Color shadowColor = mainColor.darker().darker();

        d.setColor(mainColor);
        d.fillRectangle(x, y, width, height);
        d.setColor(highlightColor);
        d.fillRectangle(x, y, width, 2);
        d.fillRectangle(x, y, 2, height);
        d.setColor(shadowColor);
        d.fillRectangle(x, y + height - 2, width, 2);
        d.fillRectangle(x + width - 2, y, 2, height);
    }


    public void expandPaddle() {
        resetSize();
        double currentWidth = this.Paddle.getWidth();
        double newWidth = currentWidth * 1.5;
        double currentX = this.Paddle.getUpperLeft().getX();
        double newX = currentX - (newWidth - currentWidth) / 2;

        if (newX < guiWidthLeft) {
            newX = guiWidthLeft;
        }
        if (newX + newWidth > guiWidthRight) {
            newX = guiWidthRight - newWidth;
        }

        this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                newWidth, this.Paddle.getHeight());

        this.expandTimer = System.currentTimeMillis() + POWERUP_DURATION;
    }


    public void increaseSpeed() {
        resetSpeed();
        this.thespeed = this.originalSpeed * 2;
        this.speedTimer = System.currentTimeMillis() + POWERUP_DURATION;
    }


    private void checkTimers() {
        long currentTime = System.currentTimeMillis();

        if (this.expandTimer > 0 && currentTime > this.expandTimer) {
            resetSize();
            this.expandTimer = 0;
        }

        if (this.speedTimer > 0 && currentTime > this.speedTimer) {
            resetSpeed();
            this.speedTimer = 0;
        }
    }


    private void resetSize() {
        double currentWidth = this.Paddle.getWidth();
        if (currentWidth == this.originalWidth) {
            return;
        }
        double currentX = this.Paddle.getUpperLeft().getX();
        double newX = currentX + (currentWidth - this.originalWidth) / 2;
        this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                this.originalWidth, this.Paddle.getHeight());
    }


    private void resetSpeed() {
        this.thespeed = this.originalSpeed;
    }
}