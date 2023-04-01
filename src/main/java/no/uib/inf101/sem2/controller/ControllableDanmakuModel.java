package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;

public interface ControllableDanmakuModel {
    
  /**
   * movePlayer
   * 
   */
  boolean movePlayer(Vector direction, double speed);
}
