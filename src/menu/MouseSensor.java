package menu;

import biuoop.GUI;
import biuoop.KeyboardSensor;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;


public class MouseSensor extends MouseAdapter implements MouseMotionListener {
    public static final int LEFT_CLICK = MouseEvent.BUTTON1;

    private int x;
    private int y;
    private final Set<Integer> pressedButtons = new HashSet<>();


    public MouseSensor(GUI gui, KeyboardSensor keyboard) {
        try {

            Field panelField = gui.getClass().getDeclaredField("drawingPanel");
            panelField.setAccessible(true);
            Component drawingPanel = (Component) panelField.get(gui);

            drawingPanel.addMouseListener(this);
            drawingPanel.addMouseMotionListener(this);


            if (keyboard instanceof KeyListener) {
                drawingPanel.addKeyListener((KeyListener) keyboard);
            }


            drawingPanel.setFocusable(true);

            drawingPanel.requestFocusInWindow();


        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not attach MouseSensor to GUI. "
                    + "The 'drawingPanel' field might have been renamed.", e);
        }
    }



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