package game;

import animation.AnimationRunner;
import animation.EndScreenLose;
import animation.EndScreenWin;
import animation.HighScoreScreen;
import animation.KeyPressStoppableAnimation;
import menu.MenuScreen;
import menu.MenuSelection;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import menu.MouseSensor;
import levels.LevelInformation;

// --- CÁC IMPORT BỊ THIẾU ĐÃ ĐƯỢC THÊM VÀO ---
import game.Counter; // (Giả sử Counter ở trong package 'game')
import game.ScorePersistence; // (Giả sử ScorePersistence ở trong package 'game')
// ------------------------------------------

import java.io.IOException;
import java.util.List;

/**
 * Quản lý vòng lặp game chính, bao gồm menu và các level.
 * (ĐÃ CẬP NHẬT để xử lý logic tạm dừng từ GameLevel)
 */
public class GameFlow {
    private AnimationRunner animationRunner;
    private KeyboardSensor keyboardSensor;
    private MouseSensor mouseSensor; // <-- Bạn đã có cái này, tốt!
    private GUI gui;
    private List<LevelInformation> levels;

    private ScorePersistence sp;
    private Counter currentScore;

    /**
     * Hàm khởi tạo mới, chấp nhận tất cả các cảm biến.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, MouseSensor ms, GUI gui, List<LevelInformation> levels) throws Exception {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.mouseSensor = ms; // <-- Bạn đã có cái này, tốt!
        this.gui = gui;
        this.levels = levels;
        this.sp = new ScorePersistence();
        this.currentScore = new Counter(0);
    }

    /**
     * Vòng lặp game chính. Hiển thị menu và xử lý các lựa chọn.
     * (Phần này của bạn đã CHÍNH XÁC)
     */
    public void runGame() {
        while (true) {
            // Truyền cả keyboard VÀ mouse vào MenuScreen
            MenuScreen menu = new MenuScreen(this.keyboardSensor, this.mouseSensor);
            this.animationRunner.run(menu);

            MenuSelection selection = menu.getSelection();
            menu.resetSelection();

            switch (selection) {
                case PLAY:
                case RESTART:
                    try {
                        this.currentScore.decrease(this.currentScore.getValue()); // Đặt lại về 0
                        this.runLevels(this.levels, this.currentScore);
                    } catch (IOException e) {
                        System.err.println("Không thể chạy các level: " + e.getMessage());
                    }
                    break;

                case HIGH_SCORE:
                    try {
                        HighScoreScreen hsScreen = new HighScoreScreen(sp.readFromFile());
                        this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                                "space", hsScreen));
                    } catch (Exception e) {
                        System.err.println("Không thể đọc điểm cao: " + e.getMessage());
                    }
                    break;

                case EXIT:
                    this.gui.close();
                    System.exit(0);
                    break;
                default:
                    // (NONE) - Không làm gì cả, vòng lặp menu sẽ chạy lại
                    break;
            }
        }
    }

    /**
     * Chạy các level theo thứ tự.
     * (ĐÂY LÀ PHƯƠNG THỨC ĐÃ ĐƯỢC SỬA LẠI HOÀN TOÀN)
     */
    public void runLevels(List<LevelInformation> levels, Counter score) throws IOException {
        boolean isWin = true; // Giả sử người chơi sẽ thắng

        // Chúng ta dùng vòng lặp for-i thay vì for-each để có thể xử lý 'RESTART'
        for (int i = 0; i < levels.size(); i++) {
            LevelInformation levelInfo = levels.get(i);

            // 1. Tạo GameLevel, lần này TRUYỀN CẢ MOUSE SENSOR
            GameLevel level = new GameLevel(levelInfo, this.animationRunner, this.keyboardSensor, this.mouseSensor, score);
            level.initialize();

            // 2. Chơi một lượt VÀ LẤY KẾT QUẢ TẠM DỪNG
            // playOneTurn() bây giờ sẽ tự chạy vòng lặp game cho đến khi level kết thúc
            MenuSelection pauseResult = level.playOneTurn();

            // 3. Xử lý kết quả từ menu tạm dừng (Pause Menu)
            if (pauseResult == MenuSelection.RESTART) {
                i--; // Giảm 'i' để vòng lặp for sẽ chạy lại level này
                continue; // Quay lại đầu vòng lặp
            }
            if (pauseResult == MenuSelection.EXIT) {
                isWin = false; // Người chơi không thắng toàn bộ game
                break; // Thoát khỏi vòng lặp 'for' (quay lại Main Menu)
            }

            // 4. Xử lý kết quả Thắng/Thua bình thường (nếu pauseResult là NONE)
            if (level.getBallsCount().getValue() == 0) {
                // Người chơi thua (hết bóng)
                isWin = false;
                sp.updateFileIfHigher(score.getValue());
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                        "space", new EndScreenLose(score)));
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                        "space", new HighScoreScreen(sp.readFromFile())));
                break; // Thoát khỏi vòng lặp 'for' (quay lại Main Menu)
            }

            // Nếu chúng ta đến đây, người chơi đã thắng level này
            score.increase(100); // Thưởng điểm khi qua màn
        }

        // 5. Hiển thị màn hình chiến thắng (chỉ khi isWin vẫn là true)
        if (isWin) {
            sp.updateFileIfHigher(score.getValue());
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new EndScreenWin(score)));
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new HighScoreScreen(sp.readFromFile())));
        }
    }
}