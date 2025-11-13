package levels;

import collidable.Block;

import java.util.Map;

/**
 * The type Blocks from symbols factory.
 *
 * 
 */
public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths;
    private Map<String, BlockCreator> blockCreators;

    /**
     * Instantiates a new Blocks from symbols factory.
     *
     * @param space  the space
     * @param blocks the blocks
     */
    public BlocksFromSymbolsFactory(Map<String, Integer> space, Map<String, BlockCreator> blocks) {
        this.spacerWidths = space;
        this.blockCreators = blocks;
    }

    /**
     * Is space symbol boolean.
     *
     * @param s the s
     * @return the boolean
     */
// returns true if 's' is a valid space symbol.
    public boolean isSpaceSymbol(String s) {
        return spacerWidths.get(s) != null;
    }

    /**
     * Is block symbol boolean.
     *
     * @param s the s
     * @return the boolean
     */
// returns true if 's' is a valid block symbol.
    public boolean isBlockSymbol(String s) {
        return blockCreators.get(s) != null;
    }

    /**
     * Gets block.
     *
     * @param s    the s
     * @param xpos the xpos
     * @param ypos the ypos
     * @return the block
     */
// Return a block according to the definitions associated
    // with symbol s. The block will be located at position (xpos, ypos).
    public Block getBlock(String s, int xpos, int ypos) {
        return this.blockCreators.get(s).create(xpos, ypos);
    }

    /**
     * Gets space width.
     *
     * @param s the s
     * @return the space width
     */
// Returns the width in pixels associated with the given spacer-symbol.
    public int getSpaceWidth(String s) {
        return this.spacerWidths.get(s);

    }

    /**
     * Gets block creators.
     *
     * @return the block creators
     */
    public Map<String, BlockCreator> getBlockCreators() {
        return blockCreators;
    }

    /**
     * Gets spacer widths.
     *
     * @return the spacer widths
     */
    public Map<String, Integer> getSpacerWidths() {
        return spacerWidths;
    }

}

