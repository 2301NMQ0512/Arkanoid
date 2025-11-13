package sounds; // (Bạn có thể đặt trong package 'sounds' hoặc 'utils')

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lớp này quản lý việc tải, phát một lần và phát lặp lại các tệp .wav.
 */
public class AudioPlayer {

    // Map để theo dõi các clip đang lặp (nhạc nền)
    private Map<String, Clip> loopingClips;

    public AudioPlayer() {
        this.loopingClips = new ConcurrentHashMap<>();
    }

    /**
     * Tải một tệp âm thanh từ thư mục resources.
     * @param audioPath Đường dẫn trong thư mục resources (ví dụ: "resources/sounds/hit.wav")
     * @return một đối tượng Clip đã sẵn sàng để phát.
     */
    private Clip loadClip(String audioPath) {
        try {
            InputStream audioSrc = Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(audioPath)
            );
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            return audioClip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
            System.err.println("Không thể tải tệp âm thanh: " + audioPath);
            return null;
        }
    }

    /**
     * Phát một hiệu ứng âm thanh một lần (ví dụ: va chạm, nhấp chuột).
     * @param audioPath Đường dẫn đến tệp .wav (ví dụ: "resources/sounds/hit.wav")
     */
    public void play(String audioPath) {
        Clip audioClip = loadClip(audioPath);
        if (audioClip != null) {
            // Tự động đóng clip khi nó phát xong để giải phóng tài nguyên
            audioClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                }
            });
            audioClip.start(); // Phát âm thanh
        }
    }

    /**
     * Phát một tệp âm thanh lặp đi lặp lại (ví dụ: nhạc nền).
     * @param audioPath Đường dẫn đến tệp .wav (ví dụ: "resources/sounds/music.wav")
     */
    public void loop(String audioPath) {
        // Nếu nhạc này đã phát, đừng làm gì cả
        if (this.loopingClips.containsKey(audioPath)) {
            return;
        }

        Clip audioClip = loadClip(audioPath);
        if (audioClip != null) {
            this.loopingClips.put(audioPath, audioClip); // Lưu lại để có thể dừng sau
            audioClip.loop(Clip.LOOP_CONTINUOUSLY); // Lặp lại mãi mãi
        }
    }

    /**
     * Dừng một âm thanh đang lặp.
     * @param audioPath Đường dẫn đến tệp .wav đã được bắt đầu bằng loop()
     */
    public void stop(String audioPath) {
        Clip audioClip = this.loopingClips.remove(audioPath); // Lấy và xóa khỏi map
        if (audioClip != null) {
            audioClip.stop(); // Dừng phát
            audioClip.close(); // Giải phóng tài nguyên
        }
    }
}