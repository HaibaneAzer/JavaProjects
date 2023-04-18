package no.uib.inf101.sem2.model.danmakus;

import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public final class Player extends Sprite<SpriteType, SpriteState>{
  
  private Vector accel;
  private Matricies Matrix = new Matricies();
  private static final Vector standStill = new Vector(0, 0, 1);
  private static final Vector startingAim = new Vector(0, -10, 1);
  private int Lives = 3; // default 3
  private double Power = 1.0; // dmg multiplier from 0 to 5
  
  /**
   * transforming constructor
   */
  private Player(String playerVar, int Radius, Vector Position, Vector Direction, Vector Velocity) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, Direction, Velocity);
  }

  /**
   * spawning constructor
   */
  private Player(String playerVar, int Radius, Vector Position) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    // default spawn variables for all players.
  }

  /**
  * newPlayer is a method that contains a list of valid playable characters.
  * playable: Circular hitbox: "P1c" and "P2c".
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
   * getter for lives
   * 
   */
  public int getLives() {
    return this.Lives;
  }

  /**
   * getter for power
   * 
   */
  public double getPower() {
    return this.Power;
  }

  /**
   * isAlive checks if player has lives left.
   */
  public boolean isAlive() {
    return this.Lives > 0;
  }

  /**
   * killPlayer removes lifes from player when getting hit. if player has no lives left, stop respawning.
   */
  public void killPlayer() {
    this.Lives--;
  }

  /**
   * update velocity using constant acceleration. Delta is acceleration factor.
   * dt >= 1 where value 1 means the acceleration is unit length. 
   * Acceleration stops when oldVel = newVel.
   */
  public void accelerate(Vector targetVel, double dt) {
    boolean TargetlessThanCur = 
    this.Velocity.length() >= targetVel.length();

    this.accel = targetVel.subVect(this.Velocity).normaliseVect().multiplyScalar(1 / dt);
    if (!TargetlessThanCur) {
      this.Velocity = this.Velocity.addVect(accel);
    }

    System.out.println(this.Velocity);
    
  }

  /**
   * displaceBy moves the player position vector by velocity vector, where
   * the scalar either represents distance (when used with {@link #shiftedToStartPoint}) 
   * or speed. 
   */
  public Player displaceBy(Vector Velocity) {
    // Math: Matrix((1, 0, 0), (0, 1, 0), delta) x Position = Position + delta, 
    // where Position and delta are Vectors
    Vector[] translate = Matrix.TranslationMatrix(Velocity); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Player displacedPlayer = new Player(this.Variation, this.Radius, displacedPosition, this.Direction, this.Velocity);
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
  
  
  
}
