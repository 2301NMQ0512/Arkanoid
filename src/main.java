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




void main(String[] args) {

    GUI gui = new GUI("Arkanoid", 800, 600);
    KeyboardSensor keyboard = gui.getKeyboardSensor();


    MouseSensor mouse = new MouseSensor(gui, keyboard);

    AnimationRunner runner = new AnimationRunner(gui);


    List<LevelInformation> levels = createLevelList(args);
    if (levels.isEmpty()) {
        IO.println("Không tìm thấy level hợp lệ. Tải các level mặc định.");
        levels.add(new LevelOne());
        levels.add(new LevelTwo());
        levels.add(new LevelThree());
        levels.add(new LevelFour());
    }


    try {

        GameFlow game = new GameFlow(runner, keyboard, mouse, gui, levels);
        game.runGame();
    } catch (Exception e) {
        System.err.println("Không thể bắt đầu trò chơi:");
        e.printStackTrace();
        gui.close();
    }
}


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
