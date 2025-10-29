package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import collidable.Block;
import collidable.Paddle;
import ball.Ball;
import powerup.PowerUpManager;
import powerup.PowerUp;
import powerup.PowerUpType;

public class GameLevel extends JPanel {
    private final int width;
    private final int height;
    private Timer timer;
    private GameState state = GameState.START_MENU;

    private Paddle paddle;
    private Ball ball;
    private java.util.List<Block> blocks;
    private int lives = 3;
    private int score = 0;
    private boolean ballAttached = true;
    private final Random rand = new Random();

    private PowerUpManager powerUpManager;

    public GameLevel(int w, int h) {
        this.width = w; this.height = h;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.BLACK);
        initGame();

        timer = new Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (state == GameState.PLAYING) updateGame();
                repaint();
            }
        });
        timer.start();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { handleKey(e); }
            public void keyReleased(KeyEvent e) { handleKeyRelease(e); }
        });
    }

    private void initGame() {
        paddle = new Paddle(width/2 - 60, height - 60, 120, 12, width);
        ball = new Ball(paddle.getX() + paddle.getWidth()/2 - 8, paddle.getY() - 16, 16);
        createBlocks();
        lives = 3; score = 0; ballAttached = true;
        powerUpManager = new PowerUpManager(height);
    }

    private void createBlocks() {
        blocks = new ArrayList<Block>();
        int rows = 6, cols = 10;
        int blockW = 64, blockH = 20, padding = 8;
        int startX = (width - (cols * (blockW + padding) - padding)) / 2;
        int startY = 60;
        Color[] palette = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA};
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) {
            int bx = startX + c*(blockW+padding);
            int by = startY + r*(blockH+padding);
            blocks.add(new Block(bx, by, blockW, blockH, palette[r%palette.length]));
        }
    }

    private void handleKey(KeyEvent e) {
        int k = e.getKeyCode();
        if (state == GameState.START_MENU) {
            if (k == KeyEvent.VK_ENTER) { state = GameState.PLAYING; ballAttached = true; resetBallOnPaddle(); }
        } else if (state == GameState.PLAYING) {
            if (k == KeyEvent.VK_LEFT) paddle.setMovingLeft(true);
            if (k == KeyEvent.VK_RIGHT) paddle.setMovingRight(true);
            if (k == KeyEvent.VK_ESCAPE) state = GameState.PAUSED;
            if (k == KeyEvent.VK_SPACE) { if (ballAttached) launchBall(); }
        } else if (state == GameState.PAUSED) {
            if (k == KeyEvent.VK_ENTER) state = GameState.PLAYING;
            if (k == KeyEvent.VK_Q) { state = GameState.START_MENU; initGame(); }
            if (k == KeyEvent.VK_ESCAPE) state = GameState.PLAYING;
        } else if (state == GameState.GAME_OVER || state == GameState.WIN) {
            if (k == KeyEvent.VK_ENTER) { initGame(); state = GameState.PLAYING; }
            if (k == KeyEvent.VK_Q) { initGame(); state = GameState.START_MENU; }
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) paddle.setMovingLeft(false);
        if (k == KeyEvent.VK_RIGHT) paddle.setMovingRight(false);
    }

    private void updateGame() {
        paddle.update();
        if (ballAttached) {
            ball.setX(paddle.getX() + paddle.getWidth()/2 - ball.getSize()/2);
            ball.setY(paddle.getY() - ball.getSize() - 2);
        } else { ball.update(); }
        checkWallCollisions();
        checkPaddleCollision();
        checkBlockCollisions();

        // update powerup drops
        powerUpManager.update();
        // check catches
        Iterator<PowerUp> pit = powerUpManager.getList().iterator();
        while (pit.hasNext()) {
            PowerUp pu = pit.next();
            if (pu.getRect().intersects(paddle.getRect())) {
                activatePowerUp(pu.getType());
                pu.deactivate();
                pit.remove();
            }
        }

        if (ball.getY() > height) {
            lives--;
            if (lives <= 0) { state = GameState.GAME_OVER; }
            else { ballAttached = true; resetBallOnPaddle(); }
        }
        if (blocks.isEmpty()) { state = GameState.WIN; }
    }

    private void resetBallOnPaddle() {
        ball.setX(paddle.getX() + paddle.getWidth()/2 - ball.getSize()/2);
        ball.setY(paddle.getY() - ball.getSize() - 2);
        ball.setVx(0); ball.setVy(0);
        ballAttached = true;
    }

    private void launchBall() {
        double speed = 5.0;
        double angle = Math.toRadians(rand.nextInt(60) + 60);
        ball.setVx((int)(speed * Math.cos(angle)));
        ball.setVy(-(int)(speed * Math.sin(angle)));
        ballAttached = false;
    }

    private void checkWallCollisions() {
        if (ball.getX() <= 0) { ball.setX(0); ball.setVx(-ball.getVx()); }
        else if (ball.getX() + ball.getSize() >= width) { ball.setX(width - ball.getSize()); ball.setVx(-ball.getVx()); }
        if (ball.getY() <= 0) { ball.setY(0); ball.setVy(-ball.getVy()); }
    }

    private void checkPaddleCollision() {
        if (ball.getRect().intersects(paddle.getRect())) {
            ball.setY(paddle.getY() - ball.getSize() - 1);
            ball.setVy(-Math.abs(ball.getVy()));
            double hitPos = (ball.getX() + ball.getSize()/2.0) - (paddle.getX() + paddle.getWidth()/2.0);
            double fraction = hitPos / (paddle.getWidth()/2.0);
            double maxH = 6.0;
            ball.setVx((int)(fraction * maxH));
            if (Math.abs(ball.getVy()) < 3) ball.setVy(ball.getVy() < 0 ? -3 : 3);
        }
    }

    private void checkBlockCollisions() {
        Iterator<Block> it = blocks.iterator();
        while (it.hasNext()) {
            Block b = it.next();
            if (b.isDestroyed()) continue;
            if (ball.getRect().intersects(b.getRect())) {
                Rectangle intersect = ball.getRect().intersection(b.getRect());
                if (intersect.getWidth() >= intersect.getHeight()) ball.setVy(-ball.getVy());
                else ball.setVx(-ball.getVx());
                b.setDestroyed(true);
                it.remove();
                score += 100;
                // spawn powerup chance
                powerUpManager.spawnRandomAt(b.getX(), b.getY());
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (state == GameState.START_MENU) drawStartMenu(g);
        else {
            drawPlayField(g);
            if (state == GameState.PAUSED) drawPauseOverlay(g);
            if (state == GameState.GAME_OVER) drawGameOver(g);
            if (state == GameState.WIN) drawWin(g);
        }
    }

    private void drawStartMenu(Graphics2D g) {
        g.setColor(Color.BLACK); g.fillRect(0,0,width,height);
        g.setColor(Color.WHITE); g.setFont(new Font("SansSerif", Font.BOLD, 36));
        drawCentered(g, "ARKANOID", width/2, height/2 - 80);
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        drawCentered(g, "Press ENTER to Start", width/2, height/2 - 20);
        drawCentered(g, "Left/Right to move, SPACE to launch, ESC to pause", width/2, height/2 + 10);
    }

    private void drawPlayField(Graphics2D g) {
        g.setColor(Color.DARK_GRAY.darker()); g.fillRect(0,0,width,height);
        for (Block b : blocks) b.draw(g);
        powerUpManager.draw(g);
        paddle.draw(g); ball.draw(g);
        g.setColor(Color.WHITE); g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString("Lives: " + lives, 12, height - 12);
        g.drawString("Score: " + score, width - 120, height - 12);

        // HUD active powerups
        int hudX = 12; int hudY = 24; g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.setColor(Color.WHITE); g.drawString("ACTIVE POWER-UPS:", hudX, hudY);
        int line = 1;
        long fb = ball.getFastRemainingMillis();
        if (fb > 0) { double secs = fb/1000.0; g.setColor(Color.RED); g.drawString(String.format("FAST BALL - %.1fs", secs), hudX, hudY + (line*18)); line++; g.setColor(Color.WHITE); }
        long fp = paddle.getFastRemainingMillis();
        if (fp > 0) { double secs = fp/1000.0; g.setColor(Color.BLUE); g.drawString(String.format("FAST PADDLE - %.1fs", secs), hudX, hudY + (line*18)); line++; g.setColor(Color.WHITE); }
        long ep = paddle.getExpandRemainingMillis();
        if (ep > 0) { double secs = ep/1000.0; g.setColor(Color.GREEN.darker()); g.drawString(String.format("EXPAND PADDLE - %.1fs", secs), hudX, hudY + (line*18)); line++; g.setColor(Color.WHITE); }
    }

    private void drawPauseOverlay(Graphics2D g) {
        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g.setColor(Color.BLACK); g.fillRect(0,0,width,height);
        g.setComposite(old);
        g.setColor(Color.WHITE); g.setFont(new Font("SansSerif", Font.BOLD, 36));
        drawCentered(g, "PAUSED", width/2, height/2 - 40);
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        drawCentered(g, "Press ENTER to Resume, Q to quit to menu, ESC to resume", width/2, height/2 + 10);
    }

    private void drawGameOver(Graphics2D g) {
        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g.setColor(Color.BLACK); g.fillRect(0,0,width,height);
        g.setComposite(old);
        g.setColor(Color.RED); g.setFont(new Font("SansSerif", Font.BOLD, 48));
        drawCentered(g, "GAME OVER", width/2, height/2 - 40);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20)); g.setColor(Color.WHITE);
        drawCentered(g, "Score: " + score, width/2, height/2 + 10);
        drawCentered(g, "Press ENTER to Retry or Q for Main Menu", width/2, height/2 + 50);
    }

    private void drawWin(Graphics2D g) {
        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g.setColor(Color.BLACK); g.fillRect(0,0,width,height);
        g.setComposite(old);
        g.setColor(Color.GREEN.brighter()); g.setFont(new Font("SansSerif", Font.BOLD, 48));
        drawCentered(g, "YOU WIN!", width/2, height/2 - 40);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20)); g.setColor(Color.WHITE);
        drawCentered(g, "Score: " + score, width/2, height/2 + 10);
        drawCentered(g, "Press ENTER to Play Again or Q for Main Menu", width/2, height/2 + 50);
    }

    private void drawCentered(Graphics2D g, String s, int cx, int cy) {
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(s);
        int h = fm.getAscent();
        g.drawString(s, cx - w/2, cy + h/2 - 4);
    }

    private void activatePowerUp(PowerUpType type) {
        int millis = 3000;
        if (type == PowerUpType.EXPAND_PADDLE) {
            paddle.applyExpand(millis);
        } else if (type == PowerUpType.FAST_PADDLE) {
            paddle.applyFast(millis);
        } else if (type == PowerUpType.FAST_BALL) {
            ball.applyFast(1.3, millis);
        }
    }
}

enum GameState { START_MENU, PLAYING, PAUSED, GAME_OVER, WIN }
