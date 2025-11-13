package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import menu.MouseSensor;
import game.ScoreInfo;
import game.ScorePersistence;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Color;
import java.util.List;

/**
 * Hiển thị màn hình Ranking.
 * Giờ đây có nút "Clear All".
 */
public class HighScoreScreen implements Animation {

    private final ScorePersistence sp;
    private final KeyboardSensor keyboard;
    private final MouseSensor mouse;
    private boolean stop;
    private List<ScoreInfo> scores;

    // (Hằng số nút)
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int SCREEN_WIDTH = 800;

    /**
     * Hàm khởi tạo.
     */
    public HighScoreScreen(ScorePersistence sp, KeyboardSensor k, MouseSensor m) {
        this.sp = sp;
        this.keyboard = k;
        this.mouse = m;
        this.stop = false;

        // Tải (refresh) điểm khi khởi tạo
        this.refreshScores();
    }

    /**
     * Tải lại điểm từ tệp.
     */
    private void refreshScores() {
        this.scores = this.sp.readScores();
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        // --- 1. Lấy trạng thái chuột ---
        int mouseX = this.mouse.getX();
        int mouseY = this.mouse.getY();
        // (Chúng ta sẽ kiểm tra 'isPressed' bên trong logic,
        //  để tránh 'refresh' nhiều lần)

        // --- 2. Vẽ nền ---
        d.setColor(Color.BLACK);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
        d.setColor(Color.YELLOW);
        d.drawText(d.getWidth() / 2 - 100, 100, "RANKING", 40);

        // --- 3. Vẽ các điểm ---
        d.setColor(Color.WHITE);
        int yPos = 180;
        int rank = 1;

        if (scores.isEmpty()) {
            d.drawText(d.getWidth() / 2 - 100, yPos, "No scores yet!", 24);
        } else {
            for (ScoreInfo info : this.scores) {
                String line = rank + ". " + info.getPlayerName() + " : " + info.getScore();
                d.drawText(200, yPos, line, 24);
                yPos += 40;
                rank++;
            }
        }

        // --- 4. Vẽ các nút ---

        // Nút CLEAR ALL (Đổi từ Refresh)
        Rectangle clearRect = new Rectangle(new Point(100, 530), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean clearHover = isMouseInside(clearRect, mouseX, mouseY);
        drawButton(d, "Clear All [R]", clearRect, Color.RED.darker(), clearHover); // Màu đỏ

        // Nút CONTINUE
        Rectangle continueRect = new Rectangle(new Point(SCREEN_WIDTH - BUTTON_WIDTH - 100, 530), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean continueHover = isMouseInside(continueRect, mouseX, mouseY);
        drawButton(d, "Continue [SPACE]", continueRect, Color.GREEN.darker(), continueHover);

        // --- 5. Xử lý Logic ---

        // Xử lý Clear All
        // (Lưu ý: isPressed có thể kích hoạt nhiều lần,
        // nhưng clearScores() chỉ xóa tệp 1 lần)
        if (keyboard.isPressed("r") || (clearHover && this.mouse.isPressed(MouseSensor.LEFT_CLICK))) {

            // --- THAY ĐỔI LOGIC ---
            // 1. Xóa tệp điểm cao
            this.sp.clearScores();
            // 2. Tải lại danh sách (giờ sẽ trống)
            this.refreshScores();
            // ---------------------
        }

        // Xử lý Continue/Thoát
        if (keyboard.isPressed("space") || (continueHover && this.mouse.isPressed(MouseSensor.LEFT_CLICK))) {
            this.stop = true;
        }
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }

    // --- CÁC PHƯƠNG THỨC TRỢ GIÚP (Copy từ MenuScreen) ---

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

        int textWidth = (int) (text.length() * 10.5);
        d.drawText(x + (w / 2) - (textWidth / 2), y + (h / 2) + 7, text, 20);
    }
}