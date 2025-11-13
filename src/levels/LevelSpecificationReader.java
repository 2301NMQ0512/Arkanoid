package levels;

import ball.Velocity;
import biuoop.DrawSurface;
import collidable.Block;
import game.Sprite;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Level specification reader.
 */
public class LevelSpecificationReader {
    private static final String COMMENT = "#";
    private static final String SPACE = " ";
    private static final String BACKGROUND = "background:";
    private static final String BALL_VELOCITIES = "ball_velocities:";
    private static final String END_BLOCKS = "END_BLOCKS";
    private static final String LEVEL_NAME = "level_name:";
    private static final String PADDLE_SPEED = "paddle_speed:";
    private static final String PADDLE_WIDTH = "paddle_width:";
    private static final String START_BLOCKS = "START_BLOCKS";
    private static final String START_LEVEL = "START_LEVEL";
    private static final String BLOCK_DEFINITIONS = "block_definitions:";
    private static final String NUM_BLOCKS = "num_blocks:";
    private static final String BLOCK_START_X = "blocks_start_x:";
    private static final String BLOCK_START_Y = "blocks_start_y:";
    private static final String ROW_HEIGHT = "row_height:";
    private int rowHeight;
    private int blockStartX;
    private int blockStartY;
    private String path;


//    private static void setBackground(LevelDtl level, String data) throws IOException {
//        Objects.requireNonNull(level, "Null level.");
//        if (data != null && !data.isEmpty()) {
//            // image(background_images/night.jpg)
//            if (data.startsWith(IMAGE)) {
//                String path = data.substring(IMAGE.length(), data.length() - 1);
//                BufferedImage image = ImageIO.read(new File(path));
//                level.setBackground(new Sprite() {
//                    /**
//                     * draws the sprite to the screen.
//                     *
//                     * @param d the drawing surface
//                     */
//                    @Override
//                    public void drawOn(DrawSurface d) {
//                        d.drawImage(200,200, image);
//                    }
//
//                    /**
//                     * notify the sprite that time has passed.
//                     */
//                    @Override
//                    public void timePassed() {
//
//                    }
//                });
//            }
//        }
//    }

    /**
     * gets the background info and parses it into a background whether it's an image or a given color.
     *
     * @param line the line
     * @return the background
     */
    private Sprite makeSpriteToBackGround(String line) {
        if (line.contains("color")) {
            ColorsParser colorsParser = new ColorsParser();
            Color color = colorsParser.colorFromString(line);
            return new Sprite() {
                @Override
                public void drawOn(DrawSurface d) {
                    d.setColor(color);
                    d.fillRectangle(0, 0, 800, 600);
                }

                @Override
                public void timePassed() {
                }
            };
        } else { // in case line contains a p   ath to an image for the background
            int index = line.indexOf("(");
            String filePath = line.substring(index);
            filePath = filePath.replaceAll("\\(", "");
            filePath = filePath.replaceAll("\\)", "");

            return new BackGroundFromImage(filePath);
        }
    }

    /**
     * parses the velocities and sets a list up and sends it to the level configuration.
     *
     * @param level the level
     * @param data  the line
     */
    private static void setInitialBallVelocities(LevelDtl level, String data) {
        Objects.requireNonNull(level, "Null level.");
        if (data != null && !data.isEmpty()) {
            String[] velocityPairs = data.split("\\s+");
            if (velocityPairs.length > 0) {
                List<Velocity> velocities = new ArrayList<>();
                for (String velocityPair : velocityPairs) {
                    String[] numbers = velocityPair.split(",");
                    // parse the results to ints
                    int angle = Integer.parseInt(numbers[0]);
                    int speed = Integer.parseInt(numbers[1]);
                    velocities.add(Velocity.fromAngleAndSpeed(angle, speed));
                }
                level.setBallVelocities(velocities);
            }
        }
    }

    /**
     * parses the paddle's speed and sets a list up and sends it to the level configuration.
     *
     * @param level the level
     * @param data  the line
     */

    private static void setPaddleSpeed(LevelDtl level, String data) {
        Objects.requireNonNull(level, "Null level.");
        if (data != null && !data.isEmpty()) {
            int speed;
            try {
                speed = Integer.parseInt(data);
            } catch (NumberFormatException xNumberFormat) {
                speed = 0;
            }
            level.setPaddleSpeed(speed);
        }
    }

    /**
     * parses the number of blocks and sets a list up and sends it to the level configuration.
     *
     * @param level the level
     * @param data  the line
     */
    private static void setNumOfBlocks(LevelDtl level, String data) {
        Objects.requireNonNull(level, "Null level.");
        if (data != null && !data.isEmpty()) {
            int numbOfBlocks;
            try {
                numbOfBlocks = Integer.parseInt(data);
            } catch (NumberFormatException xNumberFormat) {
                numbOfBlocks = 0;
            }
            level.setNumBlocks(numbOfBlocks);
        }
    }

    /**
     * parses the paddle's width and sets a list up and sends it to the level configuration.
     *
     * @param level the level
     * @param data  the line
     */
    private static void setPaddleWidth(LevelDtl level, String data) {
        Objects.requireNonNull(level, "Null level.");
        if (data != null && !data.isEmpty()) {
            int width;
            try {
                width = Integer.parseInt(data);
            } catch (NumberFormatException xNumberFormat) {
                width = 0;
            }
            level.setPaddleWidth(width);
        }
    }


    /**
     * Makes the level, parses the given text file, reading through each line and
     * sets up the level.
     *
     * @param filePath the file path
     * @return a list of created levels.
     */
    public List<LevelInformation> makeLevel(String filePath) {
        List<LevelInformation> levels = new ArrayList<>();
        try (FileReader fr = new FileReader(filePath);
             BufferedReader br = new BufferedReader(fr)) {
            LevelDtl level = null;
            String line = br.readLine();
            while (line != null) {
                if (line.contains(START_LEVEL)) {
                    // End current level.
                    if (level != null) {
                        levels.add(level);
                    }
                    // Start next level.
                    level = new LevelDtl();
                } else if (line.length() == 0 || line.startsWith(COMMENT) || line.startsWith(SPACE)) {
                    line = br.readLine();
                    continue;
                } else if (line.startsWith(LEVEL_NAME)) {
                    assert level != null;
                    level.setLevelName(line.substring(LEVEL_NAME.length()));
                } else if (line.startsWith(BALL_VELOCITIES)) {
                    setInitialBallVelocities(level, line.substring(BALL_VELOCITIES.length()));
                } else if (line.startsWith(BACKGROUND)) {
                    level.setBackground(makeSpriteToBackGround(line.substring(BACKGROUND.length())));
                } else if (line.startsWith(PADDLE_SPEED)) {
                    setPaddleSpeed(level, line.substring(PADDLE_SPEED.length()));
                } else if (line.startsWith(PADDLE_WIDTH)) {
                    setPaddleWidth(level, line.substring(PADDLE_WIDTH.length()));
                } else if (line.startsWith(NUM_BLOCKS)) {
                    setNumOfBlocks(level, line.substring(NUM_BLOCKS.length()));
                } else if (line.startsWith(BLOCK_START_X)) {
                    this.blockStartX = (Integer.parseInt(line.substring(BLOCK_START_X.length())));
                } else if (line.startsWith(BLOCK_START_Y)) {
                    this.blockStartY = (Integer.parseInt(line.substring(BLOCK_START_Y.length())));
                } else if (line.startsWith(ROW_HEIGHT)) {
                    this.rowHeight = (Integer.parseInt(line.substring(ROW_HEIGHT.length())));
                } else if (line.startsWith(BLOCK_DEFINITIONS)) {
                    //BlocksDefinitionReader.fromReader(new FileReader(line.substring(BLOCK_DEFINITIONS.length())));
                    path = line.substring(BLOCK_DEFINITIONS.length());
                } else if (line.startsWith(START_BLOCKS)) {
                    try {
                        List<Block> blockList = new ArrayList<>();
//                        InputStream is =
//                                ClassLoader.getSystemClassLoader().getResourceAsStream(path);
//                        java.io.Reader brr = new BufferedReader(new InputStreamReader(is));
////                        path = "./resources/" + path;
                        FileReader ffr = new FileReader(path);
                        BufferedReader brr = new BufferedReader(ffr);
                        BlocksFromSymbolsFactory b = new BlocksDefinitionReader().makeListOfBlocks(brr);
                        Map<String, Integer> spacers = b.getSpacerWidths();
                        Map<String, BlockCreator> blockCreatorMap = b.getBlockCreators();
                        line = br.readLine();
                        while (line != null && !line.equals(END_BLOCKS)) {
                            if (line.equals("")) {
                                line = br.readLine();
                                continue;
                            }
                            String[] symbols = line.split("");
                            boolean first = true;
                            int saveX = this.blockStartX;
                            for (String s : symbols) {
                                if (b.isBlockSymbol(s)) {
                                    if (first) {
                                        blockList.add(blockCreatorMap.get(s)
                                                .create(this.blockStartX, this.blockStartY));
                                        first = false;
                                    } else {
                                        int width = blockCreatorMap.get(s).getBlockWidth();
                                        this.blockStartX = this.blockStartX + width;
                                        blockList.add(blockCreatorMap.get(s).create(
                                                this.blockStartX, this.blockStartY));
                                    }

                                } else if (b.isSpaceSymbol(s)) {
                                    this.blockStartX = this.blockStartX + spacers.get(s);
                                }
                            }
                            this.blockStartX = saveX;
                            this.blockStartY = this.blockStartY + this.rowHeight;

                            line = br.readLine();
                        }
                        level.setBlockList(blockList);
                    } catch (NullPointerException e) {
                        System.out.println("null");
                    }
                }
                line = br.readLine();
            }
            if (level != null) {
                levels.add(level);
            }
        } catch (IOException xIo) {
            xIo.printStackTrace();
        }
        return levels;
    }
}