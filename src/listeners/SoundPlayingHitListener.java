package listeners;

import ball.Ball;
import collidable.Block;
import sounds.SoundPlayer; // Import lớp SoundPlayer mới

/**
 * Một HitListener phát ra âm thanh khi một khối bị đánh.
 */
public class SoundPlayingHitListener implements HitListener {

    // Đường dẫn đến tệp âm thanh trong thư mục resources
    private static final String HIT_SOUND_PATH = "resources/sounds/hit.wav";

    private SoundPlayer soundPlayer;

    public SoundPlayingHitListener() {
        // Tải âm thanh CHỈ MỘT LẦN khi listener được tạo
        this.soundPlayer = new SoundPlayer(HIT_SOUND_PATH);
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // Phát âm thanh
        this.soundPlayer.play();
    }
}