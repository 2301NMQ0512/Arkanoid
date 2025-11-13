package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import menu.MenuSelection;

// (Imports cho ảnh động)
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Màn hình thua, với nền GIF động và các tùy chọn ở góc dưới.
 * (Đã xóa thông báo điểm)
 */
public class EndScreenLose implements Animation {

    private final KeyboardSensor keyboard;
    private MenuSelection selection;
    private boolean stop = false;

    // (Biến cho ảnh động)
    private final List<BufferedImage> backgroundFrames;
    private int currentFrame;
    private int frameDelayCounter;
    private static final int ANIMATION_DELAY = 3;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    /**
     * THAY ĐỔI: Hàm khởi tạo này nhận 2 đối số,
     * khớp với lệnh gọi trong GameFlow.java.
     */
    public EndScreenLose(KeyboardSensor k) {
        this.keyboard = k;
        this.selection = MenuSelection.NONE;

        this.backgroundFrames = new ArrayList<>();
        this.currentFrame = 0;
        this.frameDelayCounter = 0;

        try {
            int i = 1;
            System.out.println("EndScreenLose: Bắt đầu tải các khung hình GIF...");

            while (true) {
                // (Đảm bảo bạn có thư mục: resources/backgrounds/lose_gif/)
                String framePath = "resources/backgrounds/lose_gif/frame (" + i + ").png";

                BufferedImage originalFrame = ImageIO.read(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(framePath)));

                // Co giãn
                BufferedImage scaledFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, originalFrame.getType());
                Graphics2D g = scaledFrame.createGraphics();
                g.drawImage(originalFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                g.dispose();

                this.backgroundFrames.add(scaledFrame);
                i++;
            }
        } catch (Exception e) {
            if (this.backgroundFrames.isEmpty()) {
                System.err.println("Không thể tải BẤT KỲ khung hình GIF nào cho EndScreenLose.");
            } else {
                System.out.println("EndScreenLose: Đã tải " + this.backgroundFrames.size() + " khung hình.");
            }
        }
    }


    @Override
    public void doOneFrame(DrawSurface d) {
        // --- 1. Vẽ nền GIF động ---
        if (!this.backgroundFrames.isEmpty()) {
            d.drawImage(0, 0, this.backgroundFrames.get(this.currentFrame));

            this.frameDelayCounter++;
            if (this.frameDelayCounter >= ANIMATION_DELAY) {
                this.frameDelayCounter = 0;
                this.currentFrame++;
                if (this.currentFrame >= this.backgroundFrames.size()) {
                    this.currentFrame = 0;
                }
            }
        } else {
            d.setColor(Color.BLACK);
            d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
        }

        // --- 2. Vẽ Văn bản ---
        d.setColor(Color.WHITE);

        // (Đã xóa văn bản điểm)

        // Tùy chọn 1: Dưới bên trái
        d.drawText(100, 550, "Replay [R]", 28);

        // Tùy chọn 2: Dưới bên phải
        d.drawText(550, 550, "Exit to Menu [M]", 28);


        // --- 3. Kiểm tra Lựa chọn ---
        if (this.keyboard.isPressed("r")) {
            this.selection = MenuSelection.RESTART;
            this.stop = true;
        }
        if (this.keyboard.isPressed("m")) {
            this.selection = MenuSelection.EXIT;
            this.stop = true;
        }
    }

    public MenuSelection getSelection() {
        return this.selection;
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}