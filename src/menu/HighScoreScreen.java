package menu; // Hoặc package animation của bạn

import animation.Animation;
import biuoop.DrawSurface;
import java.awt.Color;
// Bạn sẽ cần một lớp để quản lý tệp điểm cao, ví dụ: 'ScorePersistence' hoặc 'HighScoreManager'
// import game.ScorePersistence;

public class HighScoreScreen implements Animation {
    private int highScore;

    public HighScoreScreen() {
        // Tải điểm cao khi màn hình được tạo
        // (Giả sử bạn có một lớp ScorePersistence như trong GameFlow của bạn)
        // ScorePersistence sp = new ScorePersistence();
        // try {
        //     this.highScore = sp.readFromFile();
        // } catch (Exception e) {
        //     this.highScore = 0;
        // }

        // Tạm thời, nếu bạn chưa có, hãy dùng giá trị giả:
        this.highScore = 0; // <-- THAY THẾ BẰNG LOGIC TẢI ĐIỂM CỦA BẠN
    }

    // Nếu GameFlow của bạn truyền điểm cao vào
    public HighScoreScreen(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        d.setColor(new Color(20, 0, 40)); // Nền tím sẫm
        d.fillRectangle(0, 0, 800, 600);

        d.setColor(new Color(0, 255, 255)); // Cyan
        d.drawText(800 / 2 - 220, 600 / 2 - 100, "HIGH SCORE", 60);

        d.setColor(Color.WHITE);
        String scoreStr = String.valueOf(this.highScore);
        int approximateWidth = scoreStr.length() * 45;
        d.drawText(800 / 2 - (approximateWidth / 2), 600 / 2 + 50, scoreStr, 100);

        d.setColor(Color.GREEN.darker());
        d.drawText(800 / 2 - 180, 600 - 100, "Press SPACE to return", 30);
    }

    @Override
    public boolean shouldStop() {
        // Màn hình này sẽ được bọc bởi KeyPressStoppableAnimation,
        // vì vậy nó không bao giờ nên tự dừng.
        return false;
    }
}