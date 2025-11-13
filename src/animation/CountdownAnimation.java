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


    public CountdownAnimation(double numOfSeconds,
                              int countFrom,
                              SpriteCollection gameScreen) {
        this.numOfSeconds = numOfSeconds;
        this.countFrom = countFrom;
        this.gameScreen = gameScreen;
        this.stop = false;

    }


    @Override
    public void doOneFrame(DrawSurface d) {
        gameScreen.drawAllOn(d);
        Sleeper mySleeper = new Sleeper();
        d.setColor(Color.red);
        d.drawText(d.getWidth() / 2, d.getHeight() / 2, Integer.toString(countFrom--), 66);

        if (this.countFrom == 2) {
            return;
        }
        numOfSeconds--;
        mySleeper.sleepFor(1000);
        if (numOfSeconds < 0) {
            this.stop = true;
//        }
        }
    }
    public boolean shouldStop() {
        return this.stop;
    }
}