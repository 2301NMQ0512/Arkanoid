package game;

import animation.Animation;
import animation.AnimationRunner;
import animation.CountdownAnimation;
import animation.PauseScreen;
import menu.MenuSelection;

import ball.Ball;
import ball.Velocity;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collidable.Block;
import collidable.Collidable;
import collidable.Paddle;
import geometry.Point;
import geometry.Rectangle;
import levels.LevelInformation;

// Import cho các Listener
import listeners.LevelNameIndicator;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreIndicator;
import listeners.ScoreTrackingListener;
import listeners.HitListener;
import listeners.SoundPlayingHitListener;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

// Import cho Power-Up
import powerup.PowerUp;
import powerup.PowerUpManager;
import powerup.PowerUpType;
import listeners.PowerUpSpawner;


import sounds.AudioPlayer;

public class GameLevel implements Animation {
    private final LevelInformation level;
    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private Counter countBlocks;
    private Counter countBalls;
    private final Counter countScore;
    private BlockRemover blockRemover;
    private BallRemover ballRemover;
    private ScoreTrackingListener scoreTracker;
    private static final double guiWidth = 800;
    private static final double guiHeight = 600;
    private static final Point guiStart = new Point(0, 0);
    private static final double blockWidth = 35;
    private final double paddleWidth;
    private final double paddleHeight;
    private final AnimationRunner runner;
    private boolean running;
    private final KeyboardSensor keyboard;


    private PowerUpManager powerUpManager;
    private Paddle paddle;
    private final List<Ball> balls;
    private MenuSelection pauseChoice;
    private final AudioPlayer audioPlayer;
    private static final String HIT_SOUND = "resources/sounds/hit.wav"; // <-- THÊM MỚI: Định nghĩa âm thanh va chạm


    private final String ballpath;



    public GameLevel(LevelInformation level, AnimationRunner ar, KeyboardSensor ks, Counter score, AudioPlayer player) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.keyboard = ks;
        this.runner = ar;
        this.countScore = score;
        this.level = level;
        paddleWidth = level.paddleWidth();
        paddleHeight = 20;
        this.balls = new ArrayList<>();
        this.pauseChoice = MenuSelection.NONE;
        this.audioPlayer = player; // <-- THÊM MỚI: Gán AudioPlayer

        this.ballpath = level.getBallImagePath();
    }


    public void addCollidable(Collidable c) { environment.addCollidable(c); }
    public void removeCollidable(Collidable c) { environment.removeCollidable(c); }
    public void addSprite(Sprite s) { sprites.addSprite(s); }
    public void removeSprite(Sprite s) { sprites.removeSprite(s); }



    public void createSideBlocks() {
        Point[] guiBorderPoints = new Point[4];
        guiBorderPoints[0] = guiStart;
        guiBorderPoints[1] = new Point(guiWidth - 10, guiStart.getY());
        guiBorderPoints[2] = new Point(guiStart.getX(), guiHeight - 10);
        guiBorderPoints[3] = guiStart;
        Double[] borderBlockHeight = new Double[4];
        borderBlockHeight[0] = blockWidth - 10;
        borderBlockHeight[1] = guiHeight;
        borderBlockHeight[2] = (double) 1;
        borderBlockHeight[3] = guiHeight;
        Double[] borderBlockWidth = new Double[4];
        borderBlockWidth[0] = guiWidth;
        double blockHeight = 20;
        borderBlockWidth[1] = blockHeight;
        borderBlockWidth[2] = guiWidth;
        borderBlockWidth[3] = blockHeight - 10;
        for (int i = 0; i < 4; i++) {
            Rectangle rectangle = new Rectangle(guiBorderPoints[i], borderBlockWidth[i], borderBlockHeight[i]);
            Block block = new Block(rectangle, Color.gray);
            block.addToGame(this);
            if (i == 2) {
                block.addHitListener(ballRemover);
                block.removeFromSprites(this);
            }
        }
    }


    public void createBalls() {
        List<Velocity> velocities = level.initialBallVelocities();
        for (Velocity velocity : velocities) {
            int ballRadius = 13; // (Lưu ý: 20 là bán kính LỚN)
            Ball ball = new Ball(new Point((float) guiWidth / 2, guiHeight - blockWidth - paddleHeight),
                    ballRadius,
                    environment,
                    velocity,
                    this.ballpath // Truyền đường dẫn ảnh vào đây
            );

            this.balls.add(ball);
        }
    }



    public void createBlocks() {

        HitListener soundListener = new SoundPlayingHitListener(this.audioPlayer, HIT_SOUND);
        HitListener powerUpSpawner = new PowerUpSpawner(this.powerUpManager);

        for (Block block : this.level.blocks()) {
            block.addToGame(this);
            block.addHitListener(blockRemover);
            block.addHitListener(scoreTracker);
            block.addHitListener(powerUpSpawner);
            block.addHitListener(soundListener);
        }
    }


    private void scoreIndicator() {
        Rectangle rectangle = new Rectangle(guiStart, guiWidth, 15);
        Block block = new Block(rectangle, Color.white);
        block.addToGame(this);
        ScoreIndicator scoreBoard = new ScoreIndicator(countScore);
        scoreBoard.addToGame(this);
    }
    private void levelNameIndicator() {
        LevelNameIndicator levelName = new LevelNameIndicator(level.levelName());
        levelName.addToGame(this);
    }
    public void createPaddle(KeyboardSensor theKeyboard) {
        Rectangle paddleRect = new Rectangle(new Point((guiWidth / 2) - (paddleWidth / 2), guiHeight - blockWidth),
                paddleWidth, paddleHeight);
        this.paddle = new Paddle(paddleRect, Color.yellow, theKeyboard, level.paddleSpeed());
        this.paddle.addToGame(this);
    }


    public void initialize() {
        if (level.getBackground() != null) {
            sprites.addSprite(level.getBackground());
        }
        this.powerUpManager = new PowerUpManager((int) guiHeight);
        this.powerUpManager.addToGame(this);
        countBlocks = new Counter(level.numberOfBlocksToRemove());
        countBalls = new Counter(level.numberOfBalls());
        blockRemover = new BlockRemover(this, countBlocks);
        ballRemover = new BallRemover(this, countBalls);
        scoreTracker = new ScoreTrackingListener(countScore);
        createBlocks();
        createSideBlocks();
        createPaddle(keyboard);
        scoreIndicator();
        levelNameIndicator();
    }



    @Override
    public void doOneFrame(DrawSurface d) {

        if (this.keyboard.isPressed("p")) {
            PauseScreen pause = new PauseScreen(this.keyboard);
            this.runner.run(pause);
            this.pauseChoice = pause.getSelection();
        }


        this.sprites.drawAllOn(d);


        for (Ball b : this.balls) {
            b.drawOn(d);
        }


        this.sprites.notifyAllTimePassed();


        for (Ball b : new ArrayList<>(this.balls)) {
            b.timePassed();
        }


        this.checkPowerUpCollisions();

        if (countBlocks.getValue() == 0 || countBalls.getValue() == 0) {
            this.running = false;
        }
    }

    @Override
    public boolean shouldStop() {
        if (this.pauseChoice == MenuSelection.EXIT) {
            return true;
        }
        return !this.running;
    }

    public MenuSelection getPauseChoice() {
        return this.pauseChoice;
    }

    public void playOneTurn() {
        this.pauseChoice = MenuSelection.NONE;
        createBalls();
        this.runner.run(new CountdownAnimation(2, 3, sprites));
        this.running = true;
        this.runner.run(this);
    }


    public Counter getBallsCount() { return countBalls; }

    private void checkPowerUpCollisions() {
        if (this.paddle == null) {
            return;
        }
        Rectangle paddleRect = this.paddle.getCollisionRectangle();
        Iterator<PowerUp> it = this.powerUpManager.getList().iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            if (!p.isActive()) {
                continue;
            }
            Rectangle powerUpRect = p.getRect();
            if (this.intersects(paddleRect, powerUpRect)) {
                applyPowerUpEffect(p.getType());
                p.deactivate();
                it.remove();
            }
        }
    }


    private void applyPowerUpEffect(PowerUpType type) {
        switch (type) {
            case EXPAND_PADDLE:

                this.paddle.expandPaddle();
                break;
            case FAST_PADDLE:
                this.paddle.increaseSpeed();
                break;
            case MULTI_BALL:
                int maxBalls = 8;

                List<Ball> newBalls = new ArrayList<>();


                for (Ball b : new ArrayList<>(this.balls)) {


                    if (this.balls.size() + newBalls.size() >= maxBalls) {
                        break;
                    }


                    Velocity newV = new Velocity(
                            -b.getVelocity().getDx(),
                            b.getVelocity().getDy()
                    );

                    Ball newBall = new Ball(
                            new Point(b.getX(), b.getY()),
                            b.getSize(),
                            this.environment,
                            newV,
                            this.ballpath
                    );
                    newBalls.add(newBall);
                }


                for (Ball nb : newBalls) {

                    this.balls.add(nb);
                    this.countBalls.increase(1);
                }
                break;
        }
    }


    private boolean intersects(Rectangle r1, Rectangle r2) {
        double r1Left = r1.getUpperLeft().getX();
        double r1Right = r1.getUpperLeft().getX() + r1.getWidth();
        double r1Top = r1.getUpperLeft().getY();
        double r1Bottom = r1.getUpperLeft().getY() + r1.getHeight();
        double r2Left = r2.getUpperLeft().getX();
        double r2Right = r2.getUpperLeft().getX() + r2.getWidth();
        double r2Top = r2.getUpperLeft().getY();
        double r2Bottom = r2.getUpperLeft().getY() + r2.getHeight();
        return (r1Left < r2Right && r1Right > r2Left && r1Top < r2Bottom && r1Bottom > r2Top);
    }


    public void removeBallFromList(Ball b) {
        if (this.balls != null) {
            this.balls.remove(b);
        }
    }
}
