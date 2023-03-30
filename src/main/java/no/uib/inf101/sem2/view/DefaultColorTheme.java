package no.uib.inf101.sem2.view;

import java.awt.Color;

public class DefaultColorTheme implements ColorTheme {

    @Override
    public Color getBackgroundColor() {
        return null;
    }

    @Override
    public Color getFieldColor() {
        return Color.BLACK;
    }

    @Override
    public Color getSpriteColor(Character C) {
        Color color = switch(C) {
            case 'r' -> Color.RED;
            case 'b' -> Color.BLUE;
            case 'g' -> Color.GREEN;
            default -> throw new IllegalArgumentException("no available color '" + C + "'");
        };
        return color;
    }
    



    
}
