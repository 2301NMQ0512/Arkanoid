package menu; // Hoặc một package chung như 'game'

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Quản lý việc đọc và ghi điểm cao vào tệp.
 */
public class HighScoreManager {
    // Tên tệp lưu điểm cao
    private static final String FILENAME = "highscore.txt";
    private static int highScore = -1; // Lưu vào bộ nhớ đệm

    /**
     * Tải điểm cao từ tệp.
     * @return điểm cao nhất, hoặc 0 nếu không tìm thấy.
     */
    public static int loadHighScore() {
        if (highScore != -1) {
            return highScore; // Trả về từ bộ đệm nếu đã tải
        }

        File f = new File(FILENAME);
        if (!f.exists()) {
            highScore = 0;
            return 0; // Tệp chưa tồn tại
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
                return highScore;
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Không thể đọc tệp điểm cao: " + e.getMessage());
            highScore = 0; // Đặt lại nếu tệp bị hỏng
            return 0;
        }
        highScore = 0;
        return 0;
    }

    /**
     * Lưu điểm cao mới nếu nó cao hơn điểm hiện tại.
     * @param score điểm số mới
     */
    public static void saveHighScore(int score) {
        if (score > loadHighScore()) {
            highScore = score; // Cập nhật bộ đệm
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
                writer.write(String.valueOf(score));
            } catch (IOException e) {
                System.err.println("Không thể lưu điểm cao: " + e.getMessage());
            }
        }
    }
}