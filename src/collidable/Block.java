package collidable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;
import java.awt.Color;
import ball.Ball;
import listeners.HitListener;
import ball.Velocity;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import listeners.HitNotifier;
import game.Sprite;
import javax.imageio.ImageIO;


public class Block implements Collidable, Sprite, HitNotifier {
    private final List<HitListener> hitListeners;
    private Color color;
    protected Rectangle rectangle;
    private Color colorStroke;
    private String[] images;
    private Image[] loadedImages;

    // (Biến Máu (Hit Points) vẫn tồn tại)
    protected int hitPoints;

    /**
     * Hàm khởi tạo mặc định, gán 1 HP.
     */
    public Block(Rectangle rectangle, Color color) {
        this(rectangle, color, 1);
    }

    /**
     * Hàm khởi tạo với số HP tùy chỉnh.
     */
    public Block(Rectangle rectangle, Color color, int hitPoints) {
        this.color = color;
        this.rectangle = rectangle;
        this.hitPoints = hitPoints;
        this.hitListeners = new ArrayList<>();
    }


    public Block(Point leftUpperCorner, int width, int height) {
        this(new Rectangle(leftUpperCorner, width, height), Color.GRAY, 1);
    }

    // (Các phương thức addToGame, removeFromGame, getCollisionRectangle,
    //  colorForStroke, setColors, setColor, setImages,
    //  loadImagesToBlock, notifyHit, trimStrings giữ nguyên)

    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    public void removeFromSprites(GameLevel game) {
        game.removeSprite(this);
    }

    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }

    public void colorForStroke(Color c) {
        this.colorStroke = c;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void loadImagesToBlock(String[] paths) {
        try {
            List<Image> imageList = new ArrayList<>();
            for (String path : paths) {
                Image image;
                trimStrings(paths);
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
                assert is != null;
                image = ImageIO.read(is);
                imageList.add(image);
            }
            this.loadedImages = new Image[imageList.size()];
            this.loadedImages = imageList.toArray(this.loadedImages);
        } catch (IOException e) {
            System.out.println("failed loading image while drawing blocks");
        }
    }


    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }


    private void trimStrings(String[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replaceAll("\\(", "");
            s[i] = s[i].replaceAll("\\)", "");
        }
    }

    /**
     * Logic `hit()` vẫn giữ nguyên (giảm HP).
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        // 1. Giảm máu
        this.hitPoints--;

        // 2. Kiểm tra xem khối gạch có bị phá hủy không
        if (this.hitPoints <= 0) {
            this.notifyHit(hitter);
        }

        // 3. Tính toán vận tốc mới
        double dx = currentVelocity.getDx();
        double dy = currentVelocity.getDy();
        double startX = rectangle.getUpperLeft().getX();
        double startY = rectangle.getUpperLeft().getY();
        double endX = startX + rectangle.getWidth();
        double endY = startY + rectangle.getHeight();
        double x = collisionPoint.getX();
        double y = collisionPoint.getY();
        double epsilon = 0.001;
        boolean horizontalHit = false;
        boolean verticalHit = false;

        if ((x >= startX) && (x <= endX)) {
            if (Math.abs(y - startY) < epsilon || Math.abs(y - endY) < epsilon) {
                verticalHit = true;
            }
        }
        if ((y >= startY) && (y <= endY)) {
            if (Math.abs(x - startX) < epsilon || Math.abs(x - endX) < epsilon) {
                horizontalHit = true;
            }
        }
        if (verticalHit) {
            dy = dy * (-1);
        }
        if (horizontalHit) {
            dx = dx * (-1);
        }
        return new Velocity(dx, dy);
    }


    /**
     * THAY ĐỔI: Đã xóa code vẽ số HP.
     */
    @Override
    public void drawOn(DrawSurface d) {
        if (this.images != null && this.images.length > 0 && this.loadedImages != null) {
            drawOnImage(d);
        } else {
            // Vẽ 3D
            int x = (int) rectangle.getUpperLeft().getX();
            int y = (int) rectangle.getUpperLeft().getY();
            int width = (int) rectangle.getWidth();
            int height = (int) rectangle.getHeight();
            Color mainColor = this.color;
            Color highlightColor = mainColor.brighter();
            Color shadowColor = mainColor.darker().darker();

            d.setColor(mainColor);
            d.fillRectangle(x, y, width, height);
            d.setColor(highlightColor);
            d.fillRectangle(x, y, width, 2);
            d.fillRectangle(x, y, 2, height);
            d.setColor(shadowColor);
            d.fillRectangle(x, y + height - 2, width, 2);
            d.fillRectangle(x + width - 2, y, 2, height);
        }


    }


    @Override
    public void timePassed() {
        // (Giữ nguyên)
    }


    private void drawOnImage(DrawSurface d) {
        // (Giữ nguyên logic drawOnImage của bạn)
        int index = 0;
        Point p = rectangle.getUpperLeft();
        d.drawImage((int) p.getX(), (int) p.getY(), this.loadedImages[index]);
        if (this.colorStroke != null) {
            int width = (int) this.rectangle.getWidth();
            int height = (int) this.rectangle.getHeight();
            d.drawRectangle((int) p.getX(), (int) p.getY(), width, height);
        }
    }


    @Override
    public void addHitListener(HitListener hl) {
        hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        hitListeners.remove(hl);
    }
}