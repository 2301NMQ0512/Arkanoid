package game; // (Hoặc package của bạn)

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ScorePersistence implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String FILENAME = "highscores.ser"; // Tên tệp lưu
    private static final int MAX_SCORES_TO_KEEP = 10;


    @SuppressWarnings("unchecked")
    public List<ScoreInfo> readScores() {
        List<ScoreInfo> scores = new ArrayList<>();
        File file = new File(FILENAME);

        if (!file.exists()) {
            return scores;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            scores = (List<ScoreInfo>) ois.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("Không thể đọc tệp điểm cao. Tạo tệp mới: " + e.getMessage());
            file.delete();
            return new ArrayList<>();
        }

        sortScores(scores);
        return scores;
    }


    public void addScore(String playerName, int newScore) {
        List<ScoreInfo> scores = readScores();

        ScoreInfo existingEntry = null;
        int existingScore = -1;


        for (ScoreInfo info : scores) {
            if (info.getPlayerName().equalsIgnoreCase(playerName)) {
                existingEntry = info;
                existingScore = info.getScore();
                break; // Đã tìm thấy
            }
        }


        if (existingEntry != null) {
            if (newScore > existingScore) {
                scores.remove(existingEntry);
                scores.add(new ScoreInfo(playerName, newScore));
            } else {
                return;
            }
        } else {
            scores.add(new ScoreInfo(playerName, newScore));
        }

        sortScores(scores);

        while (scores.size() > MAX_SCORES_TO_KEEP) {
            scores.removeLast();
        }

        writeScores(scores); // Lưu danh sách đã cập nhật
    }
    // -----------------------------------------------------

    /**
     * Ghi đè tệp điểm cao bằng danh sách mới.
     * (Giữ nguyên, không thay đổi)
     */
    private void writeScores(List<ScoreInfo> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Không thể lưu tệp điểm cao: " + e.getMessage());
        }
    }

    /**
     * Sắp xếp danh sách, điểm cao nhất lên đầu.
     * (Giữ nguyên, không thay đổi)
     */
    private void sortScores(List<ScoreInfo> scores) {
        scores.sort(Comparator.comparingInt(ScoreInfo::getScore).reversed());
    }
    public void clearScores() {
        File file = new File(FILENAME);
        if (file.exists()) {
            try {
                if (file.delete()) {
                    System.out.println("Đã xóa tệp điểm cao.");
                } else {
                    System.err.println("Không thể xóa tệp điểm cao.");
                }
            } catch (SecurityException e) {
                System.err.println("Lỗi bảo mật khi xóa tệp: " + e.getMessage());
            }
        }
    }
}