package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import collidable.PatrollingBlock; // <-- THÊM MỚI: Import gạch di chuyển
import game.Sprite;
import geometry.Rectangle;
import geometry.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// (Các import ảnh)

import javax.imageio.ImageIO;
import java.util.Objects;


public class LevelTwo implements LevelInformation {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    // --- ASCII Art cho Cúp Vàng World Cup ---
    private static final String[] PATTERN = {
            "       Y     YGY      Y        ",
            "    YYYYYYYYYYYYYYYYYYYYY      ",
            "     YYYYYYYYYYYYYYYYYYY    ",
            "      YYYYYYYYYYYYYYYYY   ",
            "        YYHYYYYYYYHYY   ",
            "         YYYYYYYYYYY    ",
            "           YHYYYHY      ",
            "            YYYYY        ",
            "            YYYYY        ",
            "            YYGYY        ",
            "           GGGGGGG         ",
            "            YYYYY         ",
            "          YYGYYYGYY        ",
            "          YYYYYYYYY        ",
            "         YYYYYYYYYYY        ",
            "        YYYYYYYYYYYYY       ",
            "        YYYYYYYYYYYYY       ",
            "       YYYYYYYYYYYYYYY"
    };

    private static final int BLOCK_SIZE = 20;
    private static final int START_X = 215;
    private static final int START_Y = 80;

    @Override
    public String levelName() {
        return "Level Two: World Cup";
    }

    @Override
    public int numberOfBalls() {
        return 2;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        velocities.add(Velocity.fromAngleAndSpeed(320, 5));
        velocities.add(Velocity.fromAngleAndSpeed(40, 5));
        return velocities;
    }

    @Override
    public int paddleSpeed() {
        return 13;
    }

    @Override
    public int paddleWidth() {
        return 200;
    }

    @Override
    public String getBallImagePath() {
        return "resources/sprites/ball_level_two.png";
    }

    @Override
    public String getBackgroundMusicPath() {
        return "resources/sounds/level2_music.wav";
    }

    @Override
    public Sprite getBackground() {
        // (Logic tải GIF giữ nguyên)
        List<BufferedImage> backgroundFrames = new ArrayList<>();
        try {
            System.out.println("Level 4: Bắt đầu tải các khung hình GIF...");
            for (int i = 0; i <= 103; i++) { // (Giả sử bạn có 36 khung hình, 1-36)
                String framePath = "resources/backgrounds/level_2_gif/frame (" + i + ").png";
                BufferedImage originalFrame = ImageIO.read(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(framePath)));
                BufferedImage scaledFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, originalFrame.getType());
                Graphics2D g = scaledFrame.createGraphics();
                g.drawImage(originalFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                g.dispose();
                backgroundFrames.add(scaledFrame);
            }
            System.out.println("Level 2: Đã tải " + backgroundFrames.size() + " khung hình GIF.");
        } catch (Exception e) {
            System.err.println("Không thể tải GIF cho Level 2: " + e.getMessage());
        }

        final List<BufferedImage> finalFrames = backgroundFrames;

        // Trả về Sprite
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
     * THAY ĐỔI: Tạo các khối gạch DI CHUYỂN (PatrollingBlock).
     */
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        Random rand = new Random();



        // --- THÊM MỚI: Phạm vi di chuyển ---
        int moveRange = 200; // Di chuyển qua lại 100px
        // ----------------------------------

        for (int row = 0; row < PATTERN.length; row++) {
            String line = PATTERN[row];
            for (int col = 0; col < line.length(); col++) {

                char blockType = line.charAt(col);
                if (blockType == ' ') {
                    continue;
                }

                double x = START_X + (col * BLOCK_SIZE);
                double y = START_Y + (row * BLOCK_SIZE);
                Point pos = new Point(x, y);
                Rectangle rect = new Rectangle(pos, BLOCK_SIZE, BLOCK_SIZE);

                int hitPoints = rand.nextInt(4) + 1;

                Block b;

                // --- THAY ĐỔI: Sử dụng PatrollingBlock ---
                if (blockType == 'Y') {
                    b = new PatrollingBlock(rect, Color.lightGray, hitPoints, moveRange);
                } else if (blockType == 'G') {
                    b = new PatrollingBlock(rect, Color.BLUE, hitPoints, moveRange);
                } else {
                    // Gạch dự phòng (nếu c ó ký tự lạ)
                    b = new PatrollingBlock(rect, Color.YELLOW, hitPoints, moveRange);
                }
                // -----------------------------------------

                blocks.add(b);
            }
        }
        return blocks;
    }

    @Override
    public int numberOfBlocksToRemove() {
        int count = 0;
        for (String row : PATTERN) {
            for (char c : row.toCharArray()) {
                if (c != ' ') {
                    count++;
                }
            }
        }
        return count;
    }
}