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
import menu.MouseSensor; // (Nếu bạn đang dùng MouseSensor)
import levels.LevelInformation;

import java.io.IOException;
import java.util.List;

public class GameFlow {
    private final AnimationRunner animationRunner;
    private final KeyboardSensor keyboardSensor;
    private final MouseSensor mouseSensor; // (Nếu bạn đang dùng MouseSensor)
    private final GUI gui;
    private final List<LevelInformation> levels;
    private final ScorePersistence sp;
    private final Counter currentScore;

    // Hàm khởi tạo của bạn (giữ nguyên)
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, MouseSensor ms, GUI gui, List<LevelInformation> levels) throws Exception {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.mouseSensor = ms;
        this.gui = gui;
        this.levels = levels;
        this.sp = new ScorePersistence();
        this.currentScore = new Counter(0);
    }

    // runGame() của bạn (giữ nguyên)
    public void runGame() {
        while (true) {
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
                    break;
            }
        }
    }


    /**
     * Chạy các level theo thứ tự.
     * (ĐÃ CẬP NHẬT để kiểm tra lựa chọn tạm dừng)
     */
    public void runLevels(List<LevelInformation> levels, Counter score) throws IOException {
        boolean isWin = true;
        for (LevelInformation levelInfo : levels) {

            GameLevel level = new GameLevel(levelInfo, this.animationRunner, this.keyboardSensor, score);
            level.initialize();

            // Chạy level cho đến khi nó tự dừng
            level.playOneTurn();
            // (playOneTurn() sẽ chạy this.runner.run(level),
            // và GameLevel.shouldStop() sẽ dừng nó)

            // --- KIỂM TRA KẾT QUẢ ---

            // 1. Kiểm tra xem người dùng có chọn "EXIT" từ menu tạm dừng không
            MenuSelection pauseResult = level.getPauseChoice();
            if (pauseResult == MenuSelection.EXIT) {
                isWin = false; // Người chơi không thắng toàn bộ game
                break; // Thoát khỏi vòng lặp 'for' (quay lại Main Menu)
            }

            // 2. Kiểm tra xem người dùng có thua (hết bóng) không
            if (level.getBallsCount().getValue() == 0) {
                isWin = false;
                sp.updateFileIfHigher(score.getValue());
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                        "space", new EndScreenLose(score)));
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                        "space", new HighScoreScreen(sp.readFromFile())));
                break; // Thoát khỏi vòng lặp 'for' (quay lại Main Menu)
            }

            // 3. Nếu chúng ta đến đây, người chơi đã thắng level này
            score.increase(100); // Thưởng điểm khi qua màn
        }

        // 4. Hiển thị màn hình chiến thắng (chỉ khi isWin vẫn là true)
        if (isWin) {
            sp.updateFileIfHigher(score.getValue());
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new EndScreenWin(score)));
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new HighScoreScreen(sp.readFromFile())));
        }
    }
}
