package game;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Sprite collection.
 *
 * 
 */
public class SpriteCollection {
    private java.util.List<Sprite> spriteCollection;

    /**
     * Instantiates a new Sprite collection.
     */
    SpriteCollection() {
        this.spriteCollection = new ArrayList<Sprite>();
    }


    /**
     * Add a certain sprite the the collection.
     *
     * @param s the given sprite
     */
    public void addSprite(Sprite s) {
        spriteCollection.add(s);
    }

    /**
     * Remove a certain sprite from the collection.
     *
     * @param s the given sprite
     */
    public void removeSprite(Sprite s) {
        spriteCollection.remove(s);
    }

    /**
     * Notify all time passed.
     * call timePassed() on all sprites.
     */

    public void notifyAllTimePassed() {
        //different method I want to keep just in case for the next assignment.
//        Collection<mainGame.Sprite> sprites = new ArrayList<mainGame.Sprite>(this.spriteCollection);
//        for (int i = 0; i < sprites.size(); i++) {
//            ((ArrayList<mainGame.Sprite>) sprites).get(i).timePassed();
//        }

        List<Sprite> sprites = new ArrayList<Sprite>(this.spriteCollection);
        // Notify all listeners about a hit event:
        for (Sprite sprite : sprites) {
            sprite.timePassed();
        }

    }

    /**
     * call drawOn(d) on all sprites.
     *
     * @param d the drawing surface
     */
    public void drawAllOn(DrawSurface d) {
        for (int i = 0; i < spriteCollection.size(); i++) {
            spriteCollection.get(i).drawOn(d);
        }
    }
}