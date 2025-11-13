package menu; // Hoặc package 'animation' của bạn

import animation.Animation;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.imageio.ImageIO;



public class MenuScreen implements Animation {
    private final KeyboardSensor keyboard;
    private final MouseSensor mouse;
    private MenuSelection selection;
    private BufferedImage backgroundImage;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 20;
    private static final int CENTER_X = 800 / 2;
    private static final int START_Y = 250;

    public MenuScreen(KeyboardSensor k, MouseSensor m) {
        this.keyboard = k;
        this.mouse = m;
        this.selection = MenuSelection.NONE;
        try {
            // Đường dẫn tài nguyên (resource path) chính xác
            String imagePath = "resources/backgrounds/arcade_menu.jpg";
            this.backgroundImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền cho menu: " + e.getMessage());
            this.backgroundImage = null;
        }
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        // 1. Vẽ nền
        if (this.backgroundImage != null) {
            d.drawImage(0, 0, this.backgroundImage);
        } else {
            d.setColor(new Color(20, 0, 40));
            d.fillRectangle(0, 0, 800, 600);
            drawTitle(d);
        }

        // --- 2. Lấy trạng thái chuột ---
        int mouseX = this.mouse.getX();
        int mouseY = this.mouse.getY();
        boolean mousePressed = this.mouse.isPressed(MouseSensor.LEFT_CLICK);

        // --- 3. Xác định vùng nút & Kiểm tra Input ---
        int currentY = START_Y;

        // --- Nút PLAY ---
        Rectangle playRect = new Rectangle(new Point(CENTER_X - ((double) BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean playHover = isMouseInside(playRect, mouseX, mouseY);
        drawButton(d, "PLAY", playRect, Color.GREEN.darker(), playHover || keyboard.isPressed("enter"));
        if (keyboard.isPressed("enter") || (playHover && mousePressed)) {
            this.selection = MenuSelection.PLAY;
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;

        // --- Nút HIGH SCORE ---
        Rectangle hsRect = new Rectangle(new Point(CENTER_X - ((double) BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean hsHover = isMouseInside(hsRect, mouseX, mouseY);
        drawButton(d, "HIGH SCORE", hsRect, Color.CYAN.darker(), hsHover || keyboard.isPressed("h"));
        if (keyboard.isPressed("h") || (hsHover && mousePressed)) {
            this.selection = MenuSelection.HIGH_SCORE;
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;

        // --- Nút RESTART ---
        Rectangle restartRect = new Rectangle(new Point(CENTER_X - ((double) BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean restartHover = isMouseInside(restartRect, mouseX, mouseY);
        drawButton(d, "RESTART", restartRect, Color.BLUE.darker(), restartHover || keyboard.isPressed("r"));
        if (keyboard.isPressed("r") || (restartHover && mousePressed)) {
            this.selection = MenuSelection.RESTART;
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;

        // --- Nút EXIT ---
        Rectangle exitRect = new Rectangle(new Point(CENTER_X - ((double) BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean exitHover = isMouseInside(exitRect, mouseX, mouseY);
        drawButton(d, "EXIT", exitRect, Color.RED.darker(), exitHover || keyboard.isPressed("escape"));
        if (keyboard.isPressed("escape") || (exitHover && mousePressed)) {
            this.selection = MenuSelection.EXIT;
        }
    }

    private boolean isMouseInside(Rectangle rect, int x, int y) {
        return (x >= rect.getUpperLeft().getX() &&
                x <= rect.getUpperLeft().getX() + rect.getWidth() &&
                y >= rect.getUpperLeft().getY() &&
                y <= rect.getUpperLeft().getY() + rect.getHeight());
    }

    private void drawTitle(DrawSurface d) {
        d.setColor(new Color(255, 100, 200));
        d.drawText(CENTER_X - 150, 100, "ARCADE", 80);
        d.setColor(new Color(150, 50, 120));
        d.drawText(CENTER_X - 148, 102, "ARCADE", 80);
    }

    /**
     * Hàm drawButton (Đã sửa lỗi căn giữa).
     */
    private void drawButton(DrawSurface d, String text, Rectangle buttonRect, Color baseColor, boolean isActive) {
        Color fillColor = baseColor;
        Color outlineColor = baseColor.brighter();

        if (isActive) {
            fillColor = baseColor.brighter();
            outlineColor = Color.WHITE;
        }

        int x = (int) buttonRect.getUpperLeft().getX();
        int y = (int) buttonRect.getUpperLeft().getY();
        int w = (int) buttonRect.getWidth();
        int h = (int) buttonRect.getHeight();

        d.setColor(fillColor);
        d.fillRectangle(x, y, w, h);
        d.setColor(outlineColor);
        d.drawRectangle(x, y, w, h);

        d.setColor(Color.WHITE);

        // --- SỬA LỖI CĂN GIỮA ---
        // Tăng hệ số nhân từ 11.5 lên 12.5 để ước lượng chính xác hơn
        int textWidth = (int) (text.length() * 14.5);
        d.drawText(x + (w / 2) - (textWidth / 2), y + (h / 2) + 8, text, 24);
        // -------------------------
    }

    @Override
    public boolean shouldStop() { return this.selection != MenuSelection.NONE; }
    public MenuSelection getSelection() { return this.selection; }
    public void resetSelection() { this.selection = MenuSelection.NONE; }
}