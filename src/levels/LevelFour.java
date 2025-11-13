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
import java.util.Random;


/**
 * Level Four. Will have a different amount of blocks and balls than the other levels.
 * Also, the paddle's speed, width and the balls' velocity will be different as well from the
 * other levels.
 *
 * 
 */
public class LevelFour implements LevelInformation {
    /**
     * the number of the created balls in this level.
     *
     * @return int the number of the balls.
     */
    @Override
    public int numberOfBalls() {
        return 4;
    }

    /**
     * creates a list of velocities.
     * Add a velocity by angle and speed, by a given speed and a random angle for each ball.
     * The size of the list will be as the number of the balls.
     *
     * @return velocities list.
     */
    @Override
    public List<Velocity> initialBallVelocities() {
        Random rand = new Random();
        List<Velocity> velocities = new ArrayList<>();
        for (int i = 0; i < numberOfBalls(); i++) {
            velocities.add(Velocity.fromAngleAndSpeed(rand.nextInt((360 - 270) + 1) + 270, 7));
        }
        return velocities;
    }

    /**
     * sets the speed of the paddle, how fast can he move.
     *
     * @return int, the speed.
     */
    @Override
    public int paddleSpeed() {
        return 20;
    }

    /**
     * sets the width of the paddle.
     *
     * @return int, the width
     */
    @Override
    public int paddleWidth() {
        return 77;
    }

    /**
     * the name of the level.
     *
     * @return string
     */
    @Override
    public String levelName() {
        return "Final Hour";
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
                Color skyblue = new Color(31, 200, 226);

                d.setColor(skyblue);
                d.fillRectangle(0, 0, (int) GameLevel.getGuiWidth(), (int) GameLevel.getGuiHeight());

                d.setColor(new Color(60, 60, 0));
                d.fillRectangle(115, 505, 20, 100);

                d.setColor(Color.green);
                d.fillOval(75, 450, 100, 100);
                d.setColor(Color.black);
                d.drawOval(75, 450, 100, 100);

                d.setColor(Color.red);
                d.fillOval(92, 515, 20, 20);
                d.fillOval(111, 456, 20, 20);
                d.fillOval(92, 474, 20, 20);
                d.fillOval(110, 480, 20, 20);
                d.fillOval(130, 505, 20, 20);
                d.fillOval(142, 470, 20, 20);
                d.setColor(new Color(82, 60, 0));
                d.drawLine(82, 515, 85, 495);
                d.drawLine(121, 456, 124, 436);
                d.drawLine(102, 474, 105, 454);

                d.setColor(Color.red);
                d.fillOval(50, -250, 700, 400);
                d.setColor(Color.orange);
                d.fillOval(55, -255, 690, 390);
                d.setColor(Color.yellow);
                d.fillOval(60, -260, 680, 380);
                d.setColor(Color.green);
                d.fillOval(65, -265, 670, 370);
                d.setColor(Color.cyan);
                d.fillOval(70, -270, 660, 360);
                d.setColor(Color.blue);
                d.fillOval(75, -275, 650, 350);
                d.setColor(Color.magenta);
                d.fillOval(80, -280, 640, 340);
                d.setColor(skyblue);
                d.fillOval(85, -285, 630, 330);
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
     * Creates 7 lines of blocks with a different color for each line
     * and a same number of blocks in each one.
     * Adds each created block to a list of blocks and returns the list.
     *
     * @return blocks
     */
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        Color[] randomColor = new Color[7];
        randomColor[0] = Color.darkGray.brighter();
        randomColor[1] = Color.red;
        randomColor[2] = Color.yellow;
        randomColor[3] = Color.green;
        randomColor[4] = Color.white;
        randomColor[5] = Color.pink;
        randomColor[6] = Color.cyan;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 15; j++) {
                Rectangle rectangle = new Rectangle(new Point(52 * (j + 1) - 44,
                        GameLevel.getGuiHeight() * 0.30 + (30 * (i - 1))), 53,
                        30);
                Block block = new Block(rectangle, randomColor[i]);
                blocks.add(block);
                block.colorForStroke(Color.black);
            }
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
