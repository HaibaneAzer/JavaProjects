package no.uib.inf101.sem2.view;

import java.awt.Color;

public interface ColorTheme {
  
  
  /**
  * 
  * 
  */
  Color getBackgroundColor();
  
  /**
  * 
  * 
  */
  Color getFieldColor();
  
  /**
  * getSpriteColor gives a set color for a spesific sprite. Available colors are:
  * 'r' = red, 'b' = blue, 'g' = green, 'p' = pink, 'c' = cyan and 'y' = yellow.
  * 
  */
  Color getSpriteColor(Character C);
  
}
