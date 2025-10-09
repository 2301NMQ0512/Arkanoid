import ball.Ball;
import ball.Velocity;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import game.Sprite;

import java.awt.Color;


public class Paddle implements Sprite, Collideable{
    private static int step;
    private Color color;
    private Rectangle paddle;
    private static double guiWidthLeft = 10;
    private static double guiWidthRight = 780;
    private biuoop.KeyboardSensor keyboard;
    private double[] regionborders = new double[4];
}

public Paddle(Rectangle, paddle, Color color, biuoop,KeyboardSensor keybboard, int thespeed) {
    this.color = color;
    this.paddle = paddle;
    this.keyboard = keyboard;
    step = thespeed;
}

public void moveLeft() {
    if (paddle.getUpperLeft().getX() - step <= guiWidthLeft) {
        this.paddle = new Rectangle(new Point(guiWidthLeft + step + 2,
                paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
    }
    this.paddle = new Rectangle(new Point(paddle.getUpperLeft().getX() - step,
            paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
}

public void moveRight() {
    if (paddle.getUpperLeft().getX() + step + paddle.getWidth() >= guiWidthRight) {
        this.paddle = new Rectangle(new Point(guiWidthRight - paddle.getWidth() - step + 5,
                paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
    }
    this.paddle = new Rectangle(new Point(paddle.getUpperLeft().getX() + step,
            paddle.getUpperLeft().getY()), paddle.getWidth(), paddle.getHeight());
}

public void drawOn(DrawSurface d) {
    d.setColor(color);
    d.drawRectangle((int) this.paddle.getUpperLeft().getX(),
            (int) this.paddle.getUpperLeft().getY(),
            (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
    d.fillRectangle((int) paddle.getUpperLeft().getX(),
            (int) this.paddle.getUpperLeft().getY(),
            (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
    d.setColor(Color.black);
    d.drawRectangle((int) this.paddle.getUpperLeft().getX(),
            (int) this.paddle.getUpperLeft().getY(),
            (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
}

public void timePassed() {
    if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
        this.moveLeft();
    }
    if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
        this.moveRight();
    }
}

public Rectangle getCollisionRectangle() {
    return this.paddle;
}

public int checkRegion(Point collisionPoint) {
    double paddle StartingX = paddle.getUpperleft().getX();
    
}
