package no.uib.inf101.sem2.view;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;

public interface ViewableDanmakuModel {
  
  /**
  * getDimension returns an FieldDimension object.
  * @return FieldDimensioon of a model.
  */
  FieldDimension getDimension();
  
  /**
  * getter for all player-objects on field
  * @return playable character
  */
  Player getPlayer();

  /**
  * getter for all enemy objects on field
  * @return next enemy
  */
  Iterable<Enemies> getEnemiesOnField();

  /**
   * getter for all boss enemy-objects on field
   */
  Enemies getBossEnemyOnField();


  /**
   * 
   * @return list of bullets existing on field
   */
  Iterable<Bullets> getBulletsOnField();

  /**
   * getter for stages
   */
  int getCurrentStage();

  /**
   * getter for gameState
   */
  GameState getGameState();

  /**
   * getter for FPS value calculated in controller.
   * 
   */
  double getFPSValue();
}
