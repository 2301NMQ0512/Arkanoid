package levels;

import java.awt.Color;
import java.lang.reflect.Field;

/**
 * This class define an object that converts a string to a Color object.
 */
public class ColorsParser {


    /**
     * parse color definition and return the specified color.
     *
     * @param s is a string represent a color.
     * @return a color according to s
     */
    public java.awt.Color colorFromString(String s) {
        s = s.replaceAll("color", "");
        if (!s.contains("RGB")) {
            String temp = s;
            temp = temp.replaceAll("\\(", "");
            temp = temp.replaceAll("\\)", "");
            Color color;
            try {
                Field field = Class.forName("java.awt.Color").getField(temp);
                color = (Color) field.get(null);
            } catch (Exception e) {
                color = null; // Undefined
            }
            return color;
        } else { // in case the color is given by RGB.
            String copyString = s.replaceAll("RGB", "");
            copyString = copyString.replaceAll("\\(", "");
            copyString = copyString.replaceAll("\\)", "");
            String[] rgbInString = copyString.split(",");
            int[] rgbNum = new int[rgbInString.length];
            for (int i = 0; i < rgbNum.length; i++) {
                rgbNum[i] = Integer.parseInt(rgbInString[i]);
            }
            return new Color(rgbNum[0], rgbNum[1], rgbNum[2]);
        }

    }
}
