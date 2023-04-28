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
  private Enemies(SpriteVariations EnemyVar, int maxHealth, int healthPoints, int healthBars, int Radius, Vector Position, Vector aimDirection, Vector Velocity, int enemyFireTimer, int enemyFireDelay) {
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
  private Enemies(SpriteVariations EnemyVar, int healthPoints, int healthBars, int Radius, Vector Position, int enemyFireDelay) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, startingAim, standStill);
    this.maxHealth = healthPoints;
    this.healthPoints = this.maxHealth;
    this.healthBars = healthBars;
    this.enemyFireDelay = enemyFireDelay;
  }
  
  /**
  * newEnemy is a method that contains a list of valid enemy variations.
  * Variations: fairy, highfairy, guardianfairy, seasonalfairy,
  * cursedfairy, yokai, Trancendent, MoFboss1, MoFboss2, SubAnimBoss3,
  * SubAnimBoss4, EoSDsubBoss4, SubAnimBoss5 and MoFExtraBoss.
  */
  static Enemies newEnemy(SpriteVariations newEnemyVar) {
    Enemies enemy = switch(newEnemyVar) {
      case fairy -> new Enemies(newEnemyVar, 90, 1, 8, 
      new Vector(-8, -8, 1), 90); // want center at (0, 0)
      case highFairy -> new Enemies(newEnemyVar, 1400, 1, 12, 
      new Vector(-12, -12, 1), 70);
      case guardianFairy -> new Enemies(newEnemyVar, 400, 1, 9, 
      new Vector(-9, -9, 1), 100);
      case seasonalFairy -> new Enemies(newEnemyVar, 300, 1, 9, 
      new Vector(-9, -9, 1), 50);
      case cursedFairy -> new Enemies(newEnemyVar, 1600, 1, 12, 
      new Vector(-12, -12, 1), 75);
      case yokai -> new Enemies(newEnemyVar, 500, 1, 10, 
      new Vector(-10, -10, 1), 150);
      case Trancendent -> new Enemies(newEnemyVar, 2000, 1, 15, 
      new Vector(-15, -15, 1), 150);
      case MoFboss1 -> new Enemies(newEnemyVar, 3500, 3, 14, 
      new Vector(-14,-14,1), 45);
      case MoFboss2 -> new Enemies(newEnemyVar, 4500, 3, 14, 
      new Vector(-14,-14,1), 50);
      case SubAnimBoss3 -> new Enemies(newEnemyVar, 3000, 3, 12, 
      new Vector(-12,-12,1), 40);
      case SubAnimBoss4 -> new Enemies(newEnemyVar, 7500, 3, 14, 
      new Vector(-14,-14,1), 60);
      case EoSDsubBoss4 -> new Enemies(newEnemyVar, 3500, 2, 10, 
      new Vector(-10,-10,1), 40);
      case SubAnimBoss5 -> new Enemies(newEnemyVar, 8500, 3, 14, 
      new Vector(-14,-14,1), 50);
      case MoFExtraBoss -> new Enemies(newEnemyVar, 11500, 3, 12, 
      new Vector(-12,-12,1), 55);
      default -> throw new IllegalArgumentException(
        "Type '" + newEnemyVar + "' does not match one of two playable characters");
    };
    return enemy;
    
  }

  /** @return current enemy healthpoints. */
  public int getHealthPoints() {
    return this.healthPoints;
  }

  /** @return max healthpoints of enemy. */
  public int getMaxhealth() {
    return this.maxHealth;
  }

  /** @return total bars of healthpoints the enemy have. */
  public int getHealthBars() {
    return this.healthBars;
  }

  /** @return enemy's fire rate maximum delay time in ticks (int). */
  public int getFireDelay() {
    return this.enemyFireDelay;
  }

  /** @return current ticks (int) on fire rate timer. Used with {@link #getFireDelay}. */
  public int getFireTimer() {
    return this.enemyFireTimer;
  }

  /** @return new enemy with updated {@link #getFireTimer}. */
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

  /** @return true if enemy still has health bars left, otherwise return false. */
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
   * @param state is new behavoir of enemy movement/facing direction.
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
   * If enemy healthpoints is less than or equal to 0, remove
   * one healthbar.
   * @param damagetaken is flat damage number in int.
   * @return new enemy with updated {@link #getHealthPoints} and {@link #getHealthBars}.
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
