package no.uib.inf101.sem2.model.danmakus;

import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public final class Player extends Sprite<SpriteType, SpriteState>{
  
  private String Variation;
  private Matricies transform = new Matricies();
  private static final Vector standStill = new Vector(0, 0, 1);
  private int Lives; // default 3
  private double Power; // dmg multiplier from 0 to 5
  
  private Player(String playerVar, int Radius, Vector Position, Vector Velocity) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, null, Velocity);
    // constants equal for all players.
    this.Lives = 3;  
    this.Power = 1.0;
  }
  
  /**
  * newPlayer is a method that contains a list of valid playable characters.
  * playable: Circular hitbox: "P1c" and "P2c", rectangular hitbox: "P1r" and "P2r".
  * 
  */
  static Player newPlayer(String newPlayerType) {
    Player playableC = switch(newPlayerType) {
      case "P1c" -> new Player(newPlayerType, 8, new Vector(-8, -8, 1), standStill); // want center at (0, 0)
      case "P2c" -> new Player(newPlayerType, 10, new Vector(-10, -10, 1), standStill);
      default -> throw new IllegalArgumentException("Type '" + newPlayerType + "' does not match one of two playable characters");
    };
    return playableC;
    
  }
  
  /**
  * getter
  */
  public int getRadius() {
    return this.Radius();
  }
  
  /**
  * getter
  */
  public String getVariation() {
    return this.Variation;
  }
  
  /**
  * getter
  */
  public Vector getPosition() {
    return this.Position();
  }

  /**
   * displaceBy moves the player in a direction vector where
   * the scalar either represents distance (when used with {@link #shiftedToStartPoint}) 
   * or speed (when called by movePlayer method from model).  
   */
  public Player displaceBy(Vector direction, double scalar) {
    // math: position += direction * speed
    // note: changing position over time can be done by
    // multiplying with a scalar T, where T is time elapsed.                                                                                           
    
    direction = direction.normaliseVect();
    Vector newPosition = direction.multiplyScalar(scalar);
    this.transform.Translate(newPosition); // get translation
    Vector[] Translate = this.transform.getTransform();
    Vector displacedPosition = this.Position().transformVect(Translate);

    Player displacedPlayer = new Player(this.Variation, this.Radius(), displacedPosition, this.Velocity());
    return displacedPlayer;
  }
  
  /**
  * sets player spawn on field
  * 
  */
  public Player shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (Math.round(0.8*dimension.height()) + dimension.getFieldY());

    Vector direction = new Vector(startX, startY, 1);
    direction = direction.normaliseVect();
    double distance = Math.sqrt(startX*startX + startY*startY);

    return displaceBy(direction, distance);
  }
  

  
  
  // remake hashcode and equals later.
  
  
  
  
}
