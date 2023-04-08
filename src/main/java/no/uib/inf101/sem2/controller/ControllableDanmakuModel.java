package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;

public interface ControllableDanmakuModel {
    
  /**
   * movePlayer moves the player by dx and dy, given by the velocity Vector
   * 
   */
  boolean movePlayer(Vector velocity, double dt);

  /**
   * playerFire spawns a new bullet(s) belonging to player. 
   * Bullets spawn position and how many is spawned at once varies depending on player variation.
   * Bullets spawn formation can either be spread shot (default) and focused shot (higher bullet density)
   * which the player can switch between by holding shift-key.
   * fireRate controlls how often bullets gets fired when holding down z-key
   */
  void playerFire(int fireRate, boolean holdingShift);

  /**
   * moveAllBullets goes through the list of bullets on field and moves each according to their velocity vector.
   * 
   */
  void moveAllBullets();

  /**
   * 
   * @return list of bullets existing on field
   */
  Iterable<Bullets> getBulletsOnField();

  /**
   * rotateAxisEnemy rotates the enemy by angle theta, given by it's aim diretion Vector
   * 
   */
  boolean rotateAxisEnemy(double theta);

  /**
   * moveEnemiesInWaves spawns a set number of enemies (not total) per wave and gives them a custom path to move in. 
   * Method runs in conjunction with (insert stage setter method), where each stage has around 4 to 5 waves of enemies spawning. 
   * Movement for a wave is considered finished when all enemies has moved out of screen (wether or not player kills them doesn't affect completion time).
   * time interval per wave is fixed, but short enough for each wave to overlap, making it necessary for the player to eliminate the enemies
   * as fast as possible before more spawns. 
   * For sake of simplicity we will give each enemy variation a set movement path.
   */
  void moveEnemiesInWaves();

  /**
   * set FPS value.
   * updates every 2 seconds
   * 
   */
  void setFPSValue(double newFPS);
}
