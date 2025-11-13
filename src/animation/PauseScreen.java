package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import menu.MenuSelection; // <-- Import tệp mới

import java.awt.Color;

/**
 * Một màn hình Tạm dừng tương tác với các tùy chọn.
 */
public class PauseScreen implements Animation {
    private KeyboardSensor keyboard;
    private MenuSelection selection;
    private boolean isAlreadyPressed; // Sửa lỗi "sticky key"

    // Hằng số bố cục nút
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 50;
    private static final int CENTER_X = 800 / 2; // Giả sử chiều rộng 800
    private static final int START_Y = 250;
    private static final int BUTTON_SPACING = 20;

    /**
     * Hàm khởi tạo mới, nhận KeyboardSensor.
     */
    public PauseScreen(KeyboardSensor k) {
        this.keyboard = k;
        this.selection = MenuSelection.NONE;
        // Giả định phím "p" (để vào đây) vẫn đang được nhấn
        this.isAlreadyPressed = true;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        // 1. Vẽ lớp phủ bán trong suốt
        d.setColor(new Color(0, 0, 0)); // Đen mờ
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());

        // 2. Vẽ tiêu đề "PAUSED"
        d.setColor(Color.WHITE);
        d.drawText(CENTER_X - 110, 150, "PAUSED", 60);

        int currentY = START_Y;

        // --- Nút CONTINUE (Tiếp tục) ---
        // Sửa lỗi vòng lặp vô hạn - Đổi "space" thành "enter"
        boolean continuePressed = keyboard.isPressed(KeyboardSensor.ENTER_KEY);
        drawButton(d, "CONTINUE (Enter)", currentY, Color.GREEN.darker(), continuePressed);

        currentY += BUTTON_HEIGHT + BUTTON_SPACING;

        // --- Nút EXIT TO MENU (Thoát) ---
        boolean exitPressed = keyboard.isPressed("e"); // Dùng phím 'E'
        drawButton(d, "EXIT TO MENU (E)", currentY, Color.RED.darker(), exitPressed);

        // --- Xử lý Logic Sticky Key ---
        if (this.isAlreadyPressed) {
            // Nếu phím "p" (hoặc phím khác) vẫn đang được nhấn, kiểm tra xem nó đã được nhả ra chưa
            if (!keyboard.isPressed(KeyboardSensor.ENTER_KEY) && !keyboard.isPressed("e")
                    && !keyboard.isPressed("p")) { // <-- Lắng nghe phím "P"
                this.isAlreadyPressed = false;
            }
        } else {
            // Chỉ chấp nhận input MỚI sau khi tất cả các phím đã được nhả ra
            if (continuePressed) {
                this.selection = MenuSelection.PLAY; // PLAY có nghĩa là "Continue"
            } else if (exitPressed) {
                this.selection = MenuSelection.EXIT;
            }
        }
    }

    /**
     * Hàm trợ giúp để vẽ các nút.
     */
    private void drawButton(DrawSurface d, String text, int y, Color baseColor, boolean isActive) {
        int x = CENTER_X - (BUTTON_WIDTH / 2);
        Rectangle buttonRect = new Rectangle(new Point(x, y), BUTTON_WIDTH, BUTTON_HEIGHT);
        Color fillColor = baseColor;
        Color outlineColor = baseColor.brighter();

        if (isActive) {
            fillColor = baseColor.brighter();
            outlineColor = Color.WHITE;
        }

        d.setColor(fillColor);
        d.fillRectangle((int) buttonRect.getUpperLeft().getX(), (int) buttonRect.getUpperLeft().getY(),
                (int) buttonRect.getWidth(), (int) buttonRect.getHeight());
        d.setColor(outlineColor);
        d.drawRectangle((int) buttonRect.getUpperLeft().getX(), (int) buttonRect.getUpperLeft().getY(),
                (int) buttonRect.getWidth(), (int) buttonRect.getHeight());

        d.setColor(Color.WHITE);
        // Căn giữa văn bản
        int textWidth = text.length() * 10;
        d.drawText(x + (BUTTON_WIDTH / 2) - (textWidth / 2) - 5, y + (BUTTON_HEIGHT / 2) + 8, text, 24);
    }

    @Override
    public boolean shouldStop() {
        // Dừng animation khi người dùng đã chọn (Enter hoặc E)
        return this.selection != MenuSelection.NONE;
    }

    /**
     * Trả về lựa chọn của người dùng cho GameLevel.
     */
    public MenuSelection getSelection() {
        return this.selection;
    }
}
