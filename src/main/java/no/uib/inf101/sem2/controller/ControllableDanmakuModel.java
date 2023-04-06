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
  void playerFire(int fireRate);

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
   * reset velocity in either x or y direction. 
   * @param horisontal is direction to reset
   */
  void resetVelocity(boolean horisontal);


  /**
   * set FPS value.
   * updates every 2 seconds
   * 
   */
  void setFPSValue(double newFPS);
}
