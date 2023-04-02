package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;

public interface ControllableDanmakuModel {
    
  /**
   * movePlayer
   * 
   */
  boolean movePlayer(Vector direction, double speed);

  /**
   * set FPS value.
   * updates every 2 seconds
   * 
   */
  void setFPSValue(double newFPS);
}
