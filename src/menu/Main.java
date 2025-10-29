package menu;

import javax.swing.JFrame;
import game.GameLevel;
import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Arkanoid");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                GameLevel level = new GameLevel(800, 600);
                frame.setContentPane(level);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                level.requestFocusInWindow();
            }
        });
    }
}
