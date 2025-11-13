package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
// --- THAY ĐỔI: Import PatrollingBlock ---
import collidable.PatrollingBlock;
// (Bạn có thể xóa import này nếu không dùng nữa)
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Level 4: "Rick Roll" với các khối gạch DI CHUYỂN QUA LẠI
 * và nền GIF động.
 */
public class LevelFour implements LevelInformation {

    // --- XÓA BỎ: activeBlocks và blockPositions ---

    // (PATTERN giữ nguyên)
    private static final String[] PATTERN = {
            " RRR   III   CCC   K  K ",
            " R  R   I   C   C  K K  ",
            " RRR    I   C      KK   ",
            " R  R   I   C   C  K K  ",
            " R  R  III   CCC   K  K ",
            "                         ",
            " RRR    OOO   L     L   ",
            " R  R  O   O  L     L   ",
            " RRR   O   O  L     L   ",
            " R  R  O   O  L     L   ",
            " R  R   OOO   LLLL  LLLL"
    };

    private static final int BLOCK_SIZE = 20;
    private static final int START_X = 150;
    private static final int START_Y = 100;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;


    public LevelFour() {
        // (Xóa khởi tạo danh sách cũ)
    }

    // (numberOfBalls, initialBallVelocities, paddleSpeed, paddleWidth, levelName, getBackgroundMusicPath giữ nguyên)
    @Override
    public int numberOfBalls() { return 3; }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        velocities.add(Velocity.fromAngleAndSpeed(-20, 6));
        velocities.add(Velocity.fromAngleAndSpeed(0, 6));
        velocities.add(Velocity.fromAngleAndSpeed(20, 6));
        return velocities;
    }

    @Override
    public int paddleSpeed() { return 13; }

    @Override
    public int paddleWidth() { return 200; }

    @Override
    public String levelName() {
        return "Level 4: ...Never Gonna";
    }

    @Override
    public String getBackgroundMusicPath() {
        return "resources/sounds/level4_music.wav";
    }

    /**
     * THAY ĐỔI: timePassed() của Sprite nền
     * giờ đây CHỈ phát ảnh GIF.
     */
    @Override
    public Sprite getBackground() {
        // (Logic tải GIF giữ nguyên)
        List<BufferedImage> backgroundFrames = new ArrayList<>();
        try {
            System.out.println("Level 4: Bắt đầu tải các khung hình GIF...");
            for (int i = 1; i <= 26; i++) { // (Giả sử bạn có 36 khung hình, 1-36)
                String framePath = "resources/backgrounds/level_4_gif/frame (" + i + ").png";
                BufferedImage originalFrame = ImageIO.read(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(framePath)));
                BufferedImage scaledFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, originalFrame.getType());
                Graphics2D g = scaledFrame.createGraphics();
                g.drawImage(originalFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                g.dispose();
                backgroundFrames.add(scaledFrame);
            }
            System.out.println("Level 4: Đã tải " + backgroundFrames.size() + " khung hình GIF.");
        } catch (Exception e) {
            System.err.println("Không thể tải GIF cho Level 4: " + e.getMessage());
        }

        final List<BufferedImage> finalFrames = backgroundFrames;


        return new Sprite() {
            // (Chỉ giữ lại các biến cho GIF)
            private int gifFrameCounter = 0;
            private int currentGifFrame = 0;
            private static final int GIF_ANIMATION_DELAY = 3;

            @Override
            public void drawOn(DrawSurface d) {
                if (!finalFrames.isEmpty()) {
                    d.drawImage(0, 0, finalFrames.get(this.currentGifFrame));
                } else {
                    d.setColor(Color.BLACK);
                    d.fillRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
            }

            /**
             * THAY ĐỔI: Chỉ chứa logic GIF.
             */
            @Override
            public void timePassed() {
                // --- A. LOGIC ẢNH ĐỘNG NỀN ---
                this.gifFrameCounter++;
                if (this.gifFrameCounter >= GIF_ANIMATION_DELAY) {
                    this.gifFrameCounter = 0;
                    this.currentGifFrame++;
                    if (this.currentGifFrame >= finalFrames.size()) {
                        this.currentGifFrame = 0; // Quay vòng
                    }
                }


            }
        };
    }


    /**
     * THAY ĐỔI: Tạo các PatrollingBlock với HP ngẫu nhiên.
     */
    @Override
    public List<Block> blocks() {
        // (Xóa clear() các danh sách cũ)

        List<Block> blockList = new ArrayList<>();
        java.util.Random rand = new java.util.Random();
        int moveRange = 200; // Các khối gạch sẽ di chuyển 30px qua lại

        for (int row = 0; row < PATTERN.length; row++) {
            for (int col = 0; col < PATTERN[row].length(); col++) {

                if (PATTERN[row].charAt(col) != ' ') {
                    double x = START_X + (col * BLOCK_SIZE);
                    double y = START_Y + (row * BLOCK_SIZE);
                    Point pos = new Point(x, y);
                    Rectangle rect = new Rectangle(pos, BLOCK_SIZE, BLOCK_SIZE);

                    Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                    int hitPoints = rand.nextInt(4) + 1; // HP 1-4

                    // --- THAY ĐỔI: Tạo PatrollingBlock ---
                    Block block = new PatrollingBlock(rect, color, hitPoints, moveRange);

                    blockList.add(block);
                    // (Xóa add() vào các danh sách cũ)
                }
            }
        }

        return blockList;
    }

    /**
     * Đếm số khối gạch.
     * (Giữ nguyên, không thay đổi)
     */
    @Override
    public int numberOfBlocksToRemove() {
        int count = 0;
        for (String s : PATTERN) {
            for (int col = 0; col < s.length(); col++) {
                if (s.charAt(col) != ' ') {
                    count++;
                }
            }
        }
        return count;
    }
    @Override
    public String getBallImagePath() {
        // (Bạn phải tạo tệp ảnh này)
        return "resources/sprites/ball_level_four_gif/";
    }
}