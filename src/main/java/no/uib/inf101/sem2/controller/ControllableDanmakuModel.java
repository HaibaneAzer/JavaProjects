package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;

public interface ControllableDanmakuModel {
    
  /**
   * movePlayer moves the player by dx and dy, given by the velocity Vector
   * 
   */
  boolean movePlayer(Vector velocity);

  /**
   * set FPS value.
   * updates every 2 seconds
   * 
   */
  void setFPSValue(double newFPS);
}
