package listeners;

import game.Counter;
import collidable.Block;
import ball.Ball;
import game.GameLevel;


public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;


    public BlockRemover(GameLevel game, Counter removedBlocks) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
    }


    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(game);
        remainingBlocks.decrease(1);
    }
}