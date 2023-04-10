package no.uib.inf101.sem2.view;

import java.awt.Color;

public interface ColorTheme {
  
  
  /**
  * getBackgroundColor gets colors used for background behind the field. 
  * NB: Change later to images. 
  * 
  */
  Color getBackgroundColor();
  
  /**
  * getFieldColor gets colors used for Field.
  * 
  */
  Color getFieldColor();
  
  /**
  * getSpriteColor gives a set color for a spesific sprite. Available colors are:
  * 'r' = red, 'b' = blue, 'g' = green, 'p' = pink, 'c' = cyan and 'y' = yellow.
  * @param C is of type char
  */
  Color getSpriteColor(Character C);

  /**
  * getScoreBoardColor has parameter of type string and returns values of type Color.
  * valid inputs are "back"; corrosponds to color dark gray, and "score"; corrosponds to
  * color white.
  * @param C is of type string
  */
  Color getScoreBoardColor(String C);

  /**
  * getGameOverColor gives colors used for game over screen texts.
  * @param C can be 'back', 'gameover' or 'key'.
  */
  Color getGameOverColor(String C);

  /**
  * getMenuScreenColor gives colors used for menu screen texts.
  * @param C can be 'back', 'title' or 'key'.
  */
  Color getMenuScreenColor(String C);

  /**
  * getPauseScreenColor gives colors used for pause screen texts.
  * @param C can be 'back', 'paused' or 'key'.
  */
  Color getPauseScreenColor(String C);

  /**
   * getStatisticsColor gives colors used to text descibing statistics.
   * @param C can be 'curscore', 'hiscore' or 'stage'.
   */
  Color getStatisticsColor(String C);
  
}
