package menu;

import animation.Animation;
import biuoop.DrawSurface;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Màn hình hiển thị điểm cao – tất cả được căn giữa hoàn hảo.
 */
public class HighScoreScreen implements Animation {
    private int highScore;

    public HighScoreScreen() {
        this.highScore = 0; // Tạm thời, thay bằng logic đọc điểm cao của bạn
    }

    public HighScoreScreen(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        int screenWidth = d.getWidth();
        int screenHeight = d.getHeight();

        // --- Background ---
        d.setColor(new Color(20, 0, 40)); // Deep purple
        d.fillRectangle(0, 0, screenWidth, screenHeight);

        // --- Title ---
        String title = "HIGH SCORE";
        int titleFontSize = 60;
        int titleWidth = getTextWidth(title, titleFontSize);
        d.setColor(new Color(0, 255, 255)); // Cyan
        d.drawText((screenWidth / 2) - (titleWidth / 2), screenHeight / 3, title, titleFontSize);

        // --- Score ---
        String scoreStr = String.valueOf(this.highScore);
        int scoreFontSize = 100;
        int scoreWidth = getTextWidth(scoreStr, scoreFontSize);
        d.setColor(Color.WHITE);
        d.drawText((screenWidth / 2) - (scoreWidth / 2), (screenHeight / 2) + 50, scoreStr, scoreFontSize);

        // --- Footer / Hint ---
        String footer = "Press SPACE to return";
        int footerFontSize = 30;
        int footerWidth = getTextWidth(footer, footerFontSize);
        d.setColor(Color.GREEN.darker());
        d.drawText((screenWidth / 2) - (footerWidth / 2), screenHeight - 80, footer, footerFontSize);
    }

    /**
     * Uses FontMetrics to calculate accurate text width.
     */
    private int getTextWidth(String text, int fontSize) {
        BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = tempImg.getGraphics();
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, fontSize));
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(text);
        g.dispose();
        return width;
    }

    @Override
    public boolean shouldStop() {
        // Sử dụng KeyPressStoppableAnimation bên ngoài để dừng khi nhấn phím
        return false;
    }
}