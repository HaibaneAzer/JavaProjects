package no.uib.inf101.sem2.model.danmakus;

import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public final class Player extends Sprite<SpriteType, SpriteState>{
  
  private String Variation;
  private Matricies Matrix = new Matricies();
  private static final Vector standStill = new Vector(0, 0, 1);
  private int Lives; // default 3
  private double Power; // dmg multiplier from 0 to 5
  
  private Player(String playerVar, int Radius, Vector Position) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, null, standStill);
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
      case "P1c" -> new Player(newPlayerType, 8, new Vector(-8, -8, 1)); // want center at (0, 0)
      case "P2c" -> new Player(newPlayerType, 10, new Vector(-10, -10, 1));
      default -> throw new IllegalArgumentException("Type '" + newPlayerType + "' does not match one of two playable characters");
    };
    return playableC;
    
  }
  
  /**
   * getter radius
   */
  public int getRadius() {
    return this.Radius();
  }
  
  /**
  * getter variation
  */
  public String getVariation() {
    return this.Variation;
  }
  
  /**
  * getter position
  */
  public Vector getPosition() {
    return this.Position();
  }

  /**
   * getter velocity
   */
  public Vector getVelocity() {
    return this.Velocity();
  }

  /**
   * update velocity by acceleration
   */
  public Vector accelerate() {
    
    return null; 
  }

  /**
   * displaceBy moves the player position vector by direction vector, where
   * the scalar either represents distance (when used with {@link #shiftedToStartPoint}) 
   * or speed. 
   */
  public Player displaceBy(Vector Velocity) {

    double scalar = Velocity.length();                                                                                          
    Velocity = Velocity.normaliseVect();
    Vector[] scale = Matrix.ScaleMatrix(scalar);
    Vector displacement = Velocity.transformVect(scale); // either distance to spawn or velocity vector 
    Vector[] translate = Matrix.TranslationMatrix(displacement); // get translation matrix
    // Math: Matrix((1, 0, 0), (0, 1, 0), delta) x Position = Position + delta, 
    // where Position and delta are Vectors
    Vector displacedPosition = this.Position().transformVect(translate); // displace position

    Player displacedPlayer = new Player(this.Variation, this.Radius(), displacedPosition);
    return displacedPlayer;
  }
  
  /**
  * sets player spawn on field
  * 
  */
  public Player shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (Math.round(0.8*dimension.height()) + dimension.getFieldY());

    Vector originToSpawn = new Vector(startX, startY, 1);

    return displaceBy(originToSpawn);
  }
  

  
  
  // remake hashcode and equals later.
  
  
  
  
}
