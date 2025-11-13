package game;

import animation.Animation;
import animation.AnimationRunner;
// import animation.PauseScreen; // <-- ĐÃ XÓA
// import animation.KeyPressStoppableAnimation; // <-- ĐÃ XÓA
import animation.CountdownAnimation;

// --- Imports cho Menu và Pause (ĐÃ THÊM) ---
import menu.MouseSensor;
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

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

// Import cho Power-Up
import powerup.PowerUp;
import powerup.PowerUpManager;
import powerup.PowerUpType;
import listeners.PowerUpSpawner;

/**
 * Môi trường game chính, nhận một level, tạo cửa sổ, các đối tượng và thiết kế của chúng.
 * (ĐÃ CẬP NHẬT để tích hợp màn hình tạm dừng mới)
 */
public class GameLevel implements Animation {
    private LevelInformation level;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter countBlocks;
    private Counter countBalls;
    private Counter countScore;
    private BlockRemover blockRemover;
    private BallRemover ballRemover;
    private ScoreTrackingListener scoreTracker;
    /**
     * Chiều rộng không đổi của cửa sổ GUI chạy game.
     */
    private static double guiWidth = 800;
    /**
     * Chiều cao không đổi của cửa sổ GUI chạy game.
     */
    private static double guiHeight = 600;
    private static Point guiStart = new Point(0, 0);
    private static double blockWidth = 35;
    private double paddleWidth;
    private double paddleHeight;
    private AnimationRunner runner;
    private boolean running;
    private KeyboardSensor keyboard;
    private MouseSensor mouse; // <-- THÊM MỚI
    private MenuSelection finalSelection; // <-- THÊM MỚI: Để lưu lựa chọn khi tạm dừng

    // --- Biến thành viên cho Power-Up ---
    private PowerUpManager powerUpManager;
    private Paddle paddle;
    private List<Ball> balls;

    /**
     * Khởi tạo một level mới, nhận thông tin và tạo các sprite.
     * (Hàm khởi tạo ĐÃ CẬP NHẬT để nhận MouseSensor)
     *
     * @param level the given level
     * @param ar    the animation runner
     * @param ks    the keyboardSensor
     * @param ms    the mouseSensor (MỚI)
     * @param score the given score
     */
    public GameLevel(LevelInformation level, AnimationRunner ar, KeyboardSensor ks, MouseSensor ms, Counter score) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.keyboard = ks;
        this.runner = ar;
        this.mouse = ms; // <-- THÊM MỚI
        countScore = score;
        this.level = level;
        paddleWidth = level.paddleWidth();
        paddleHeight = 20;
        this.balls = new ArrayList<Ball>();
        this.finalSelection = MenuSelection.NONE; // <-- THÊM MỚI
    }

    /**
     * Thêm collidable vào game.
     *
     * @param c the collidable
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * Xóa một collidable khỏi môi trường game.
     *
     * @param c the collidable
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * Thêm sprite vào game.
     *
     * @param s the sprite
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * Xóa sprite khỏi game.
     *
     * @param s the sprite
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    /**
     * Tạo các khối biên bên cạnh để ngăn bóng bay ra ngoài.
     * Cũng tạo ra một "vùng chết", khối dưới cùng sẽ loại bỏ bất kỳ quả bóng nào chạm vào nó.
     */
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

    /**
     * Tạo và thêm các quả bóng trắng với vận tốc cho trước vào game.
     */
    public void createBalls() {
        List<Velocity> velocities = level.initialBallVelocities();
        for (int i = 0; i < velocities.size(); i++) {
            int ballRadius = 5;
            Ball ball = new Ball(new Point((float) guiWidth / 2, guiHeight - blockWidth - paddleHeight),
                    ballRadius, Color.white, environment,
                    velocities.get(i));
            ball.addToGame(this);
            this.balls.add(ball);
        }
    }

    /**
     * Tạo và thêm các khối vào game theo mẫu của mỗi level.
     */
    public void createBlocks() {
        HitListener powerUpSpawner = new PowerUpSpawner(this.powerUpManager);
        for (Block block : this.level.blocks()) {
            block.addToGame(this);
            block.addHitListener(blockRemover);
            block.addHitListener(scoreTracker);
            block.addHitListener(powerUpSpawner);
        }
    }

    /**
     * Tạo bảng điểm.
     */
    private void scoreIndicator() {
        Rectangle rectangle = new Rectangle(guiStart, guiWidth, 15);
        Block block = new Block(rectangle, Color.white);
        block.addToGame(this);
        ScoreIndicator scoreBoard = new ScoreIndicator(countScore);
        scoreBoard.addToGame(this);
    }

    /**
     * Hiển thị tên level ở góc trên bên trái.
     */
    private void levelNameIndicator() {
        LevelNameIndicator levelName = new LevelNameIndicator(level.levelName());
        levelName.addToGame(this);
    }

    /**
     * Tạo một paddle có thể di chuyển bằng bàn phím.
     *
     * @param theKeyboard the given keyboard.
     */
    public void createPaddle(KeyboardSensor theKeyboard) {
        Rectangle paddleRect = new Rectangle(new Point((guiWidth / 2) - (paddleWidth / 2), guiHeight - blockWidth),
                paddleWidth, paddleHeight);
        this.paddle = new Paddle(paddleRect, Color.yellow, theKeyboard, level.paddleSpeed());
        this.paddle.addToGame(this);
    }

    /**
     * Khởi tạo game.
     */
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

    /**
     * Chạy một khung hình (frame) của game.
     * (ĐÃ CẬP NHẬT VỚI LOGIC TẠM DỪNG MỚI)
     */
    @Override
    public void doOneFrame(DrawSurface d) {
        // --- 1. Kiểm tra Tạm dừng (Pause) ---
        if (this.keyboard.isPressed("escape")) {
            // Tạo màn hình tạm dừng
            PauseScreen pauseScreen = new PauseScreen(this.keyboard, this.mouse);

            // Chạy màn hình tạm dừng. Nó sẽ lặp lại cho đến khi người dùng chọn.
            this.runner.run(pauseScreen);

            // Lấy lựa chọn của người dùng
            MenuSelection choice = pauseScreen.getSelection();

            // Xử lý lựa chọn
            switch (choice) {
                case PLAY:
                    // Đây là "CONTINUE". Không làm gì cả, game sẽ tự động tiếp tục.
                    break;
                case RESTART:
                    // Người dùng muốn "REPLAY". Dừng vòng lặp game.
                    this.running = false;
                    this.finalSelection = MenuSelection.RESTART; // Lưu lựa chọn
                    break;
                case EXIT:
                    // Người dùng muốn "EXIT TO MENU". Dừng vòng lặp game.
                    this.running = false;
                    this.finalSelection = MenuSelection.EXIT; // Lưu lựa chọn
                    break;
                case NONE:
                default:
                    // (Chỉ là 'Continue')
                    break;
            }
        }

        // --- 2. Logic Game Bình thường ---
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed();

        // Kiểm tra va chạm paddle với power-up
        this.checkPowerUpCollisions();

        // --- 3. Kiểm tra điều kiện Thắng/Thua ---
        if (countBlocks.getValue() == 0 || countBalls.getValue() == 0) {
            this.running = false;
            // finalSelection vẫn là NONE, điều này là đúng.
        }
    }

    /**
     * Kiểm tra xem vòng lặp animation có nên dừng lại không.
     *
     * @return true hoặc false
     */
    @Override
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * Chơi một lượt.
     * (ĐÃ CẬP NHẬT để trả về MenuSelection)
     *
     * @return Lựa chọn của người dùng từ menu tạm dừng (hoặc NONE nếu game kết thúc bình thường)
     */
    public MenuSelection playOneTurn() {
        createBalls();
        this.runner.run(new CountdownAnimation(2, 3, sprites));
        this.running = true;
        this.runner.run(this);

        // Trả về lựa chọn cuối cùng (NONE nếu game kết thúc bình thường)
        return this.finalSelection;
    }

    /**
     * Lấy số lượng khối hiện tại.
     *
     * @return số lượng
     */
    public Counter getBlocksCount() {
        return countBlocks;
    }

    /**
     * Lấy số lượng bóng hiện tại.
     *
     * @return số lượng
     */
    public Counter getBallsCount() {
        return countBalls;
    }

    /**
     * Lấy chiều cao GUI.
     *
     * @return chiều cao
     */
    public static double getGuiHeight() {
        return guiHeight;
    }

    /**
     * Lấy chiều rộng GUI.
     *
     * @return chiều rộng
     */
    public static double getGuiWidth() {
        return guiWidth;
    }


    // ----------------------------------------------------------------
    // <-- CÁC PHƯƠNG THỨC TRỢ GIÚP CHO POWER-UP (Không thay đổi) -->
    // ----------------------------------------------------------------

    /**
     * Kiểm tra va chạm giữa paddle và bất kỳ power-up nào đang hoạt động.
     */
    private void checkPowerUpCollisions() {
        Rectangle paddleRect = this.paddle.getCollisionRectangle();
        Iterator<PowerUp> it = this.powerUpManager.getList().iterator();

        while (it.hasNext()) {
            PowerUp p = it.next();
            if (!p.isActive()) continue;

            Rectangle powerUpRect = p.getRect();

            if (this.intersects(paddleRect, powerUpRect)) {
                applyPowerUpEffect(p.getType());
                p.deactivate();
                it.remove();
            }
        }
    }

    /**
     * Phương thức trợ giúp để áp dụng hiệu ứng power-up.
     */
    private void applyPowerUpEffect(PowerUpType type) {
        switch (type) {
            case EXPAND_PADDLE:
                this.paddle.expand();
                break;
            case FAST_PADDLE:
                this.paddle.increaseSpeed();
                break;
            case FAST_BALL:
                for (Ball b : this.balls) {
                    b.increaseSpeed();
                }
                break;
        }
    }

    /**
     * Hàm kiểm tra giao nhau đơn giản cho geometry.Rectangle.
     */
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
}