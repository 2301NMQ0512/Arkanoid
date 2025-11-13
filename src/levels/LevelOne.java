package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import game.Sprite;
import geometry.Rectangle;
import geometry.Point;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Level One hoàn chỉnh:
 * 1. Nền bằng ảnh (Ricardo Milos 800x600).
 * 2. 3 hàng gạch với màu sắc ngẫu nhiên.
 * 3. CÓ VIỀN ĐEN cho mỗi gạch (giống Level Two).
 * 4. Có nhạc nền.
 */
public class LevelOne implements LevelInformation {

    private static final int NUM_ROWS = 3;
    private static final int BLOCKS_PER_ROW = 12;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    @Override
    public int numberOfBalls() {
        return 1;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        List<Velocity> veloList = new ArrayList<>();
        veloList.add(Velocity.fromAngleAndSpeed(0, 6)); // Một quả bóng đi thẳng
        return veloList;
    }

    @Override
    public int paddleSpeed() {
        return 13;
    }

    @Override
    public int paddleWidth() {
        return 175;
    }

    @Override
    public String levelName() {
        return "Ricardo Bricks";
    }

    @Override
    public Sprite getBackground() {
        try {
            // Đường dẫn đến tệp ảnh 800x600 của bạn
            String imagePath = "resources/backgrounds/level_one_bg.png";
            final Image img = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(imagePath)));

            return new Sprite() {
                @Override
                public void drawOn(DrawSurface d) {
                    // Dùng phiên bản 3 tham số (vì thư viện của bạn cũ)
                    // Yêu cầu ảnh nền phải có kích thước 800x600
                    d.drawImage(0, 0, img);
                }

                @Override
                public void timePassed() { }
            };

        } catch (IOException | NullPointerException e) {
            System.err.println("Không thể tải ảnh nền cho Level 1: " + e.getMessage());
            // Trả về một nền màu đen đơn giản nếu thất bại
            return new Sprite() {
                @Override
                public void drawOn(DrawSurface d) {
                    d.setColor(Color.BLACK);
                    d.fillRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                }
                @Override
                public void timePassed() { }
            };
        }
    }

    /**
     * Tạo các hàng gạch với MÀU NGẪU NHIÊN và VIỀN ĐEN.
     * @return danh sách các gạch
     */
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();

        final double blockWidth = 65;
        final double blockHeight = 25;
        double startY = 150;
        double startX = 10;

        Random rand = new Random();

        for (int j = 0; j < NUM_ROWS; j++) {
            double currentY = startY + (j * blockHeight);

            for (int i = 0; i < BLOCKS_PER_ROW; i++) {
                // Tạo màu ngẫu nhiên
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                Color randomColor = new Color(r, g, b);

                double currentX = startX + (i * blockWidth);
                Point upperLeft = new Point(currentX, currentY);
                Rectangle rect = new Rectangle(upperLeft, blockWidth, blockHeight);

                Block block = new Block(rect, randomColor);

                // <-- THÊM MỚI: Thêm viền đen, giống hệt LevelTwo
                block.colorForStroke(Color.black);

                blocks.add(block);
            }
        }
        return blocks;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return NUM_ROWS * BLOCKS_PER_ROW; // 3 * 12 = 36
    }

    /**
     * THÊM MỚI: Chỉ định nhạc nền cho Level 1.
     * (Triển khai từ bước trước)
     */
    @Override
    public String getBackgroundMusicPath() {
        // Đảm bảo bạn có tệp này trong thư mục resources/sounds
        return "resources/sounds/level1_music.wav";
    }
    @Override
    public String getBallImagePath() {
        // (Bạn phải tạo tệp ảnh này)
        return "resources/sprites/ball_level_one.png";
    }
}