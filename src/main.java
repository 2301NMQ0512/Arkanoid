import animation.AnimationRunner;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import game.GameFlow;
import levels.LevelFour;
import levels.LevelInformation;
import levels.LevelOne;
import levels.LevelThree;
import levels.LevelTwo;
import menu.MouseSensor;

// (Import các lớp Level của bạn ở đây)

/**
 * Phương thức main, điểm bắt đầu của chương trình.
 * (SỬA LỖI: Đã thêm 'public' vào 'public static void main')
 */
void main(String[] args) {
    // --- 1. Khởi tạo biuoop ---
    GUI gui = new GUI("Arkanoid", 800, 600);
    KeyboardSensor keyboard = gui.getKeyboardSensor();

    // Khởi tạo MouseSensor (nó sử dụng Reflection để tự đính vào 'drawingPanel')
    MouseSensor mouse = new MouseSensor(gui, keyboard);

    AnimationRunner runner = new AnimationRunner(gui);

    // --- 2. Tạo danh sách level ---
    List<LevelInformation> levels = createLevelList(args);
    if (levels.isEmpty()) {
        IO.println("Không tìm thấy level hợp lệ. Tải các level mặc định.");
        levels.add(new LevelOne());
        levels.add(new LevelTwo());
        levels.add(new LevelThree());
        levels.add(new LevelFour());
    }

    // --- 3. Khởi tạo và chạy GameFlow ---
    try {
        // Truyền 'mouse' (mà bạn vừa tạo) vào GameFlow
        GameFlow game = new GameFlow(runner, keyboard, mouse, gui, levels);
        game.runGame(); // Bắt đầu vòng lặp game
    } catch (Exception e) {
        System.err.println("Không thể bắt đầu trò chơi:");
        e.printStackTrace();
        gui.close();
    }
}

/**
 * Phân tích các đối số (args) từ dòng lệnh để tạo danh sách level.
 */
private static List<LevelInformation> createLevelList(String[] args) {
    List<LevelInformation> levels = new ArrayList<>();
    for (String arg : args) {
        try {
            int levelNum = Integer.parseInt(arg);
            switch (levelNum) {
                case 1:
                    levels.add(new LevelOne());
                    break;
                case 2:
                    levels.add(new LevelTwo());
                    break;
                case 3:
                    levels.add(new LevelThree());
                    break;
                case 4:
                    levels.add(new LevelFour());
                    break;
                default:
                    IO.println("Bỏ qua level không hợp lệ: " + arg);
                    break;
            }
        } catch (NumberFormatException e) {
            IO.println("Bỏ qua đối số không hợp lệ: " + arg);
        }
    }
    return levels;
}
