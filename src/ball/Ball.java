package ball;

import biuoop.DrawSurface;
import collidable.CollisionInfo;
import geometry.Line;
import geometry.Point;
import game.GameLevel;
import game.GameEnvironment;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;


public class Ball implements Sprite {
    private final int r;
    private Point center;
    private Velocity velocity;
    private final GameEnvironment gameEnvironment;
    private final String imagePath;


    private final List<Image> scaledBallFrames;


    private int currentFrame;
    private int frameDelayCounter;

    private static final int ANIMATION_DELAY = 2;


    public Ball(Point center, int r, GameEnvironment gameEnvironment, Velocity v, String imagePath) {
        this.center = center;
        this.r = r;
        this.velocity = v;
        this.gameEnvironment = gameEnvironment;
        this.imagePath = imagePath;

        // Khởi tạo các biến GIF
        this.scaledBallFrames = new ArrayList<>();
        this.currentFrame = 0;
        this.frameDelayCounter = 0;


        this.loadAndScaleImages();
    }


    private void loadAndScaleImages() {
        String path = this.imagePath;
        if (path == null) {
            // Dùng ảnh mặc định nếu level không cung cấp
            path = "resources/sprites/ball_image.png";
        }


        if (path.endsWith("/")) {
            try {
                int i = 1;
                while (true) {
                    // Thử tải "resources/sprites/level_four_ball_gif/frame (1).png"
                    String framePath = path + "frame (" + i + ").png";
                    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(framePath);
                    BufferedImage frame = ImageIO.read(Objects.requireNonNull(is));

                    // Co giãn và thêm vào danh sách
                    this.scaledBallFrames.add(scaleImage(frame));
                    i++;
                }
            } catch (Exception e) {
                // Mong đợi lỗi này khi hết khung hình
                if (this.scaledBallFrames.isEmpty()) {
                    System.err.println("Không tải được GIF nào từ thư mục: " + path);
                }
            }
        }


        if (this.scaledBallFrames.isEmpty()) {
            try {
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
                BufferedImage img = ImageIO.read(Objects.requireNonNull(is));
                this.scaledBallFrames.add(scaleImage(img)); // Thêm 1 ảnh
            } catch (Exception e) {
                System.err.println("Không thể tải ảnh cho bóng tại: " + path);
            }
        }
    }

    /**
     * THÊM MỚI: Phương thức trợ giúp để co giãn ảnh.
     */
    private Image scaleImage(BufferedImage originalImage) {
        int diameter = this.r * 2; // Kích thước của bóng
        BufferedImage scaledImg = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImg.createGraphics();
        g.drawImage(originalImage, 0, 0, diameter, diameter, null);
        g.dispose();
        return scaledImg;
    }


    // (Getters/Setters giữ nguyên)
    public int getX() { return (int) this.center.getX(); }
    public int getY() { return (int) this.center.getY(); }
    public int getSize() { return this.r; }
    public void setVelocity(Velocity v) { this.velocity = v; }

    public Velocity getVelocity() { return this.velocity; }

    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
        game.removeBallFromList(this);
    }


    @Override
    public void drawOn(DrawSurface surface) {
        if (!this.scaledBallFrames.isEmpty()) {
            // 1. Lấy khung hình GIF hiện tại
            Image currentImage = this.scaledBallFrames.get(this.currentFrame);

            // 2. Tính toán vị trí góc trên-trái (top-left) để vẽ
            int drawX = getX() - this.r;
            int drawY = getY() - this.r;

            // 3. Vẽ ảnh
            surface.drawImage(drawX, drawY, currentImage);

        } else {
            // 4. Dự phòng: Nếu không tải được ảnh
            draw3DFallback(surface);
        }
    }


    private void draw3DFallback(DrawSurface surface) {
        int x = getX();
        int y = getY();
        int r = getSize();
        Color highlightColor = Color.RED.brighter();
        Color shadowColor = Color.RED.darker().darker();
        int offset = 1;

        surface.setColor(shadowColor);
        surface.fillCircle(x + offset, y + offset, r);
        surface.setColor(highlightColor);
        surface.fillCircle(x - offset, y - offset, r);
        surface.setColor(Color.RED);
        surface.fillCircle(x, y, r);
    }



    /**
     * THAY ĐỔI: Chia nhỏ chuyển động để sửa lỗi xuyên gạch (Tunneling).
     */
    @Override
    public void timePassed() {

        if (this.scaledBallFrames.size() > 1) {
            this.frameDelayCounter++;
            if (this.frameDelayCounter >= ANIMATION_DELAY) {
                this.frameDelayCounter = 0;
                this.currentFrame++;
                if (this.currentFrame >= this.scaledBallFrames.size()) {
                    this.currentFrame = 0;
                }
            }
        }

        int steps = 12;


        double smallDx = this.velocity.getDx() / steps;
        double smallDy = this.velocity.getDy() / steps;


        this.velocity = new Velocity(smallDx, smallDy);


        for (int i = 0; i < steps; i++) {
            moveOneStep();
        }


        this.velocity = new Velocity(this.velocity.getDx() * steps, this.velocity.getDy() * steps);
    }


    public Point moveNear(Point collisionPoint) {
        double xCenter = collisionPoint.getX();
        double yCenter = collisionPoint.getY();
        if (this.getVelocity().getDx() > 0) {
            xCenter = collisionPoint.getX() - 1;
        }
        if (this.getVelocity().getDx() < 0) {
            xCenter = collisionPoint.getX() + 1;
        }
        if (this.getVelocity().getDy() > 0) {
            yCenter = collisionPoint.getY() - 1;
        }
        if (this.getVelocity().getDy() < 0) {
            yCenter = collisionPoint.getY() + 1;
        }
        return new Point(xCenter, yCenter);
    }
    public void moveOneStep() {
        Point nextPosition = this.getVelocity().applyToPoint(this.center);
        Line trajectory = new Line(this.center, nextPosition);
        CollisionInfo collisionInfo = this.gameEnvironment.getClosestCollision(trajectory);
        if (collisionInfo != null) {
            this.center = moveNear(collisionInfo.collisionPoint());
            Velocity newVelocity = collisionInfo.collisionObject().
                    hit(this, collisionInfo.collisionPoint(), this.velocity);
            this.setVelocity(newVelocity);
        } else {
            this.center = nextPosition;
        }
    }
}