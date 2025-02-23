package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Consumables;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public interface ControllableDanmakuModel {
    
  /**
   * movePlayer moves the player by dx and dy, given by the velocity Vector.
   * @param velocity is velocity vector the player moves by.
   * @return true if successful movement, false otherwise.
   */
  boolean movePlayer(Vector velocity);

  /**
   * playerFire spawns a new bullet(s) belonging to player. 
   * Bullets spawn position and how many is spawned at once varies depending on player variation.
   * Bullets spawn formation can either be spread shot (default) and focused shot (higher bullet density)
   * which the player can switch between by holding the shift-key.
   * fireRate controlls how often bullets gets fired when holding down z-key
   * @param fireRate controlls how often player shoots. Higher values makes player shoot fewer bullets
   * per second.
   * @param holdingShift changes bullet formation from spread shot (false) to focused shot (true).
   */
  void playerFire(int fireRate, boolean holdingShift);

  /**
   * enemyFire makes all enemies on field shoot bullets. bullet pattern, velocity, firerate and spawn position 
   * depends is controlled by several helper methods, which depends on enemy variation.
   */
  void enemyFire();

  /** moveAllBullets goes through the list of bullets on field and moves each according to their velocity vector */
  void moveAllBullets();

  /**
   * getter for all spawned bullets, still existing on field.
   * @return list of bullets;
   */
  Iterable<Bullets> getBulletsOnField();

  /**
   * moveEnemiesInWaves spawns a set number of enemies per wave per stage and gives them a custom path to move in. 
   * Movement for a wave is considered finished when all enemies has moved out of screen (wether or not player kills 
   * them doesn't affect completion time). time interval per wave is fixed, but short enough for each wave to overlap, 
   * making it necessary for the player to eliminate the enemies
   * as fast as possible before more spawns. 
   */
  void moveEnemiesInWaves();

  /**
   * moveAllCollectibles moves spawned consumable-objects in 2 set patterns. 
   * All collectibles moves down towards the bottom of the field by default.
   * if player is one human length away from a collectible, then move that item towards player.
   * if player is above line of collection, move all collectibles on field towards player.
   */
  void moveAllCollectibles();

  /**
   * set FPS value. Used with a fps calculator.
   * updates every second.
   */
  void setFPSValue(double newFPS);

  /** @return current Game State */
  GameState getGameState();

  /** @param newState is new Game Status */
  void setGameState(GameState newState);

  /** resetField resets all sprite-objects on field and statistics to start value */
  void resetField();

  /** @return current stage (int) */
  int getCurrentStage();

  /** @return current boss on field. is null when outside of boss battle. */
  Enemies getBossEnemyOnField();

  /** @return all consumable items on field  */
  Iterable<Consumables> getCollectiblesOnField();

  /** Select which player to use (used in character selection screen) */
  void SelectPlayer(SpriteVariations variation);

}
