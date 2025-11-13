package listeners;

import game.Counter;
import game.GameLevel;
import biuoop.DrawSurface;
import game.Sprite;

import java.awt.Color;


public class ScoreIndicator implements Sprite {
    private Counter score;


    public ScoreIndicator(Counter score) {
        this.score = score;
    }


    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }



    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText(300, 13, "Score: " + score.getValue(), 13);
    }


    public void timePassed() {
    }
}
