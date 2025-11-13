package animation;

import biuoop.DrawSurface;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HighScoreScreen implements Animation {
    private boolean stop;
    private int score;

    public HighScoreScreen(int score) {
        this.stop = false;
        this.score = score;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        int width = d.getWidth();
        int height = d.getHeight();

        // Background
        d.setColor(new Color(20, 0, 40)); // Deep purple background
        d.fillRectangle(0, 0, width, height);

        // Title text
        String title = "HIGH SCORE";
        d.setColor(new Color(0, 255, 255)); // Cyan text
        int titleFontSize = 60;
        int titleWidth = getTextWidth(title, titleFontSize);
        d.drawText((width / 2) - (titleWidth / 2), height / 2 - 100, title, titleFontSize);

        // Score display
        String scoreText = String.valueOf(score);
        d.setColor(Color.WHITE);
        int scoreFontSize = 100;
        int scoreWidth = getTextWidth(scoreText, scoreFontSize);
        d.drawText((width / 2) - (scoreWidth / 2), height / 2 + 50, scoreText, scoreFontSize);

        // Hint text
        String hint = "Press SPACE to return";
        d.setColor(Color.GREEN.darker());
        int hintFontSize = 30;
        int hintWidth = getTextWidth(hint, hintFontSize);
        d.drawText((width / 2) - (hintWidth / 2), height - 80, hint, hintFontSize);
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }

    // Helper to measure text width for proper centering
    private int getTextWidth(String text, int fontSize) {
        BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempImg.createGraphics();
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, fontSize));
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        g2d.dispose();
        return width;
    }
}
