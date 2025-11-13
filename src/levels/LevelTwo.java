package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import game.Sprite;
import geometry.Rectangle;
import geometry.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Level Two. Will have a different amount of blocks and balls than the other levels.
 * Also, the paddle's speed, width and the balls' velocity will be different as well from the
 * other levels.
 *
 * 
 */

public class LevelTwo implements LevelInformation {

    /**
     * the number of the created balls in this level.
     *
     * @return int the number of the balls
     */
    @Override
    public int numberOfBalls() {
        return 10;
    }

    /**
     * creates a list of velocities.
     * Add a velocity by angle and speed, by a given speed and a specific angle, so the balls
     * will fly in an arc structure.
     * The size of the list will be as the number of the balls.
     *
     * @return velocities list.
     */
    @Override
    public List<Velocity> initialBallVelocities() {
        Random rand = new Random();
        List<Velocity> velocities = new ArrayList<>();
        for (int i = 0; i < numberOfBalls(); i++) {
            velocities.add(Velocity.fromAngleAndSpeed((i * -6) + 30, 5));
        }
        return velocities;
    }

    /**
     * the speed of the paddle, how fast can he move.
     *
     * @return int, the speed.
     */
    @Override
    public int paddleSpeed() {
        return 4;
    }

    /**
     * sets the width of the paddle.
     *
     * @return int, the width
     */
    @Override
    public int paddleWidth() {
        return 340;
    }

    /**
     * the name of the level.
     *
     * @return string
     */
    @Override
    public String levelName() {
        return "Wide Easy";
    }

    @Override
    public Sprite getBackground() {
        return new Sprite() {

            /**
             * draws the sprite to the screen.
             *
             * @param d the drawing surface
             */
            @Override
            public void drawOn(DrawSurface d) {
                d.setColor(Color.orange);
                d.drawLine(130, 150, 411, 300);
                d.drawLine(140, 150, 511, 300);
                d.drawLine(100, 150, 611, 300);
                d.drawLine(100, 150, 311, 300);
                d.drawLine(100, 150, 711, 300);
                d.setColor(Color.getHSBColor(344, 174, 11));
                d.fillCircle(120, 130, 70);
                d.setColor(Color.orange);
                d.fillCircle(120, 130, 60);
                d.setColor(Color.yellow);
                d.fillCircle(120, 130, 50);
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
     * Creates 1 line of blocks with a different color.
     * Adds each created block to a list of blocks and returns the list.
     *
     * @return blocks
     */
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        Color blockColor = Color.red;
        for (int i = 0; i < 15; i++) {
            if (i == 2) {
                blockColor = Color.yellow;
            }
            if (i == 4) {
                blockColor = Color.orange;
            }
            if (i == 6) {
                blockColor = Color.green;
            }
            if (i == 9) {
                blockColor = Color.blue;
            }
            if (i == 11) {
                blockColor = Color.pink;
            }
            if (i == 13) {
                blockColor = Color.cyan;
            }
            Rectangle rectangle = new Rectangle(new Point(52 * (i + 1) - 40,
                    300), 51,
                    30);
            Block block = new Block(rectangle, blockColor);
            blocks.add(block);
            block.colorForStroke(Color.black);
        }
        return blocks;
    }

    /**
     * the amount of the blocks in the beginning of the level.
     *
     * @return int, the number of the blocks
     */
    @Override
    public int numberOfBlocksToRemove() {
        return blocks().size();
    }

}
