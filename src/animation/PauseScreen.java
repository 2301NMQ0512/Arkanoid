package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import menu.MenuSelection;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PauseScreen implements Animation {
    private final KeyboardSensor keyboard;
    private MenuSelection selection;
    private boolean isAlreadyPressed;

    // Layout constants
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 20;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CENTER_X = SCREEN_WIDTH / 2;
    private static final int CENTER_Y = SCREEN_HEIGHT / 2;

    public PauseScreen(KeyboardSensor k) {
        this.keyboard = k;
        this.selection = MenuSelection.NONE;
        this.isAlreadyPressed = true;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        // --- Background overlay ---
        d.setColor(new Color(0, 0, 0)); // translucent black overlay
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());

        // --- Title ---
        String title = "PAUSED";
        int titleWidth = getTextWidth(title, 60);
        d.setColor(Color.WHITE);
        d.drawText(CENTER_X - (titleWidth / 2), 150, title, 60);

        // --- Button positions ---
        int totalHeight = (2 * BUTTON_HEIGHT) + BUTTON_SPACING; // 2 buttons
        int startY = CENTER_Y - (totalHeight / 2); // vertically centered

        // --- CONTINUE Button ---
        boolean continuePressed = keyboard.isPressed(KeyboardSensor.ENTER_KEY);
        drawButton(d, "CONTINUE (Enter)", startY, Color.GREEN.darker(), continuePressed);

        // --- EXIT Button ---
        boolean exitPressed = keyboard.isPressed("e");
        drawButton(d, "EXIT TO MENU (E)", startY + BUTTON_HEIGHT + BUTTON_SPACING, Color.RED.darker(), exitPressed);

        // --- Input handling (debounce/sticky key fix) ---
        if (this.isAlreadyPressed) {
            if (!keyboard.isPressed(KeyboardSensor.ENTER_KEY)
                    && !keyboard.isPressed("e")
                    && !keyboard.isPressed("p")) {
                this.isAlreadyPressed = false;
            }
        } else {
            if (continuePressed) {
                this.selection = MenuSelection.PLAY;
            } else if (exitPressed) {
                this.selection = MenuSelection.EXIT;
            }
        }
    }

    /**
     * Draws a centered button with properly centered text.
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

        // Draw button box
        d.setColor(fillColor);
        d.fillRectangle((int) buttonRect.getUpperLeft().getX(), (int) buttonRect.getUpperLeft().getY(),
                (int) buttonRect.getWidth(), (int) buttonRect.getHeight());

        d.setColor(outlineColor);
        d.drawRectangle((int) buttonRect.getUpperLeft().getX(), (int) buttonRect.getUpperLeft().getY(),
                (int) buttonRect.getWidth(), (int) buttonRect.getHeight());

        // --- Center text properly ---
        int textWidth = getTextWidth(text, 24);
        d.setColor(Color.WHITE);
        int textX = x + (BUTTON_WIDTH / 2) - (textWidth / 2);
        int textY = y + (BUTTON_HEIGHT / 2) + 8; // visually centered
        d.drawText(textX, textY, text, 24);
    }

    /**
     * Uses FontMetrics to accurately measure string width.
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
        return this.selection != MenuSelection.NONE;
    }

    public MenuSelection getSelection() {
        return this.selection;
    }
}
