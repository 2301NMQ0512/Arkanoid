package game;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;


public class SpriteCollection {
    private final java.util.List<Sprite> spriteCollection;


    SpriteCollection() {
        this.spriteCollection = new ArrayList<>();
    }


    public void addSprite(Sprite s) {
        spriteCollection.add(s);
    }

    public void removeSprite(Sprite s) {
        spriteCollection.remove(s);
    }

    public void notifyAllTimePassed() {

        List<Sprite> sprites = new ArrayList<>(this.spriteCollection);
        for (Sprite sprite : sprites) {
            sprite.timePassed();
        }

    }

    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : spriteCollection) {
            sprite.drawOn(d);
        }
    }
}