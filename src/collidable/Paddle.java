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

