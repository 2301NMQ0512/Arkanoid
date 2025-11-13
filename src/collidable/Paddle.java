package collidable;

import ball.Ball;
import ball.Velocity;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import game.Sprite;

import java.awt.Color;

public class Paddle implements Sprite, Collidable {
    private int thespeed; // Tốt! Đây không phải là static
    private Color color;
    private Rectangle Paddle; // Lưu ý: Tên biến nên bắt đầu bằng chữ thường, ví dụ: 'paddle'
    private static double guiWidthLeft = 10;
    private static double guiWidthRight = 780;
    private biuoop.KeyboardSensor keyboard;
    private double[] regionBorders = new double[4];

    // Lưu trạng thái gốc
    private final int originalSpeed;
    private final double originalWidth;

    // Bộ đếm thời gian (giá trị 0 có nghĩa là không hoạt động)
    private long expandTimer;
    private long speedTimer;

    // Thời lượng power-up (ví dụ: 7000 mili giây = 7 giây)
    private static final long POWERUP_DURATION = 3000;
    public Paddle(Rectangle Paddle, Color color, biuoop.KeyboardSensor keyboard, int thespeed) {
        this.color = color;
        this.Paddle = Paddle;
        this.keyboard = keyboard;
        this.thespeed = thespeed; // <-- SỬA LỖI QUAN TRỌNG Ở ĐÂY

        this.originalSpeed = thespeed;
        this.originalWidth = Paddle.getWidth();

        // Khởi tạo bộ đếm thời gian là không hoạt động
        this.expandTimer = 0;
        this.speedTimer = 0;
    }

    public void moveLeft() {
        double newX = this.Paddle.getUpperLeft().getX() - thespeed;
        if (newX <= guiWidthLeft) {
            newX = guiWidthLeft;
        }
        this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                this.Paddle.getWidth(), this.Paddle.getHeight());
    }

    public void moveRight() {
        double newX = this.Paddle.getUpperLeft().getX() + thespeed;
        if (newX + this.Paddle.getWidth() >= guiWidthRight) {
            newX = guiWidthRight - this.Paddle.getWidth();
        }
        this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                this.Paddle.getWidth(), this.Paddle.getHeight());
    }

    /**
     * draws the Paddle.
     *
     * @param d the drawing surface
     */
    public void drawOn(DrawSurface d) {
        d.setColor(color);
        d.drawRectangle((int) this.Paddle.getUpperLeft().getX(),
                (int) this.Paddle.getUpperLeft().getY(),
                (int) this.Paddle.getWidth(), (int) this.Paddle.getHeight());
        d.fillRectangle((int) Paddle.getUpperLeft().getX(),
                (int) this.Paddle.getUpperLeft().getY(),
                (int) this.Paddle.getWidth(), (int) this.Paddle.getHeight());
        d.setColor(Color.black);
        d.drawRectangle((int) this.Paddle.getUpperLeft().getX(),
                (int) this.Paddle.getUpperLeft().getY(),
                (int) this.Paddle.getWidth(), (int) this.Paddle.getHeight());
    }

    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft();
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight();
        }
        checkTimers(); // Kiểm tra xem power-up có hết hạn không

    }

    public Rectangle getCollisionRectangle() {
        return this.Paddle;
    }

    public int checkRegion(Point collisionPoint) {
        double PaddleStartingX = Paddle.getUpperLeft().getX();
        // because we have 5 regions
        double collisionPointX = collisionPoint.getX();
        for (int i = 0; i < 4; i++) {
            regionBorders[i] = (Paddle.getWidth() / 4) * i + 1;
        }
        if (collisionPointX >= PaddleStartingX && collisionPointX <= PaddleStartingX + regionBorders[0]) {
            return 1; //hit region 1
        }
        if (collisionPointX >= PaddleStartingX + regionBorders[0]
                && collisionPointX <= PaddleStartingX + regionBorders[1]) {
            return 2;
        }
        if (collisionPointX >= PaddleStartingX + regionBorders[1]
                && collisionPointX <= PaddleStartingX + regionBorders[2]) {
            return 3;
        }
        if (collisionPointX >= PaddleStartingX + regionBorders[2]
                && collisionPointX <= PaddleStartingX + regionBorders[3]) {
            return 4;
        }
        if (collisionPointX >= PaddleStartingX + regionBorders[3]
                && collisionPointX <= PaddleStartingX + Paddle.getWidth()) {
            return 5;
        }
        return 0;
    }

    public void changeVelocity(double speed, Velocity[] velo,
                               Velocity currVel) {
        double dx = currVel.getDx();
        double dy = currVel.getDy();
        velo[1] = Velocity.fromAngleAndSpeed(-60, speed);
        velo[2] = Velocity.fromAngleAndSpeed(-30, speed);
        velo[3] = new Velocity(dx, -dy);
        velo[4] = Velocity.fromAngleAndSpeed(30, speed);
        velo[5] = Velocity.fromAngleAndSpeed(60, speed);
        velo[0] = new Velocity(dx, dy);
    }

    public Velocity angleChange(Point collisionPoint,
                                int region, Velocity currVel) {
        Velocity[] velo = new Velocity[6];
        // Bạn đã sửa lỗi trong Ball.java, Velocity của bạn dùng dx/dy. Tốt!
        double speed = Math.sqrt((currVel.getDx() * currVel.getDx())
                + (currVel.getDy() * currVel.getDy()));
        changeVelocity((int) speed, velo, currVel);
        Velocity v = velo[region];
        return v;
    }

    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        int region = this.checkRegion(collisionPoint);
        return angleChange(collisionPoint, region, currentVelocity);
    }

    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * Increases the width of the Paddle.
     * Re-centers the Paddle to avoid shifting.
     */
    /**
     * Tăng chiều rộng của Paddle VÀ đặt bộ đếm thời gian hết hạn.
     */
    public void expand() {
        // --- CẬP NHẬT CHÍNH ---
        // Đặt (hoặc đặt lại) bộ đếm thời gian hết hạn thành 7 giây kể từ bây giờ
        this.expandTimer = System.currentTimeMillis() + POWERUP_DURATION;

        // Logic cũ của bạn để ngăn mở rộng vô hạn
        double currentWidth = this.Paddle.getWidth();
        double maxWidth = 300; // Giới hạn chiều rộng tối đa

        // Nếu đã ở mức tối đa, chúng ta vẫn đặt lại bộ đếm thời gian, nhưng không cần làm gì thêm
        if (currentWidth >= maxWidth) {
            return;
        }

        double newWidth = currentWidth * 1.5; // Rộng hơn 50%
        if (newWidth > maxWidth) {
            newWidth = maxWidth;
        }

        // Tính toán X mới để giữ paddle ở giữa
        double currentX = this.Paddle.getUpperLeft().getX();
        double newX = currentX - (newWidth - currentWidth) / 2;

        // Ngăn không cho mở rộng ra ngoài màn hình
        if (newX < guiWidthLeft) {
            newX = guiWidthLeft + 1;
        }
        if (newX + newWidth > guiWidthRight) {
            newX = guiWidthRight - newWidth - 1;
        }

        this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                newWidth, this.Paddle.getHeight());
    }

    /**
     * Tăng tốc độ di chuyển của Paddle VÀ đặt bộ đếm thời gian hết hạn.
     */
    public void increaseSpeed() {
        // --- CẬP NHẬT CHÍNH ---
        // Đặt (hoặc đặt lại) bộ đếm thời gian hết hạn thành 7 giây kể từ bây giờ
        this.speedTimer = System.currentTimeMillis() + POWERUP_DURATION;

        // Logic cũ của bạn để ngăn tăng tốc độ vô hạn
        if (thespeed < 16) {
            thespeed += 10;
        }
    }

    // --- THÊM 3 PHƯƠNG THỨC MỚI SAU ĐÂY VÀO CUỐI TỆP ---

    /**
     * Được gọi bởi timePassed(), kiểm tra xem có bộ đếm thời gian nào đã hết hạn không.
     */
    private void checkTimers() {
        long currentTime = System.currentTimeMillis();

        // Kiểm tra bộ đếm thời gian mở rộng
        if (this.expandTimer > 0 && currentTime > this.expandTimer) {
            resetSize();
            this.expandTimer = 0; // Hủy kích hoạt bộ đếm thời gian
        }

        // Kiểm tra bộ đếm thời gian tốc độ
        if (this.speedTimer > 0 && currentTime > this.speedTimer) {
            resetSpeed();
            this.speedTimer = 0; // Hủy kích hoạt bộ đếm thời gian
        }
    }

    /**
     * Hoàn tác hiệu ứng mở rộng, trả paddle về chiều rộng ban đầu.
     */
    private void resetSize() {
        double currentWidth = this.Paddle.getWidth();
        // Chỉ đặt lại nếu nó chưa có kích thước gốc
        if (currentWidth == this.originalWidth) {
            return;
        }

        double currentX = this.Paddle.getUpperLeft().getX();
        // Căn giữa lại paddle khi nó co lại
        double newX = currentX + (currentWidth - this.originalWidth) / 2;

        this.Paddle = new Rectangle(new Point(newX, this.Paddle.getUpperLeft().getY()),
                this.originalWidth, this.Paddle.getHeight());
    }

    /**
     * Hoàn tác hiệu ứng tốc độ, trả paddle về tốc độ ban đầu.
     */
    private void resetSpeed() {
        this.thespeed = this.originalSpeed;
    }
}
// <-- Tôi đã xóa bình luận thừa ở cuối tệp của bạn -->