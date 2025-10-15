package game;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;

public class SpriteCollection {
    private List<Sprite> sprites = new ArrayList<>();

    public void addSprite(Sprite s) { sprites.add(s); }
    public void removeSprite(Sprite s) { sprites.remove(s); }

    public void drawAllOn(Graphics g) {
        for (Sprite s : new ArrayList<>(sprites)) {
            s.drawOn(g);
        }
    }

    public void notifyAllTimePassed() {
        for (Sprite s : new ArrayList<>(sprites)) {
            s.timePassed();
        }
    }

    public List<Sprite> getSprites() { return sprites; }
}
