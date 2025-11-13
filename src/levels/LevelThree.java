package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import game.GameLevel;
import game.Sprite;
import geometry.Rectangle;
import geometry.Point;

import java.awt.Polygon;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Level Three. Will have a different amount of blocks and balls than the other levels.
 * Also, the paddle's speed, width and the balls' velocity will be different as well from the
 * other levels.
 *
 * 
 */
public class LevelThree implements LevelInformation {
    /**
     * the number of the created balls in this level.
     *
     * @return
     */
    @Override
    public int numberOfBalls() {
        return 2;
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
        List<Velocity> velocities = new ArrayList<Velocity>();
        for (int i = 0; i < numberOfBalls(); i++) {
            velocities.add(Velocity.fromAngleAndSpeed(rand.nextInt((360 - 270) + 1) + 270, 6));
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
        return 13;
    }

    /**
     * sets the width of the paddle.
     *
     * @return int, the width
     */
    @Override
    public int paddleWidth() {
        return 80;
    }

    /**
     * the name of the level.
     *
     * @return string
     */
    @Override
    public String levelName() {
        return "Green 3";
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
                d.setColor(new Color(22, 111, 22));
                d.fillRectangle(0, 0, (int) GameLevel.getGuiWidth(), (int) GameLevel.getGuiHeight());

                d.setColor(Color.YELLOW);
                d.fillOval(510, 100, 200, 200);


                d.setColor(Color.BLACK);
                d.fillOval(555, 155, 30, 30);
                d.fillOval(635, 155, 30, 30);


                d.fillOval(550, 210, 120, 60);


                d.setColor(Color.YELLOW);
                d.fillRectangle(550, 210, 120, 30);
                d.fillOval(550, 220, 120, 40);

                Polygon poly = new Polygon();
                poly.addPoint(50, 90);
                poly.addPoint(150, 50);
                poly.addPoint(250, 90);
                d.setColor(new Color(218, 165, 32));
                d.fillPolygon(poly);

                d.setColor(Color.black);
                d.drawLine(50, 90, 150, 50);
                d.drawLine(150, 50, 250, 90);
                d.setColor(Color.yellow);
                d.fillRectangle(50, 90, 200, 100);
                d.setColor(Color.black);
                d.drawRectangle(50, 90, 200, 100);

                d.setColor(Color.black);
                d.fillRectangle(75, 110, 30, 25);
                d.fillRectangle(190, 110, 30, 25);
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
     * Creates 5 lines of blocks with a different color for each line
     * and a different number of blocks in each one.
     * Adds each created block to a list of blocks and returns the list.
     *
     * @return blocks
     */
    @Override
    public List<Block> blocks() {
        List<Block> blocks = new ArrayList<>();
        Color[] randomColor = new Color[5];
        randomColor[0] = Color.darkGray.brighter();
        randomColor[1] = Color.red;
        randomColor[2] = Color.yellow;
        randomColor[3] = Color.blue;
        randomColor[4] = Color.white;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10 - i; j++) {
                Rectangle rectangle = new Rectangle(new Point(50 * (j + 1) - 44,
                        GameLevel.getGuiHeight() * 0.30 + (30 * (i - 1))), 50,
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
