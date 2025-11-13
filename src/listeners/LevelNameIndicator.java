package listeners;

import game.GameLevel;
import biuoop.DrawSurface;
import game.Sprite;


public class LevelNameIndicator implements Sprite {
    private String levelName;


    public LevelNameIndicator(String levelName) {
        this.levelName = levelName;
    }

    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }


    public void drawOn(DrawSurface d) {
        d.drawText(450, 13, "Level Name: " + levelName, 13);
    }


    public void timePassed() {

    }
}
