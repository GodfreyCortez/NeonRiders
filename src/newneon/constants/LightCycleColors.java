package newneon.constants;

import java.util.HashMap;
import java.awt.Color;

// This class holds the colors of the various light cycles
// Modifications to this data structure are not permitted as the colours
// are meant to be constant
public class LightCycleColors {

    private static final HashMap<ColorScheme, Color> colorMap = new HashMap<ColorScheme, Color>();

    LightCycleColors() {
        colorMap.put(ColorScheme.TEAL, new Color(41, 171, 226));
        colorMap.put(ColorScheme.ORANGE, new Color(255, 105, 39));
        colorMap.put(ColorScheme.NEON_GREEN, new Color(140, 198, 63));
        colorMap.put(ColorScheme.PINK, new Color(255, 151, 163));
        colorMap.put(ColorScheme.RED, new Color(212, 20, 90));
        colorMap.put(ColorScheme.PURPLE, new Color(147, 39, 143));
    }

    public Color getColor(ColorScheme colorName){
        return colorMap.get(colorName);
    }
}
