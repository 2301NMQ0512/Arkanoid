package levels;

import ball.Velocity;
import collidable.Block;
import game.Sprite;

import java.util.List;

/**
 * Implements the interface and sets up a new level based on given settings.
 */
public class LevelDtl implements LevelInformation {
    private String levelName;
    private List<Velocity> ballVelocities;
    private Sprite background;
    private int paddleSpeed;
    private int paddleWidth;
    private List<Block> blocks;
    private int numBlocks;

    @Override
    public int numberOfBalls() {
        return ballVelocities == null ? 0 : ballVelocities.size();
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        return ballVelocities;
    }

    @Override
    public int paddleSpeed() {
        return paddleSpeed;
    }

    @Override
    public int paddleWidth() {
        return paddleWidth;
    }

    @Override
    public String levelName() {
        return levelName;
    }

    @Override
    public Sprite getBackground() {
        return background;
    }

    @Override
    public List<Block> blocks() {
        return blocks;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return numBlocks;
    }

    /**
     * Sets background.
     *
     * @param bg the bg
     */
    public void setBackground(Sprite bg) {
        background = bg;
    }

    /**
     * Sets ball velocities.
     *
     * @param velocities the velocities
     */
    public void setBallVelocities(List<Velocity> velocities) {
        ballVelocities = velocities;
    }

    /**
     * Sets level name.
     *
     * @param name the name
     */
    public void setLevelName(String name) {
        levelName = name;
    }

    /**
     * Sets paddle speed.
     *
     * @param speed the speed
     */
    public void setPaddleSpeed(int speed) {
        paddleSpeed = speed;
    }

    /**
     * Sets paddle width.
     *
     * @param width the width
     */
    public void setPaddleWidth(int width) {
        paddleWidth = width;
    }

    /**
     * Sets num blocks.
     *
     * @param numberOfBlocks the number of blocks
     */
    public void setNumBlocks(int numberOfBlocks) {
        numBlocks = numberOfBlocks;
    }

    /**
     * Sets block list.
     *
     * @param blocksList the given blocks
     */
    public void setBlockList(List<Block> blocksList) {
        this.blocks = blocksList;
    }

//    public String toString() {
//        return String.format("Level: %s , Paddle: [speed = %d , width = %d], VelocityListSize: %d, NumOfBlocks: %d",
//                levelName,
//                paddleSpeed,
//                paddleWidth,
//                initialBallVelocities().size(),
//                numBlocks);
//    }
}