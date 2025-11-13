package animation;

import biuoop.DrawSurface;


public class HighScoreScreen implements Animation {
    private boolean stop;
    private int score;


    public HighScoreScreen(int score) {
        this.stop = false;
        this.score = score;
    }


    public void doOneFrame(DrawSurface d) {
        d.drawText(d.getWidth() / 5, d.getHeight() / 2, "High Score is " + score, 35);
    }


    public boolean shouldStop() {
        return this.stop;
    }
}