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
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;

public class MenuScreen implements Animation {
    private final KeyboardSensor keyboard;
    private final MouseSensor mouse;
    private MenuSelection selection;


    private final List<BufferedImage> backgroundFrames;
    private int currentFrame;
    private int frameDelayCounter;
    private static final int ANIMATION_DELAY = 6;


    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;


    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 20;
    private static final int CENTER_X = SCREEN_WIDTH / 2;
    private static final int START_Y = 250;


    public MenuScreen(KeyboardSensor k, MouseSensor m) {
        this.keyboard = k;
        this.mouse = m;
        this.selection = MenuSelection.NONE;

        this.backgroundFrames = new ArrayList<>();
        this.currentFrame = 0;
        this.frameDelayCounter = 0;

        try {
            int i = 1;
            System.out.println("Menu: Bắt đầu tải và co giãn các khung hình GIF (việc này có thể mất một lúc)...");

            while (true) {

                String framePath = "resources/backgrounds/menu_gif/frame (" + i + ").png";
                BufferedImage originalFrame = ImageIO.read(Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(framePath)));


                BufferedImage scaledFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, originalFrame.getType());


                Graphics2D g2d = scaledFrame.createGraphics();


                g2d.drawImage(originalFrame, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                g2d.dispose(); // Giải phóng bộ nhớ


                this.backgroundFrames.add(scaledFrame);
                i++;
            }
        } catch (Exception e) {
            if (this.backgroundFrames.isEmpty()) {
                System.err.println("Không thể tải BẤT KỲ khung hình GIF nào (định dạng 'frame (i).png').");
            } else {
                System.out.println("Menu: Đã tải và co giãn " + this.backgroundFrames.size() + " khung hình nền.");
            }
        }
    }

    @Override
    public void doOneFrame(DrawSurface d) {


        d.setColor(Color.BLACK);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());

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

            drawTitle(d);
        }


        int mouseX = this.mouse.getX();
        int mouseY = this.mouse.getY();
        boolean mousePressed = this.mouse.isPressed(MouseSensor.LEFT_CLICK);

        int currentY = START_Y;


        Rectangle playRect = new Rectangle(new Point(CENTER_X - ((double) BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean playHover = isMouseInside(playRect, mouseX, mouseY);
        drawButton(d, "PLAY", playRect, Color.GREEN.darker(), playHover || keyboard.isPressed("enter"));
        if (keyboard.isPressed("enter") || (playHover && mousePressed)) {
            this.selection = MenuSelection.PLAY;
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;


        Rectangle rankRect = new Rectangle(new Point(CENTER_X - ((double) BUTTON_WIDTH / 2), currentY), BUTTON_WIDTH, BUTTON_HEIGHT);
        boolean rankHover = isMouseInside(rankRect, mouseX, mouseY);
        drawButton(d, "RANKING", rankRect, Color.CYAN.darker(), rankHover || keyboard.isPressed("k"));
        if (keyboard.isPressed("k") || (rankHover && mousePressed)) {
            this.selection = MenuSelection.RANKING;
        }
        currentY += BUTTON_HEIGHT + BUTTON_SPACING;


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

        int textWidth = (int) (text.length() * 14.5);
        d.drawText(x + (w / 2) - (textWidth / 2), y + (h / 2) + 8, text, 24);
    }

    @Override
    public boolean shouldStop() { return this.selection != MenuSelection.NONE; }
    public MenuSelection getSelection() { return this.selection; }
    public void resetSelection() { this.selection = MenuSelection.NONE; }
}