package game;

import biuoop.DrawSurface;


public interface Sprite {

    void drawOn(DrawSurface d);


    void timePassed();
}