package listeners;

import ball.Ball;
import collidable.Block;
import sounds.AudioPlayer; // Import lớp AudioPlayer mới


public class SoundPlayingHitListener implements HitListener {

    private final AudioPlayer audioPlayer;
    private final String soundPath;

    public SoundPlayingHitListener(AudioPlayer player, String soundPath) {
        this.audioPlayer = player;
        this.soundPath = soundPath;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {

        this.audioPlayer.play(this.soundPath);
    }
}