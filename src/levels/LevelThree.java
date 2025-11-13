package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import collidable.PatrollingBlock;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.Objects;
import javax.imageio.ImageIO;


public class LevelThree implements LevelInformation {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    // (PATTERN Cúp Lớn giữ nguyên)
    private static final String[] PATTERN = {
            "            CCCCCCC            ",
            "           CCCCCCCCC           ",
            "          CCCCCCCCCCC          ",
            "         CCCCCCCCCCCCC         ",
            "         CCCCCCCCCCCCC         ",
            "          CCCCCCCCCCC          ",
            "           CCCCCCCCC           ",
            "            CCCCCCC            ",
            "            CCCCCCC            ",
            "            CCCCCCC            ",
            "        BBBBBBBBBBBBBBB        ",
            "       BBBBBBBBBBBBBBBBB       "
    };

    private static final int BLOCK_SIZE = 25;
    private static final int START_X = 90;
    private static final int START_Y = 100;



    @Override
    public int numberOfBalls() {
        return 3;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<>();
        velocities.add(Velocity.fromAngleAndSpeed(-45, 6));
        velocities.add(Velocity.fromAngleAndSpeed(0, 6));
        velocities.add(Velocity.fromAngleAndSpeed(45, 6));
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
    public String levelName() {
        return "Level Three: The Cup";
    }

    @Override
    public String getBackgroundMusicPath() {
        return "resources/sounds/level3_music.wav";
    }


    @Override
    public Sprite getBackground() {

        List<BufferedImage> backgroundFrames = new ArrayList<>();
        try {
            System.out.println("LevelThree: Bắt đầu tải các khung hình GIF...");
            int i = 1;
            while (true) {

                String framePath = "resources/backgrounds/level_3_gif/frame (" + i + ").png";

                BufferedImage originalFrame = ImageIO.read(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(framePath)));


                BufferedImage scaledFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, originalFrame.getType());
                Graphics2D g = scaledFrame.createGraphics();
                g.drawImage(originalFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                g.dispose();

                backgroundFrames.add(scaledFrame);
                i++;
            }
        } catch (Exception e) {

            if (backgroundFrames.isEmpty()) {
                System.err.println("Không thể tải BẤT KỲ khung hình GIF nào cho LevelThree.");
            } else {
                System.out.println("LevelThree: Đã tải " + backgroundFrames.size() + " khung hình.");
            }
        }

        final List<BufferedImage> finalFrames = backgroundFrames;


        return new Sprite() {
            private int gifFrameCounter = 0;
            private int currentGifFrame = 0;

            private static final int GIF_ANIMATION_DELAY = 3;

            @Override
            public void drawOn(DrawSurface d) {
                if (!finalFrames.isEmpty()) {
                    d.drawImage(0, 0, finalFrames.get(this.currentGifFrame));
                } else {
                    d.setColor(new Color(0, 80, 0));
                    d.fillRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
            }

            @Override
            public void timePassed() {

                this.gifFrameCounter++;
                if (this.gifFrameCounter >= GIF_ANIMATION_DELAY) {
                    this.gifFrameCounter = 0;
                    this.currentGifFrame++;
                    if (this.currentGifFrame >= finalFrames.size()) {
                        this.currentGifFrame = 0;
                    }
                }
            }
        };
    }


    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        Random rand = new Random();

        int moveRange = 200;

        for (int row = 0; row < PATTERN.length; row++) {
            for (int col = 0; col < PATTERN[row].length(); col++) {

                char blockType = PATTERN[row].charAt(col);
                if (blockType == ' ') {
                    continue;
                }

                double x = START_X + (col * BLOCK_SIZE);
                double y = START_Y + (row * BLOCK_SIZE);
                Point pos = new Point(x, y);
                Rectangle rect = new Rectangle(pos, BLOCK_SIZE, BLOCK_SIZE);

                int hitPoints = rand.nextInt(4) + 1;
                Color randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

                Block b;
                b = new PatrollingBlock(rect, randomColor, hitPoints, moveRange);

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
                if (c == 'C' || c == 'B') {
                    count++;
                }
            }
        }
        return count;
    }
    public String getBallImagePath() {
        return "resources/sprites/ball_level_three.png";
    }
}