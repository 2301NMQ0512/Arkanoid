package listeners;

import game.Counter;
import collidable.Block;
import ball.Ball;
import game.GameLevel;

/**
 * a Block Remover is in charge of removing blocks from the game, as well as keeping count
 * of the number of blocks that remain.
 *
 * 
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;

    /**
     * Instantiates a new Block remover.
     *
     * @param game          the game
     * @param removedBlocks the removed blocks
     */
    public BlockRemover(GameLevel game, Counter removedBlocks) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
    }

    /**
     * Blocks that are hit are being removed
     * from the game.
     *
     * @param beingHit the block that being hit and removed from the game.
     * @param hitter   the hitter is the Ball that's doing the hitting.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(game);
        remainingBlocks.decrease(1);
    }
}