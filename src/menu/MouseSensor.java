package menu;

import biuoop.GUI;
import biuoop.KeyboardSensor; // <-- 1. IMPORT
import java.awt.*;
import java.awt.event.KeyListener; // <-- 2. IMPORT
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Theo dõi vị trí chuột và các nút được nhấn.
 * (Đã cập nhật để sửa lỗi focus của bàn phím)
 */
public class MouseSensor extends MouseAdapter implements MouseMotionListener {
    public static final int LEFT_CLICK = MouseEvent.BUTTON1;
    public static final int MIDDLE_CLICK = MouseEvent.BUTTON2;
    public static final int RIGHT_CLICK = MouseEvent.BUTTON3;

    private int x;
    private int y;
    private final Set<Integer> pressedButtons = new HashSet<>();

    /**
     * Hàm khởi tạo ĐÃ CẬP NHẬT.
     * Nó chấp nhận cả GUI và KeyboardSensor để sửa lỗi focus.
     */
    public MouseSensor(GUI gui, KeyboardSensor keyboard) { // <-- 3. HÀM KHỞI TẠO 2 THAM SỐ
        try {
            // Lấy 'drawingPanel' riêng tư từ GUI bằng Reflection
            Field panelField = gui.getClass().getDeclaredField("drawingPanel");
            panelField.setAccessible(true);
            Component drawingPanel = (Component) panelField.get(gui);

            // --- Đính kèm các Listener cho CHUỘT ---
            drawingPanel.addMouseListener(this);
            drawingPanel.addMouseMotionListener(this);

            // --- SỬA LỖI FOCUS CHO BÀN PHÍM ---
            // 4. Đính kèm listener của bàn phím vào 'drawingPanel'
            if (keyboard instanceof KeyListener) {
                drawingPanel.addKeyListener((KeyListener) keyboard);
            }

            // 5. Yêu cầu 'drawingPanel' phải có khả năng nhận focus
            drawingPanel.setFocusable(true);
            // 6. Yêu cầu nó nhận focus ngay lập tức khi cửa sổ mở
            drawingPanel.requestFocusInWindow();
            // --- KẾT THÚC SỬA LỖI ---

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not attach MouseSensor to GUI. "
                    + "The 'drawingPanel' field might have been renamed.", e);
        }
    }

    // ... (Phần còn lại của tệp: getX, getY, mousePressed, ...) ...

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isPressed(int button) { return pressedButtons.contains(button); }

    @Override
    public void mousePressed(MouseEvent e) { pressedButtons.add(e.getButton()); }

    @Override
    public void mouseReleased(MouseEvent e) { pressedButtons.remove(e.getButton()); }

    @Override
    public void mouseMoved(MouseEvent e) { updatePosition(e); }

    @Override
    public void mouseDragged(MouseEvent e) { updatePosition(e); }

    private void updatePosition(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }
}