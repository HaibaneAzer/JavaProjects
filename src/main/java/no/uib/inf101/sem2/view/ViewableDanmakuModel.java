package no.uib.inf101.sem2.view;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;

public interface ViewableDanmakuModel {
  
  /**
  * getDimension is getter for FieldDimension object. 
  * Gets field where sprites can move and shoot in.
  * @return FieldDimension of a model.
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

  /** Getter for all boss enemy-objects on field */
  Enemies getBossEnemyOnField();

  /** Getter for boss attack type */
  boolean getBossAttackType();

  /**
   * Getter for all bullets-objects on field.
   * @return list of bullets
   */
  Iterable<Bullets> getBulletsOnField();

  /** Getter for stages */
  int getCurrentStage();

  /** Getter for gameState */
  GameState getGameState();

  /** Getter for FPS value calculated in controller */
  double getFPSValue();
}
