package levels;

import collidable.Block;

/**
 * The interface Block creator.
 */
public interface BlockCreator {

    /**
     * Create block.
     *
     * @param xPos the x position of the block.
     * @param yPos the y pososition of the block.
     * @return a block at the specified location.
     */
    Block create(int xPos, int yPos);

    /**
     * Gets block width.
     *
     * @return the block width
     */
    int getBlockWidth();
}
