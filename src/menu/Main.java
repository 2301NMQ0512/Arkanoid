import javax.swing.JFrame;
import game.GameLevel;
import java.awt.Dimension;
import java.awt.EventQueue;

class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Arkanoid - Minimal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int w = 800, h = 600;
            GameLevel level = new GameLevel(w, h);
            level.setPreferredSize(new Dimension(w, h));
            frame.setContentPane(level);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            level.requestFocusInWindow();
            level.start();
        });
    }
}
