package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import game.GameLevel;
import game.Sprite;
import geometry.Rectangle;
import geometry.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * Level one. Will have a different amount of blocks and balls than the other levels.
 * Also, the paddle's speed, width and the balls' velocity will be different as well from the
 * other levels.
 *
 * 
 */
public class LevelOne implements LevelInformation {

    /**
     * the number of the created balls in this level.
     * @return int the number of the bals
     */
    @Override
    public int numberOfBalls() {
        return 1;
    }


    /**
     * makes the ball to fly directly to the single block and destroy it.
     */
    @Override

    public List<Velocity> initialBallVelocities() {
        List<Velocity> veloList = new ArrayList<>();
        veloList.add(new Velocity(0, -6));
        return veloList;
    }

    /**
     * the speed of the paddle, how fast can he move.
     * @return int, the speed.
     */
    @Override
    public int paddleSpeed() {
        return 8;
    }
    /**
     * sets the width of the paddle.
     * @return int, the width
     */
    @Override
    public int paddleWidth() {
        return 110;
    }

    /**
     * the name of the level.
     * @return string
     */
    @Override
    public String levelName() {
        return "Direct Hit";
    }

    @Override
    public Sprite getBackground() {
        return new Sprite() {

            /**
             * draws the background on the screen.
             *
             * @param d the drawing surface
             */
            @Override
            public void drawOn(DrawSurface d) {
                d.setColor(Color.black);
                d.fillRectangle(0, 0, (int) GameLevel.getGuiWidth(), (int) GameLevel.getGuiHeight());
                d.setColor(Color.blue);
                d.drawCircle((int) GameLevel.getGuiWidth() / 2, 140, 100);
                d.drawCircle((int) GameLevel.getGuiWidth() / 2, 140, 80);
                d.drawCircle((int) GameLevel.getGuiWidth() / 2, 140, 60);
                d.drawLine(280, 135, 380, 135);
                d.drawLine(420, 135, 520, 135);
                d.drawLine((int) GameLevel.getGuiWidth() / 2, 40, (int) GameLevel.getGuiWidth() / 2, 126);
                d.drawLine((int) GameLevel.getGuiWidth() / 2, 150, (int) GameLevel.getGuiWidth() / 2, 250);
                d.setColor(new Color(220, 205, 45));
            }

            /**
             * notify the sprite that time has passed.
             */
            @Override
            public void timePassed() {
//
            }
        };
    }

    /**
     * creates a single block.
     *
     * @return the block inside of a list.
     */
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        Rectangle rectangle = new Rectangle(new Point(380,
                120), 40,
                40);
        Block block = new Block(rectangle, Color.red.darker());
        blocks.add(block);
        block.colorForStroke(Color.black);
        return blocks;
    }

    /**
     * the amount of the blocks in the beginning of the level.
     * @return int, the number of the blocks
     */
    @Override
    public int numberOfBlocksToRemove() {
        return blocks().size();
    }

}
