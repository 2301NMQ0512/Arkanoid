package levels;

import collidable.Block;
import geometry.Point;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Blocks definition reader.
 */
public class BlocksDefinitionReader implements BlockCreator {

    private static final String DEFAULT = "default";
    private static final String BDEF = "bdef";
    private static final String COMMENT = "#";
    private static final String SDEF = "sdef";
    private static final String BDEF_SYMBOL = "bdef symbol:";
    private static final String FILL = "fill";
    private int blockWidth;
    private int blockHeight;
    private Color stroke;
    private Color[] colorsToDraw;
    private String[] imagesPath;
    private static final String HEIGHT = "height";
    private static final String WIDTH = "width";
    private static final String STROKE = "stroke";
    private static final String IMAGE = "image";

    /**
     * Instantiates a new Blocks definition reader.
     */
    public BlocksDefinitionReader() {

    }

    /**
     * Define block.
     *
     * @param line        the line
     * @param defaultLine the default line
     */
    public void defineBlock(String line, String defaultLine) {
        String[] defaultData = defaultLine.split(" ");
        defaultLine = defaultLine.replaceAll("default ", "");
        if (line.startsWith(BDEF_SYMBOL)) {
            line = line.replaceAll("bdef symbol:", "");
        }
        read(defaultData);
        read(line.split(" "));
        if (this.blockWidth < 0 || this.blockHeight < 0) {
            throw new RuntimeException("Invalid inputs");
        }
    }

    /**
     * Reads each line and checks the settings for each block.
     *
     * @param data the setting
     */
    private void read(String[] data) {
        boolean firStFill = true;
        for (String s : data) {
            int index;
            s = s.trim();
            if (s.startsWith(HEIGHT)) {
                index = s.indexOf("height:");
                String temp = "height:";
                this.blockHeight = Integer.parseInt(s.substring(index + temp.length()));
                if (this.blockHeight < 0) {
                    throw new RuntimeException("Invalid inputs");
                }
            } else if (s.startsWith(WIDTH)) {
                index = s.indexOf("width:");
                String temp = "width:";
                this.blockWidth = Integer.parseInt(s.substring(index + temp.length()));
                if (this.blockWidth < 0) {
                    throw new RuntimeException("Invalid inputs");
                }
            } else if (s.startsWith(STROKE)) {
                String temp = "stroke:color";
                String color = s.replaceAll(temp, "");
                this.stroke = new ColorsParser().colorFromString(color);
            } else if (s.startsWith(FILL)) {
                if (s.contains("color")) {
                    if (firStFill) {
                        this.colorsToDraw = new Color[1];
                        firStFill = false;
                    }
                    if (s.indexOf("-") > 0) {
                        char c = s.charAt(s.indexOf("-") + 1);
                        String s1 = Character.toString(c);
                        int fillNum = Integer.parseInt(s1);
                        this.colorsToDraw[fillNum - 1] = new ColorsParser().colorFromString(
                                s.substring(s.indexOf("(")));
                    } else {
                        Color c = new ColorsParser().colorFromString(s.substring(s.indexOf("(")));
                        for (int i = 0; i < this.colorsToDraw.length; i++) {
                            if (this.colorsToDraw[i] == null) {
                                this.colorsToDraw[i] = c;
                            }
                        }
                    }
                } else if (s.contains(IMAGE)) {
                    if (firStFill) {
                        this.imagesPath = new String[1];
                        firStFill = false;
                    }
                    if (s.indexOf("-") > 0) {
                        char c = s.charAt(s.indexOf("-") + 1);
                        String s1 = Character.toString(c);
                        int fillNum = Integer.parseInt(s1);
                        this.imagesPath[fillNum - 1] = s.substring(s.indexOf("("));
                    } else {
                        String s1 = s.substring(s.indexOf("("));
                        for (int i = 0; i < this.imagesPath.length; i++) {
                            if (this.imagesPath[i] == null) {
                                this.imagesPath[i] = s1;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * From reader blocks from symbols factory.
     *
     * @param reader the reader
     * @return the blocks from symbols factory
     */
    public static BlocksFromSymbolsFactory fromReader(java.io.Reader reader) {
        Map<String, Integer> spacerWidths = new HashMap<>();
        Map<String, BlockCreator> blockCreators = new HashMap<>();
        BlocksFromSymbolsFactory b = new BlocksFromSymbolsFactory(spacerWidths, blockCreators);
        BufferedReader bf = new BufferedReader(reader);
        try {
            String line = bf.readLine();
            String defaultData = new String();
            while (line != null) {
                BlocksDefinitionReader blockFactory = new BlocksDefinitionReader();
                if (line.startsWith(COMMENT) || line.length() == 0) {
                    line = bf.readLine();
                    continue;
                } else if (line.startsWith(BDEF)) {
                    int symbolIndex = line.indexOf(":") + 1;
                    char c = line.charAt(symbolIndex);
                    String s = Character.toString(c);
                    blockFactory.defineBlock(line, defaultData);
                    blockCreators.put(s, blockFactory);
                } else if (line.startsWith(DEFAULT)) {
                    defaultData = line;
                } else if (line.startsWith(SDEF)) {
                    String copyLine = line.replaceAll("sdef symbol:", "").trim();
                    char c = copyLine.charAt(0);
                    int index = copyLine.trim().indexOf(":");
                    Integer value = Integer.parseInt(copyLine.substring(index + 1));
                    spacerWidths.put(Character.toString(c), value);
                }
                line = bf.readLine();
            }
        } catch (IOException e) {
            System.out.println("Failed reading a line");
        } finally {
            try {
                bf.close();
            } catch (IOException e) {
                System.out.println("failed closing reader");
            }
        }
        return b;
    }

    /**
     * Make block list blocks from symbols factory.
     *
     * @param reader the reader
     * @return the blocks from symbols factory
     */
    public BlocksFromSymbolsFactory makeListOfBlocks(java.io.Reader reader) {
        return fromReader(reader);
    }

    /**
     * Create block.
     *
     * @param xPos the x position of the block.
     * @param yPos the y pososition of the block.
     * @return a block at the specified location.
     */
    @Override
    public Block create(int xPos, int yPos) {
        Block b = new Block(new Point(xPos, yPos), blockWidth, blockHeight);
        b.colorForStroke(stroke);
        if (colorsToDraw != null && this.colorsToDraw.length > 0) {
            b.setColor(colorsToDraw[0]);
        } else if (imagesPath != null && imagesPath.length > 0) {
            b.loadImagesToBlock(imagesPath);
            b.setImages(imagesPath);
        }
        return b;
    }

    @Override
    public int getBlockWidth() {
        return this.blockWidth;
    }
}