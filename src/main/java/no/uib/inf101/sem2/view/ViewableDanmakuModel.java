package no.uib.inf101.sem2.view;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;

public interface ViewableDanmakuModel {
  
  
  /**
  * getDimension returns an FieldDimension object.
  * @return FieldDimensioon of a model.
  */
  FieldDimension getDimension();
  
  /**
  * 
  * 
  * @return playable character
  */
  Player getPlayer();

  /**
  * 
  * 
  * @return next enemy
  */
  Enemies getEnemy();

  

  /**
   * getter for FPS value calculated in controller.
   * 
   */
  double getFPSValue();
}
