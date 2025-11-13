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
import sounds.AudioPlayer;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;

public class GameFlow {
    private final AnimationRunner animationRunner;
    private final KeyboardSensor keyboardSensor;
    private final MouseSensor mouseSensor;
    private final GUI gui;
    private final List<LevelInformation> levels;
    private final ScorePersistence sp;
    private final Counter currentScore;
    private final AudioPlayer audioPlayer;
    private static final String MENU_MUSIC = "resources/sounds/menu_music.wav";



    public GameFlow(AnimationRunner ar, KeyboardSensor ks, MouseSensor ms, GUI gui, List<LevelInformation> levels) {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.mouseSensor = ms;
        this.gui = gui;
        this.levels = levels;
        this.sp = new ScorePersistence();
        this.currentScore = new Counter(0);
        this.audioPlayer = new AudioPlayer();
    }


    public void runGame() {
        while (true) {
            this.audioPlayer.loop(MENU_MUSIC);
            MenuScreen menu = new MenuScreen(this.keyboardSensor, this.mouseSensor);
            this.animationRunner.run(menu);
            MenuSelection selection = menu.getSelection();
            menu.resetSelection();

            String playerName;

            switch (selection) {
                case PLAY:
                    playerName = JOptionPane.showInputDialog(null,
                            "Enter your name:",
                            "Player Name",
                            JOptionPane.PLAIN_MESSAGE);
                    if (playerName == null || playerName.trim().isEmpty()) {
                        playerName = "Anonymous";
                    }
                    this.audioPlayer.stop(MENU_MUSIC);
                    try {
                        this.currentScore.decrease(this.currentScore.getValue());
                        this.runLevels(this.levels, this.currentScore, playerName);
                    } catch (IOException e) {
                        System.err.println("Không thể chạy các level: " + e.getMessage());
                    }
                    break;

                case RANKING:
                    try {

                        HighScoreScreen hsScreen = new HighScoreScreen(this.sp, this.keyboardSensor, this.mouseSensor);
                        this.animationRunner.run(hsScreen);

                    } catch (Exception e) {
                        System.err.println("Không thể đọc điểm cao: " + e.getMessage());
                    }
                    break;
                case EXIT:
                    this.audioPlayer.stop(MENU_MUSIC);
                    this.gui.close();
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }


    public void runLevels(List<LevelInformation> levels, Counter score, String playerName) throws IOException {
        boolean isWin = true;
        for (LevelInformation levelInfo : levels) {

            String currentLevelMusic = levelInfo.getBackgroundMusicPath();
            GameLevel level = new GameLevel(levelInfo, this.animationRunner, this.keyboardSensor, score, this.audioPlayer);
            level.initialize();
            if (currentLevelMusic != null) {
                this.audioPlayer.loop(currentLevelMusic);
            }
            level.playOneTurn();
            if (currentLevelMusic != null) {
                this.audioPlayer.stop(currentLevelMusic);
            }
            MenuSelection pauseResult = level.getPauseChoice();
            if (pauseResult == MenuSelection.EXIT) {
                isWin = false;
                break;
            }
            if (level.getBallsCount().getValue() == 0) {
                isWin = false;
                sp.addScore(playerName, score.getValue());

                EndScreenLose loseScreen = new EndScreenLose(this.keyboardSensor);
                this.animationRunner.run(loseScreen);


                this.animationRunner.run(new HighScoreScreen(this.sp, this.keyboardSensor, this.mouseSensor));


                break;
            }
            score.increase(100);
        }

        // --- LOGIC THẮNG ---
        if (isWin) {
            sp.addScore(playerName, score.getValue());
            this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor,
                    "space", new EndScreenWin(score)));


            this.animationRunner.run(new HighScoreScreen(this.sp, this.keyboardSensor, this.mouseSensor));

        }
    }
}