package sounds; // (Bạn nên đặt tệp này trong package 'sounds')

// --- SỬA LỖI: THÊM CÁC IMPORT BỊ THIẾU ---
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
// ------------------------------------

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Một lớp đơn giản để tải trước và phát một tệp âm thanh (ví dụ: .wav).
 */
public class SoundPlayer {
    private Clip clip;

    /**
     * Tải một tệp âm thanh từ thư mục resources.
     * @param soundFilePath Đường dẫn bên trong thư mục resources (ví dụ: "sounds/hit.wav")
     */
    public SoundPlayer(String soundFilePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(soundFilePath);
             AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(is))) {

            if (is == null) {
                throw new IOException("Không tìm thấy tệp âm thanh: " + soundFilePath);
            }

            this.clip = AudioSystem.getClip();
            this.clip.open(audioIn);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
            System.err.println("Không thể tải tệp âm thanh: " + soundFilePath + " Lỗi: " + e.getMessage());
            this.clip = null;
        }
    }

    /**
     * Phát âm thanh từ đầu.
     */
    public void play() {
        if (this.clip != null) {
            if (this.clip.isRunning()) {
                this.clip.stop(); // Dừng nếu đang phát
            }
            this.clip.setFramePosition(0); // Tua lại từ đầu
            this.clip.start(); // Phát
        }
    }
}