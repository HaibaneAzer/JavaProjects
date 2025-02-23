package no.uib.inf101.sem2.model.danmakus;

import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public final class Player extends Sprite<SpriteType, SpriteState>{
  
  private Matricies Matrix = new Matricies();
  private static final Vector standStill = new Vector(0, 0, 1);
  private static final Vector startingAim = new Vector(0, -10, 1);
  private int Lives = 3; // default 3
  private double Power = 1.00; // dmg multiplier from 0 to 5
  
  /**
   * transforming constructor
   */
  private Player(SpriteVariations playerVar, int Radius, Vector Position, Vector Direction, Vector Velocity, int Lives, double Power) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, Direction, Velocity);
    this.Lives = Lives;
    this.Power = Power;
  }

  /**
   * spawning constructor
   */
  private Player(SpriteVariations playerVar, int Radius, Vector Position) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    // default spawn variables for all players.
  }

  /**
   * respawning constructor
   */
  private Player(SpriteVariations playerVar, int Radius, Vector Position, int Lives, double Power) {
    // spriteType is unchangeable, SpriteState can change.
    super(SpriteType.Player, playerVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    // default spawn variables for all players.
    this.Lives = Lives;
    this.Power = Power;
  }

  /**
  * newPlayer is a method that contains a list of valid playable characters.
  * playable: Circular hitbox: "P1c" and "P2c".
  */
  static Player newPlayer(SpriteVariations newPlayerType) {

    Player playableC = switch(newPlayerType) {
      case player1 -> new Player(newPlayerType, 9, new Vector(-9, -9, 1)); // want center at (0, 0)
      case player2 -> new Player(newPlayerType, 9, new Vector(-9, -9, 1));
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

  /** increase Power */
  public void addPower(double powerIncrease) {
    this.Power += powerIncrease;
    if (this.Power > 5.0) {
      this.Power = 5.0;
    }
  }

  /**
   * isAlive checks if player has lives left.
   */
  public boolean isAlive() {
    return this.Lives > 0;
  }

  /** extend your lives by one */
  public void lifeExtend() {
    this.Lives++;
  }

  /**
   * respawnPlayer makes a new player with updated lives
   * 
   */
  public Player respawnPlayer(int newLifeCount) {
    this.Power -= 2.0;
    if (this.Power < 0) {
      this.Power = 0.0;
    }
    return new Player(this.Variation, this.Radius, this.Position, newLifeCount, this.Power);
  }

  @Override
  public Player displaceBy(Vector Velocity) {
    // Math: Matrix((1, 0, 0), (0, 1, 0), delta) x Position = Position + delta, 
    // where Position and delta are Vectors
    Vector[] translate = Matrix.TranslationMatrix(Velocity); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Player displacedPlayer = new Player(this.Variation, this.Radius, displacedPosition, this.Direction, this.Velocity, this.Lives, this.Power);
    return displacedPlayer;
  }

  @Override
  public Player setNewPosition(Vector displacedPosition) {
    return new Player(this.Variation, this.Radius, displacedPosition, this.Direction, this.Velocity, this.Lives, this.Power);
  }
  
  @Override
  public Player shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (Math.round(0.8*dimension.height()) + dimension.getFieldY());

    Vector originToSpawn = new Vector(startX, startY, 1);

    return setNewPosition(originToSpawn);
  }

  @Override
  public Sprite<SpriteType, SpriteState> rotateAxisBy(double theta) {
    Vector[] rotateAroundPosition = Matrix.RotationMatrix(theta, new Vector(0, 0, 1));
    Vector rotatedDirection = this.Direction.transformVect(rotateAroundPosition);
    
    return new Player(this.Variation, this.Radius, this.Position, rotatedDirection, this.Velocity, this.Lives, this.Power);
  }
  
  
  
}
