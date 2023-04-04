package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.Vector;

public class Enemies extends Sprite<SpriteType, SpriteState>{

  private static final Vector standStill = new Vector(0, 0, 1);
  private static final Vector startingAim = new Vector(0, 1, 1);
  private int healthPoints;
  private int healthBars;

  public Enemies(String EnemyVar, int healthPoints, int healthBars, int Radius, Vector Position) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    this.healthPoints = healthPoints;
    this.healthBars = healthBars;
  }
  
  /**
  * newPlayer is a method that contains a list of valid playable characters.
  * playable: Circular hitbox: "P1c" and "P2c", rectangular hitbox: "P1r" and "P2r".
  * 
  */
  static Enemies newEnemy(String newEnemyVar) {
    Enemies enemy = switch(newEnemyVar) {
      case "monster1" -> new Enemies(newEnemyVar, 300, 1, 8, 
      new Vector(-8, -8, 1)); // want center at (0, 0)
      case "monster2" -> new Enemies(newEnemyVar, 500, 1, 10, 
      new Vector(-10, -10, 1));
      default -> throw new IllegalArgumentException("Type '" + newEnemyVar + "' does not match one of two playable characters");
    };
    return enemy;
    
  }

  
}
