package animation;

import biuoop.Sleeper;
import biuoop.DrawSurface;
import game.SpriteCollection;

import java.awt.Color;

public class CountdownAnimation implements Animation {

    private double numOfSeconds;
    private int countFrom;
    private SpriteCollection gameScreen;
    private boolean stop;
    private Sleeper sleeper;
    private long sleepTimePerCount;
    private boolean firstFrame;

    public CountdownAnimation(double numOfSeconds,
                              int countFrom,
                              SpriteCollection gameScreen) {
        this.numOfSeconds = numOfSeconds;
        this.countFrom = countFrom;
        this.gameScreen = gameScreen;
        this.stop = false;
        this.sleeper = new Sleeper();
        // Tính toán thời gian ngủ cho mỗi số đếm
        this.sleepTimePerCount = (long) (numOfSeconds * 1000 / (double) countFrom);
        this.firstFrame = true;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        // SỬA LỖI LOGIC: Không ngủ ở khung hình đầu tiên, chỉ ngủ *giữa* các số
        if (firstFrame) {
            firstFrame = false;
        } else {
            // Ngủ trong một khoảng thời gian đã tính toán
            this.sleeper.sleepFor(this.sleepTimePerCount);
        }

        // SỬA LỖI LOGIC: Dừng *trước khi* vẽ số 0
        if (this.countFrom <= 0) {
            this.stop = true;
            return;
        }

        // Vẽ màn hình game và số đếm
        gameScreen.drawAllOn(d);
        d.setColor(Color.RED);
        d.drawText(d.getWidth() / 2 - 16, d.getHeight() / 2, Integer.toString(countFrom), 66);

        // Giảm số đếm
        countFrom--;
    }

    public boolean shouldStop() {
        return this.stop;
    }
}
