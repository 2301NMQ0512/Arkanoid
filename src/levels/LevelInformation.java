package levels;

import ball.Velocity;
import collidable.Block;
import game.Sprite;

import java.util.List;

/**
 * The LevelInformation interface specifies the information required to fully describe a level.
 *
 *
 */
public interface LevelInformation {
    /**
     * sets the needed number of balls in the level.
     *
     * @return the int
     */
    int numberOfBalls();

    /**
     * Initial ball velocities list.
     *
     * @return the list
     */

    List<Velocity> initialBallVelocities();

    /**
     * sets Paddle's speed.
     *
     * @return int, the paddle's speed.
     */
    int paddleSpeed();

    /**
     * sets paddle's  width.
     *
     * @return int, the width.
     */
    int paddleWidth();

    /**
     * Sets the name of the level.
     *
     * @return string, the name.
     */

    String levelName();

    /**
     * Draws background for the level.
     *
     * @return sprite, the background
     */

    Sprite getBackground();

    /**
     * Creates the blocks and adds them to a list.
     *
     * @return the list
     */

    List<Block> blocks();

    /**
     * Number of blocks to remove, the number of blocks in the beginning of a level.
     *
     * @return int, the number.
     */

    int numberOfBlocksToRemove();

    String getBackgroundMusicPath();

    String getBallImagePath();
}
