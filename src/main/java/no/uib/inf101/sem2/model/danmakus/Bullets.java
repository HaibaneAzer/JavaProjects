package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.Vector;

public class Bullets extends Sprite<SpriteType, SpriteState>{


  public Bullets(String bulletVar, SpriteState State, int Radius, Vector Position, Vector bulletAim, Vector Velocity) {
    super(SpriteType.Bullet, bulletVar, State, Radius, Position, bulletAim, Velocity);
    
    
  }

  
    
}
