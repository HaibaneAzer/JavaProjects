package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public class Enemies extends Sprite<SpriteType, SpriteState>{

  private Matricies Matrix = new Matricies();
  private static final Vector standStill = new Vector(0, 0, 1);
  private static final Vector startingAim = new Vector(0, 20, 1); // unit length aim Vector
  private int healthPoints;
  private int maxHealth;
  private int healthBars;

  /**
   * constructor for transformations
   */
  public Enemies(String EnemyVar, int healthPoints, int healthBars, int Radius, Vector Position, Vector aimDirection, Vector Velocity) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, aimDirection, Velocity);
    this.healthPoints = healthPoints;
    this.healthBars = healthBars;
  }

 /** 
  * constructor for spawning
  */
  public Enemies(String EnemyVar, int healthPoints, int healthBars, int Radius, Vector Position) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    this.healthPoints = healthPoints;
    this.maxHealth = healthPoints;
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

  /**
   * getter for enemy healthpoints
   */
  public int getHealthPoints() {
    return this.healthPoints;
  }

  /**
   * getter for maxhealth of enemy
   */
  public int getMaxhealth() {
    return this.maxHealth;
  }

  /**
   * getter for enemy healthBars
   */
  public int getHealthBars() {
    return this.healthBars;
  }

  /**
   * isAlive checks if enemy still lives. When all healthbars are depleted, return false.
   * 
   */
  public boolean isAlive() {
    return this.healthBars > 0;
  }

  /**
   * displaceBy moves the enemy position vector by velocity vector, where
   * the scalar either represents distance (when used with {@link #shiftedToStartPoint}) 
   * or speed. 
   */
  public Enemies displaceBy(Vector displacement) {
    
    Vector[] translate = Matrix.TranslationMatrix(displacement); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Enemies displacedEnemy = new Enemies(this.Variation, this.healthPoints, this.healthBars, this.Radius, displacedPosition, this.Direction, this.Velocity);
    return displacedEnemy;
  }

  /**
   * setNewPosition sets current position to any Vector. New Vector can be a transformed version of old position
   * example: Vect(sin(x), sin(y) + center, 1) returns a displacement moving along a sine wave center, where center is a line parallell to 
   * x-axis.
   */
  public Enemies setNewPosition(Vector displacedPosition) {
    return new Enemies(this.Variation, this.healthPoints, this.healthBars, this.Radius, displacedPosition, this.Direction, this.Velocity);
  }

  /**
   * rotateAxisBy rotates the enemy around its own axis, where 
   * the angle theta either depends on time or fixed rotations.
   * Rotation can be done at any point on field, but note that the vector being rotated must be position.vect + direction.Vect. 
   * example: rotate direction vector(5, 0, 1) to vector(0, 5, 1) "- 90 degrees" at position vector(25, 25, 1). Then, 
   * (direction + pos) * rotationMatrix(- pi / 2, pos) = vector(25, 30, 1), since (direction + pos) = vector(30, 25, 1).
   */
  public Enemies rotateAxisBy(double theta) {
    Vector[] rotateAroundPosition = Matrix.RotationMatrix(theta, new Vector(0, 0, 1)); // get rotation matrix, 
    Vector rotatedDirection = this.Direction.transformVect(rotateAroundPosition);

    Enemies rotatedEnemy = new Enemies(this.Variation, this.healthPoints, this.healthBars, this.Radius, this.Position, rotatedDirection, this.Velocity);
    return rotatedEnemy;
  }

  /**
   * updateDirectionState fixes the direction vector to a direction depending on enemy's current SpriteState.
   * call this only when the Sprite's state changes from state set at spawn.
   */
  public void updateDirectionState(SpriteState state) {

    if (this.directionState.equals(SpriteState.aim)) {
      this.Direction = startingAim; // direction is set to enemy aim
    } 
    else if (this.directionState.equals(SpriteState.absolute)) {
      /* set aim relative to coordinates on field */
    }
    else if (this.directionState.equals(SpriteState.relative)) {
      /* set aim relative to another sprite's aim */
    }
    else if (this.directionState.equals(SpriteState.sequence)) {
      /* set aim relative to last spawned sprite (mostly used for bullets) */
    }
  }

  /**
  * sets enemy default spawn on field.
  */
  public Enemies shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (0);

    Vector originToSpawn = new Vector(startX, startY, 1);

    return displaceBy(originToSpawn);
  }

  /**
   * attackEnemy removes healthpoints from enemy by players damage. 
   * if enemy healthpoints is less than or equal to 0, remove
   * one healthbar. if enemy is not alive, return false, otherwise return true;
   * 
   */
  public void attackEnemy(int damageTaken) {
    this.healthPoints -= damageTaken;
    if (this.healthPoints <= 0) {
      this.healthPoints = this.maxHealth;
      this.healthBars--;
      
    }
    
  }
  
}
