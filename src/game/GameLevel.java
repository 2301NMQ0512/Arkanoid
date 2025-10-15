package game;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ball.Ball;
import ball.Velocity;
import geometry.Point;
import geometry.Rect;
import collidable.Block;
import collidable.Paddle;

import java.util.ArrayList;
import java.util.List;

public class GameLevel extends JPanel {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Timer timer;
    private int width;
    private int height;
    private Paddle paddle;
    private Ball ball;
    private List<Block> blocks = new ArrayList<>();

    public GameLevel(int width, int height) {
        this.width = width; this.height = height;
        setSize(width, height);
        setFocusable(true);
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        environment.setBounds(0, width, 0, height);
        initialize();
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        });
    }

    public void start() { timer.start(); }

    private void initialize() {
        int paddleW = 100, paddleH = 12;
        Rect paddleRect = new Rect((width - paddleW) / 2.0, height - 60, paddleW, paddleH);
        paddle = new Paddle(paddleRect, Color.BLUE, 12, 0, width);
        environment.addCollidable(paddle);
        addKeyListener(paddle.getKeyListener());

        Point ballCenter = new Point(width / 2.0, height / 2.0);
        Velocity v = new Velocity(3, -3);
        ball = new Ball(ballCenter, 8, Color.RED, v, environment);
        sprites.addSprite(ball);

        int rows = 3;
        int cols = 8;
        int brickW = 80;
        int brickH = 25;
        int startX = 20;
        int startY = 50;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = startX + c * (brickW + 5);
                double y = startY + r * (brickH + 5);
                Rect rect = new Rect(x, y, brickW, brickH);
                Block block = new Block(rect, Color.ORANGE);
                block.setOnDestroy(() -> {
                    environment.removeCollidable(block);
                    blocks.remove(block);
                });
                environment.addCollidable(block);
                blocks.add(block);
            }
        }
    }

    private void gameLoop() {
        sprites.notifyAllTimePassed();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        for (Block b : new ArrayList<>(blocks)) {
            b.drawOn(g);
        }

        paddle.drawOn(g);
        sprites.drawAllOn(g);
    }
}
