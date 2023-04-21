package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public class Enemies extends Sprite<SpriteType, SpriteState>{

  private Matricies Matrix = new Matricies();
  private static final Vector standStill = new Vector(0, 0, 1);
  private static final Vector startingAim = new Vector(0, 2, 1); // unit length aim Vector
  private int healthPoints;
  private final int maxHealth;
  private int healthBars;
  private final int enemyFireDelay;
  private int enemyFireTimer;

  /**
   * constructor for transformations
   */
  public Enemies(SpriteVariations EnemyVar, int maxHealth, int healthPoints, int healthBars, int Radius, Vector Position, Vector aimDirection, Vector Velocity, int enemyFireTimer, int enemyFireDelay) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, aimDirection, Velocity);
    this.maxHealth = maxHealth;
    this.healthPoints = healthPoints;
    this.healthBars = healthBars;
    this.enemyFireDelay = enemyFireDelay;
    this.enemyFireTimer = enemyFireTimer;
  }

 /** 
  * constructor for spawning
  */
  public Enemies(SpriteVariations EnemyVar, int healthPoints, int healthBars, int Radius, Vector Position, int enemyFireDelay) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    this.maxHealth = healthPoints;
    this.healthPoints = this.maxHealth;
    this.healthBars = healthBars;
    this.enemyFireDelay = enemyFireDelay;
  }
  
  /**
  * newPlayer is a method that contains a list of valid playable characters.
  * playable: Circular hitbox: "P1c" and "P2c", rectangular hitbox: "P1r" and "P2r".
  * 
  */
  static Enemies newEnemy(SpriteVariations newEnemyVar) {
    Enemies enemy = switch(newEnemyVar) {
      case yokai1 -> new Enemies(newEnemyVar, 300, 1, 8, 
      new Vector(-8, -8, 1), 90); // want center at (0, 0)
      case yokai2 -> new Enemies(newEnemyVar, 500, 1, 10, 
      new Vector(-10, -10, 1), 110);
      case boss4 -> new Enemies(newEnemyVar, 3500, 2, 12, 
      new Vector(-12,-12,1), 50);
      default -> throw new IllegalArgumentException(
        "Type '" + newEnemyVar + "' does not match one of two playable characters");
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
   * getter for enemy bullet fire rate delay
   */
  public int getFireDelay() {
    return this.enemyFireDelay;
  }

  /**
   * getter for enemy bullet fire rate timer
   */
  public int getFireTimer() {
    return this.enemyFireTimer;
  }

  /**
   * setter for enemy bullet fire rate timer
   */
  public Enemies setFireTimer(int advance) {
    this.enemyFireTimer = advance;
    return new Enemies(
      this.Variation, 
      this.maxHealth, 
      this.healthPoints, 
      this.healthBars, 
      this.Radius, 
      this.Position, 
      this.Direction, 
      this.Velocity, 
      this.enemyFireTimer,
      this.enemyFireDelay);
  }

  /**
   * isAlive checks if enemy still lives. When all healthbars are depleted, return false.
   */
  public boolean isAlive() {
    return this.healthBars > 0;
  }

  @Override
  public Enemies displaceBy(Vector displacement) {
    
    Vector[] translate = Matrix.TranslationMatrix(displacement); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Enemies displacedEnemy = new Enemies(
      this.Variation, 
      this.maxHealth, 
      this.healthPoints, 
      this.healthBars, 
      this.Radius, 
      displacedPosition, 
      this.Direction, 
      this.Velocity, 
      this.enemyFireTimer, 
      this.enemyFireDelay);
    return displacedEnemy;
  }

  @Override
  public Enemies setNewPosition(Vector displacedPosition) {
    return new Enemies(
      this.Variation, 
      this.maxHealth, 
      this.healthPoints, 
      this.healthBars, this.Radius, displacedPosition, this.Direction, this.Velocity, this.enemyFireTimer, this.enemyFireDelay);
  }

  @Override
  public Enemies rotateAxisBy(double theta) {
    Vector[] rotateAroundPosition = Matrix.RotationMatrix(theta, new Vector(0, 0, 1));
    Vector rotatedDirection = this.Direction.transformVect(rotateAroundPosition);

    Enemies rotatedEnemy = new Enemies(
      this.Variation, 
      this.maxHealth, 
      this.healthPoints, 
      this.healthBars, 
      this.Radius, 
      this.Position, 
      rotatedDirection, 
      this.Velocity, 
      this.enemyFireTimer, 
      this.enemyFireDelay);
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

  @Override
  public Enemies shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (0);

    Vector originToSpawn = new Vector(startX, startY, 1);

    return setNewPosition(originToSpawn); 
  }

  /**
   * attackEnemy removes healthpoints from enemy by players damage. 
   * if enemy healthpoints is less than or equal to 0, remove
   * one healthbar. if enemy is not alive, return false, otherwise return true;
   * 
   */
  public Enemies attackEnemy(int damageTaken) {
    this.healthPoints -= damageTaken;
    if (this.healthPoints <= 0) {
      this.healthPoints = this.maxHealth;
      this.healthBars -= 1;
      
    }
    return new Enemies(
      this.Variation, 
      this.maxHealth, 
      this.healthPoints, 
      this.healthBars, 
      this.Radius, 
      this.Position, 
      this.Direction, 
      this.Velocity, 
      this.enemyFireTimer, 
      this.enemyFireDelay);
    
  }
  
}
