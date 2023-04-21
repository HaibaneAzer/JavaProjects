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
      case 'p' -> Color.PINK;
      case 'c' -> Color.CYAN;
      case 'y' -> Color.YELLOW;
      default -> throw new IllegalArgumentException("no available color '" + C + "'");
    };
    return color;
  }

  @Override
  public Color getScoreBoardColor(String C) {
    Color color = switch (C) {
      case "back" -> Color.DARK_GRAY;
      case "score" -> Color.WHITE;
      default -> throw new IllegalArgumentException("No available color for '" + C + "'\nTry 'back' or 'score'.");
    };
    return color;
  }

  @Override
  public Color getGameOverColor(String C) {
    Color color = switch(C) {
      case "back" -> new Color(0, 0, 0, 192);
      case "gameover" -> Color.decode("#4F0001").brighter().brighter().brighter();
      case "key" -> Color.decode("#4F0001").brighter().brighter().brighter();
      default -> throw new IllegalArgumentException("No available color for '" + C + "'\nTry 'back', 'gameover' or 'key'.");
    };
    return color;
  }

  @Override
  public Color getMenuScreenColor(String C) {
    Color color = switch(C) {
      case "back" -> Color.BLACK.brighter();
      case "title" -> Color.WHITE;
      case "key" -> Color.WHITE.darker();
      default -> throw new IllegalArgumentException("No available color for '" + C + "'\nTry 'back', 'title' or 'key'.");
    };
    return color;
  }

  @Override
  public Color getPauseScreenColor(String C) {
    Color color = switch(C) {
      case "back" -> new Color(0, 0, 0, 192);
      case "paused" -> Color.WHITE.darker();
      case "key" -> Color.WHITE.darker();
      default -> throw new IllegalArgumentException("No available color for '" + C + "'\nTry 'back', 'paused' or 'key'.");
    };
    return color;
  }

  @Override
  public Color getStatisticsColor(String C) {
    Color color = switch(C) {
      case "curscore" -> Color.GRAY.darker().darker();
      case "hiscore" -> Color.GRAY;
      case "stage" -> Color.GRAY;
      default -> throw new IllegalArgumentException("No available color for '" + C + "'\nTry 'curscore', 'hiscore' or 'stage'.");
    };
    return color;
  }

  @Override
  public Color getFrameColor() {
    return Color.BLACK;
  }

  @Override
  public Color getFieldBackgroundColor() {
    return Color.GREEN.brighter();
  }
  
  
  
  
  
}
