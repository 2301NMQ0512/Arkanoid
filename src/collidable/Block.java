package collidable;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;
import java.awt.Color;

import ball.Ball;
import listeners.HitListener;
import ball.Velocity;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import game.GameLevel;
import listeners.HitNotifier;
import game.Sprite;

import javax.imageio.ImageIO;


public class Block implements Collidable, Sprite, HitNotifier {
    private List<HitListener> hitListeners;
    private Color color;
    private Rectangle rectangle;
    private Color colorStroke;
    private String[] images;
    private Image[] loadedImages;
    private Color[] colors;


    public Block(Rectangle rectangle, Color color) {
        this.color = color;
        this.rectangle = rectangle;
        hitListeners = new ArrayList<>();
    }


    public Block(Point leftUpperCorner, int width, int height) {
        this.rectangle = new Rectangle(leftUpperCorner, width, height);
        hitListeners = new ArrayList<>();
    }


    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }


    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }


    public void removeFromSprites(GameLevel game) {
        game.removeSprite(this);
    }


    public Rectangle getCollisionRectangle() {

        return this.rectangle;
    }


    public void colorForStroke(Color c) {
        this.colorStroke = c;
    }



    public void setColors(Color[] changingBlockColorFill) {
        this.colors[0] = changingBlockColorFill[0];
    }


    public void setColor(Color c) {
        this.color = c;
    }


    public void setImages(String[] s) {
        this.images = s;
    }


    public void loadImagesToBlock(String[] paths) {
        try {
            List<Image> imageList = new ArrayList<>();
            for (int i = 0; i < paths.length; i++) {
                Image image;
                trimStrings(paths);
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(paths[i]);
                image = ImageIO.read(is);
//                image = ImageIO.read(new File("resources/" + paths[i]));
                imageList.add(image);
            }
            this.loadedImages = new Image[imageList.size()];
            this.loadedImages = imageList.toArray(this.loadedImages);

        } catch (IOException e) {
            System.out.println("failed loading image while drawing blocks");
        }
    }


    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }


    private void trimStrings(String[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replaceAll("\\(", "");
            s[i] = s[i].replaceAll("\\)", "");
        }
    }




    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        Velocity newVelocity = currentVelocity;

        // starting point - according to the upper left point of the rectangle
        double startX = rectangle.getUpperLeft().getX();
        double startY = rectangle.getUpperLeft().getY();

        double x = collisionPoint.getX();
        double y = collisionPoint.getY();

        // Y direction
        if ((x > startX) && (x < startX + rectangle.getWidth())) {
            newVelocity.setDy((currentVelocity.getDy()) * (-1));
        }
        // X direction
        if ((y > startY) && (y < startY + rectangle.getHeight())) {
            newVelocity.setDx(currentVelocity.getDx() * (-1));
        }
        // hits the corners
//        else {
//            newVelocity.setDx(currentVelocity.getDx() * (-1));
//            newVelocity.setDy(currentVelocity.getDy() * (-1));
//        }

        this.notifyHit(hitter);
        return newVelocity;

    }


    public void drawOn(DrawSurface d) {
        if (this.images != null && this.images.length > 0) {
            drawOnImage(d); // one image
        } else {
            d.setColor(color);
            d.drawRectangle((int) rectangle.getUpperLeft().getX(),
                    (int) rectangle.getUpperLeft().getY(),
                    (int) rectangle.getWidth(), (int) rectangle.getHeight());
            d.fillRectangle((int) rectangle.getUpperLeft().getX(),
                    (int) rectangle.getUpperLeft().getY(),
                    (int) rectangle.getWidth(), (int) rectangle.getHeight());
            if (this.colorStroke != null) {
                d.setColor(this.colorStroke);
            } else {
                d.setColor((color));
            }
            d.drawRectangle((int) rectangle.getUpperLeft().getX(),
                    (int) rectangle.getUpperLeft().getY(),
                    (int) rectangle.getWidth(), (int) rectangle.getHeight());
        }
    }

    /**
     * nothing meanwhile.
     */
    public void timePassed() {
//nothing (as you asked).
    }


    private void drawOnImage(DrawSurface d) {
        int index = 0;
        Point p = rectangle.getUpperLeft();
        d.drawImage((int) p.getX(), (int) p.getY(), this.loadedImages[index]);
        if (this.colorStroke != null) {
            int width = (int) this.rectangle.getWidth();
            int height = (int) this.rectangle.getHeight();
            d.drawRectangle((int) p.getX(), (int) p.getY(), width, height);
        }

    }


    public void addHitListener(HitListener hl) {
        hitListeners.add(hl);
    }


    public void removeHitListener(HitListener hl) {
        hitListeners.remove(hl);
    }
}
