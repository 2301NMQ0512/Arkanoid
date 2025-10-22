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
import powerup.PowerUp;
import powerup.PowerUpType;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class GameLevel extends JPanel {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Timer timer;
    private int width;
    private int height;
    private Paddle paddle;
    private Ball ball;
    private List<Block> blocks = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();

    private int score = 0;
    private int lives = 3;

    private boolean fastballActive = false;
    private Timer fastballTimer;

    public GameLevel(int width, int height) {
        this.width = width; this.height = height;
        setSize(width, height);
        setFocusable(true);
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        environment.setBounds(0, width, 0, height);
        environment.setBallLostHandler(() -> SwingUtilities.invokeLater(() -> handleBallLost()));
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
        Velocity v = new Velocity(4, -4);
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
                block.setGameLevel(this);
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
        paddle.update();
        // update powerups separately (they are also sprites but we manage add/remove)
        for (PowerUp p : new ArrayList<>(powerUps)) {
            p.timePassed();
        }

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

        // draw powerups
        for (PowerUp p : new ArrayList<>(powerUps)) {
            p.drawOn(g);
        }

        sprites.drawAllOn(g);

        // HUD: score and lives
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, 10, 40);
    }

    // called by Block when it's destroyed
    public void addScore(int delta) {
        this.score += delta;
    }

    public void addPowerUp(PowerUp p) {
        powerUps.add(p);
        sprites.addSprite(p);
    }

    public void removePowerUp(PowerUp p) {
        powerUps.remove(p);
        sprites.removeSprite(p);
    }

    public Paddle getPaddle() { return paddle; }
    public int getHeight() { return height; }

    // when powerup is collected by paddle
    public void onPowerUpCollected(PowerUp p) {
        PowerUpType t = p.getType();
        switch (t) {
            case EXPAND:
                paddle.expand(60);
                new javax.swing.Timer(8000, e -> {
                    paddle.expand(-60); // thu nhỏ lại
                }) {{
                    setRepeats(false);
                    start();
                }};
                break;

            case FASTBALL:
                activateFastball(8000); // 8 seconds
                break;
            case EXTRA_LIFE:
                lives += 1;
                break;
        }
    }

    private void activateFastball(int durationMs) {
        if (fastballActive) {
            // restart timer
            if (fastballTimer != null) fastballTimer.stop();
        } else {
            // multiply ball velocity
            ball.getVelocity().setDx(ball.getVelocity().getDx() * 1.6);
            ball.getVelocity().setDy(ball.getVelocity().getDy() * 1.6);
            fastballActive = true;
        }
        fastballTimer = new Timer(durationMs, e -> {
            // revert speed
            ball.getVelocity().setDx(ball.getVelocity().getDx() / 1.6);
            ball.getVelocity().setDy(ball.getVelocity().getDy() / 1.6);
            fastballActive = false;
            fastballTimer.stop();
        });
        fastballTimer.setRepeats(false);
        fastballTimer.start();
    }

    // ball lost handling
    private void handleBallLost() {
        lives -= 1;
        if (lives <= 0) {
            timer.stop();
            // simple game over dialog
            JOptionPane.showMessageDialog(this, "Game Over\nScore: " + score);
            return;
        }
        resetBallAndPaddle();
    }

    private void resetBallAndPaddle() {
        // reset paddle center
        double paddleW = paddle.getCollisionRectangle().getWidth();
        double newPaddleX = (width - paddleW) / 2.0;
        double paddleY = paddle.getCollisionRectangle().getY();
        // recreate paddle rect to center
        Rect newP = new Rect(newPaddleX, paddleY, paddleW, paddle.getCollisionRectangle().getHeight());
        // remove old from environment then create new
        environment.removeCollidable(paddle);
        paddle = new Paddle(newP, Color.BLUE, 12, 0, width);
        environment.addCollidable(paddle);
        addKeyListener(paddle.getKeyListener());

        // reset ball to center with default velocity
        ball.getCenter().setX(width / 2.0);
        ball.getCenter().setY(height / 2.0);
        ball.setVelocity(new Velocity(4, -4));
    }
}
