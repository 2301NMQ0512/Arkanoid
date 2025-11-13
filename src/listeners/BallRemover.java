package listeners;

import game.Counter;
import collidable.Block;
import ball.Ball;
import game.GameLevel;


public class BallRemover implements HitListener {
    private final GameLevel game;
    private final Counter remainingBalls;


    public BallRemover(GameLevel game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }


    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(game);
        remainingBalls.decrease(1);
    }
}