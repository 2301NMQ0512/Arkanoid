package listeners;

import game.Counter;
import game.GameLevel;
import biuoop.DrawSurface;
import game.Sprite;

import java.awt.Color;

/**
 * The ScoreIndicator will hold a reference to the scores counter
 * and will be added to the game as a sprite positioned at the top of the screen.
 *
 * 
 */
public class ScoreIndicator implements Sprite {
    private Counter score;

    /**
     * Instantiates a new Score indicator.
     *
     * @param score the score
     */
    public ScoreIndicator(Counter score) {
        this.score = score;
    }

    /**
     * Add the score indicator to the game.
     *
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    /**
     * Prints the score on the drawSurface.
     *
     * @param d the drawing surface
     */

    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText(300, 13, "Score: " + score.getValue(), 13);
    }

    /**
     * Nothing changes over time.
     */
    public void timePassed() {
//nothing
    }
}
