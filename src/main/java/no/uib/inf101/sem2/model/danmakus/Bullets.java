package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public class Bullets extends Sprite<SpriteType, SpriteState>{

  private int damage;
  private int bulletLifeTime = 0; // time left until the bullet vanishes
  private SpriteVariations bulletOwner;
  private Matricies Matrix = new Matricies();
  private static final Vector spawnAim = new Vector(0, 0, 1);
  private static final Vector standStill = new Vector(0, 0, 1);

  /**
   * Constructor for bullet movement.
   * 
   */
  private Bullets(SpriteType type, SpriteVariations bulletVar, SpriteVariations bulletOwner, int Radius, Vector Position, Vector bulletAim, Vector Velocity, int damage) {
    super(type, bulletVar, SpriteState.aim, Radius, Position, bulletAim, Velocity);
    this.damage = damage;
    this.bulletOwner = bulletOwner;

  }

  /**
   * constructor for spawning bullet Player.
   * damage depends on player power and bullet type
   */
  private Bullets(SpriteVariations bulletVar, int Radius, Vector Position, int damage) {
    super(SpriteType.Bullet, bulletVar, SpriteState.aim, Radius, Position, spawnAim, standStill);
    this.damage = damage;

  }

  /**
   * constructor for spawning bullet Enemy.
   * collision with player removes 1 life.
   */
  private Bullets(SpriteVariations bulletVar, int Radius, Vector Position) {
    super(SpriteType.Bullet, bulletVar, SpriteState.aim, Radius, Position, spawnAim, standStill);

  }

  /**
  * newBullet is a method that contains a list of valid bullet variations.
  * playable: enemy bullets: "circleSmall" and "ellipseLarge", player bullets: "arrow".
  */
  static Bullets newBullet(SpriteVariations newBulletVar) {
    // hitbox variables. Using circular hitboxes for all to make collision calculation easier.
    int circleSmallR = 4;
    Vector circleSmallPos = new Vector(-circleSmallR, -circleSmallR, 1);
    int ellipseLargeR = 10;
    Vector ellipseLargePos = new Vector(-ellipseLargeR, -ellipseLargeR, 1);
    int arrowR = 4;
    Vector arrowPos = new Vector(-arrowR, -arrowR, 1);
    int ofudaR = 5;
    Vector ofudaPos = new Vector(-ofudaR, -ofudaR, 1);

    Bullets bullet = switch(newBulletVar) {
      // enemy bullets
      case circleSmall -> new Bullets(newBulletVar, circleSmallR, circleSmallPos);
      case ellipseLarge -> new Bullets(newBulletVar, ellipseLargeR, ellipseLargePos);
      // player bullets
      case arrow -> new Bullets(newBulletVar, arrowR, arrowPos, 36); 
      case ofuda -> new Bullets(newBulletVar, ofudaR, ofudaPos, 16); 
      default -> throw new IllegalArgumentException("Type '" + newBulletVar + "' does not match one of two playable characters");
    };
    return bullet;
    
  }

  /** @return damage of bullet (int) */
  public int getDamage() {
    return this.damage;
  }

  /** @retrun bullet life time in game ticks (int) */
  public int getBulletLifeTime() {
    return this.bulletLifeTime;
  }

  /** @return the sprite Variation that shot this bullet */
  public SpriteVariations getBulletOwner() {
    return this.bulletOwner;
  }

  /**
   * setBulletType changes spriteType to either PlayerBullet or EnemyBullet.
   * Used to determine collision action.
   * @param newType to change bullet to.
   */
  public void setBulletType(SpriteType newType) {
    this.type = newType;
  }

  /**
   * setBulletOwner changes spriteVariation of bullet owner to any of the 
   * sprite variations under types Enemy, player or bullet.
   * Used to determine collision action.
   * @param newOwner to give this bullet.
   */
  public void setBulletOwner(SpriteVariations newOwner) {
    this.bulletOwner = newOwner;
  }

  /**
   * setBulletState changes spriteState to either aim, relative, absolute or sequence.
   * Used to determine movement after spawn.
   * @param newState to change bullet behavior.
   */
  public void setBulletState(SpriteState newState) {
    this.directionState = newState;
  }

  /**
   * updateBulletVelocity sets a new velocity vector of the bullet-object.
   * How the bullet gets updated should depend on it's bullet state.
   * @param newVel is the new velocity the bullet moves with.
   */
  public void updateBulletVelocity(Vector newVel) {
    this.Velocity = newVel;
  }

  /**
   * updateBulletDirection sets a new aim vector of the bullet-object.
   * How the bullet gets updated should depend on it's bullet state.
   * @param newAim is the new pointing direction the bullet should face.
   */
  public void updateBulletDirection(Vector newAim) {
    this.Direction = newAim;
  }

  @Override
  public Bullets displaceBy(Vector Velocity) {
    // Math: Matrix((1, 0, 0), (0, 1, 0), delta) x Position = Position + delta, 
    // where Position and delta are Vectors
    Vector[] translate = Matrix.TranslationMatrix(Velocity); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Bullets displacedBullet = new Bullets(
      this.type, 
      this.Variation, 
      this.bulletOwner, 
      this.Radius, 
      displacedPosition, 
      this.Direction, 
      this.Velocity, 
      this.damage);
    return displacedBullet;
  }

  @Override
  public Bullets rotateAxisBy(double theta) {
    Vector[] rotateAroundPosition = Matrix.RotationMatrix(theta, new Vector(0, 0, 1));
    Vector rotatedDirection = this.Direction.transformVect(rotateAroundPosition);

    Bullets rotatedEnemy = new Bullets(
      this.type, 
      this.Variation, 
      this.bulletOwner, 
      this.Radius, 
      this.Position, 
      rotatedDirection, 
      this.Velocity, 
      this.damage);
    return rotatedEnemy;
  }

  @Override
  public Bullets shiftedToStartPoint(FieldDimension dimension) {
    /* Unused */
    return null;
  }

  @Override
  public Bullets setNewPosition(Vector displacedPosition) {
    /* Unused */
    return null;
  }


  
    
}
