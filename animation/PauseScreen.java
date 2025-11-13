package animation; // <-- THAY ĐỔI: Bây giờ là package 'animation'

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import java.awt.Color;
// --- CÁC IMPORT MỚI ĐÃ ĐƯỢC THÊM VÀO ---
import menu.MenuSelection; // <-- THÊM MỚI: Import từ package 'menu'
import menu.MouseSensor;   // <-- THÊM MỚI: Import từ package 'menu'
// ------------------------------------

/**
 * Animation cho màn hình tạm dừng.
 */
public class PauseScreen implements Animation {
    private KeyboardSensor keyboard;
    private MouseSensor mouse;
    private MenuSelection selection;

    // Hằng số bố cục nút
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 20;
    private static final int CENTER_X = 800 / 2;
    private static final int START_Y = 250;

    public PauseScreen(KeyboardSensor k, MouseSensor m) {
        this.keyboard = k;
        this.mouse = m;
        this.selection = MenuSelection.NONE;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        // 1. Vẽ lớp phủ bán trong suốt
        d.setColor(new Color(0, 0, 0, 150)); // Đen mờ 60%
        d.fillRectangle(0, 0, 800, 600);

        // 2. Vẽ tiêu đề "PAUSED"
        d.setColor(Color.WHITE);
        d.drawText(CENTER_X - 110, 150, "PAUSED", 60);

        // 3. Lấy trạng thái chuột
        int mouseX = (int) this.mouse.getX();
        int mouseY = (int) this.mouse.getY();
        boolean mousePressed = this.mouse.isPressed(MouseSensor.LEFT_CLICK);

        // 4. Vẽ các nút
        int currentY = START_Y;

        // --- Nút CONTINUE ---
        Rectangle continueRect = new Rectangle(new Point(CENTER_X - (BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean continueHover = isMouseInside(continueRect, mouseX, mouseY);

        // --- SỬA LỖI "STICKY KEY": Dùng "space" để tiếp tục ---
        boolean continuePressed = keyboard.isPressed("space");
        drawButton(d, "CONTINUE (Space)", continueRect, Color.GREEN.darker(), continueHover || continuePressed);

        if (continuePressed || (continueHover && mousePressed)) {
            this.selection = MenuSelection.PLAY; // PLAY = Continue
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;

        // --- Nút REPLAY (RESTART) ---
        Rectangle restartRect = new Rectangle(new Point(CENTER_X - (BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean restartHover = isMouseInside(restartRect, mouseX, mouseY);
        boolean restartPressed = keyboard.isPressed("r");
        drawButton(d, "REPLAY (R)", restartRect, Color.BLUE.darker(), restartHover || restartPressed);
        if (restartPressed || (restartHover && mousePressed)) {
            this.selection = MenuSelection.RESTART;
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;

        // --- Nút EXIT TO MENU ---
        Rectangle exitRect = new Rectangle(new Point(CENTER_X - (BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean exitHover = isMouseInside(exitRect, mouseX, mouseY);
        boolean exitPressed = keyboard.isPressed("e");
        drawButton(d, "EXIT (E)", exitRect, Color.RED.darker(), exitHover || exitPressed);
        if (exitPressed || (exitHover && mousePressed)) {
            this.selection = MenuSelection.EXIT;
        }
    }

    // --- Các phương thức trợ giúp ---

    @Override
    public boolean shouldStop() {
        return this.selection != MenuSelection.NONE;
    }

    public MenuSelection getSelection() {
        return this.selection;
    }

    public void resetSelection() {
        this.selection = MenuSelection.NONE;
    }

    private boolean isMouseInside(Rectangle rect, int x, int y) {
        return (x >= rect.getUpperLeft().getX() &&
                x <= rect.getUpperLeft().getX() + rect.getWidth() &&
                y >= rect.getUpperLeft().getY() &&
                y <= rect.getUpperLeft().getY() + rect.getHeight());
    }

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
        // Căn giữa văn bản (cách đơn giản)
        int textWidth = text.length() * 10;
        d.drawText(x + (w / 2) - (textWidth / 2) - 5, y + (h / 2) + 8, text, 24);
    }
}