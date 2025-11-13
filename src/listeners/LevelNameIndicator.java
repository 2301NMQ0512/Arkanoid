package listeners;

import game.GameLevel;
import biuoop.DrawSurface;
import game.Sprite;

/**
 * The Level Name Indicator will hold a reference to the name of the level,
 * and will be added to the game as a sprite positioned at the top of the screen.
 *
 * 
 */
public class LevelNameIndicator implements Sprite {
    private String levelName;

    /**
     * Instantiates a new indicator.
     *
     * @param levelName the name of the level
     */
    public LevelNameIndicator(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Add the name to the game.
     *
     * @param g the game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    /**
     * Prints the name of the level on the drawSurface.
     *
     * @param d the drawing surface
     */
    public void drawOn(DrawSurface d) {
        d.drawText(450, 13, "Level Name: " + levelName, 13);
    }

    /**
     * Nothing changes over time.
     */
    public void timePassed() {
//nothing
    }
}
