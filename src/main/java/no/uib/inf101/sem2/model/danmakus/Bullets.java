package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public class Bullets extends Sprite<SpriteType, SpriteState>{

  private int damage;
  private int bulletLifeTime = 0; // time left until the bullet vanishes
  private Matricies Matrix = new Matricies();
  private static final Vector spawnAim = new Vector(0, 0, 1);
  private static final Vector standStill = new Vector(0, 0, 1);

  /**
   * Constructor for bullet movement.
   * 
   */
  public Bullets(SpriteType type, String bulletVar, int Radius, Vector Position, Vector bulletAim, Vector Velocity, int damage) {
    super(type, bulletVar, SpriteState.relative, Radius, Position, bulletAim, Velocity);
    this.damage = damage;

  }

  /**
   * constructor for spawning bullet Player.
   * damage depends on player power and bullet type
   */
  public Bullets(String bulletVar, int Radius, Vector Position, int damage) {
    super(SpriteType.Bullet, bulletVar, SpriteState.relative, Radius, Position, spawnAim, standStill);
    this.damage = damage;

  }

  /**
   * constructor for spawning bullet Enemy.
   * collision with player removes 1 life.
   */
  public Bullets(String bulletVar, int Radius, Vector Position) {
    super(SpriteType.Bullet, bulletVar, SpriteState.relative, Radius, Position, spawnAim, standStill);

  }

  /**
  * newBullet is a method that contains a list of valid bullet variations.
  * playable: enemy bullets: "circleSmall" and "ellipseLarge", player bullets: "arrow".
  * 
  */
  static Bullets newBullet(String newBulletVar) {
    // hitbox variables. Using circular hitboxes for all to make collision calculation easier.
    int circleSmallR = 4;
    Vector circleSmallPos = new Vector(-circleSmallR, -circleSmallR, 1);
    int ellipseLargeR = 10;
    Vector ellipseLargePos = new Vector(-ellipseLargeR, -ellipseLargeR, 1);
    int arrowR = 4;
    Vector arrowPos = new Vector(-arrowR, -arrowR, 1);

    Bullets bullet = switch(newBulletVar) {
      // enemy bullets
      case "circleSmall" -> new Bullets(newBulletVar, circleSmallR, circleSmallPos);
      case "ellipseLarge" -> new Bullets(newBulletVar, ellipseLargeR, ellipseLargePos);
      // player bullets
      case "arrow" -> new Bullets(newBulletVar, arrowR, arrowPos, 9); 
      default -> throw new IllegalArgumentException("Type '" + newBulletVar + "' does not match one of two playable characters");
    };
    return bullet;
    
  }

  /**
   * getter for damage
   * 
   */
  public int getDamage() {
    return this.damage;
  }

  /**
   * getter for bullet life time
   * 
   */
  public int getBulletLifeTime() {
    return this.bulletLifeTime;
  }

  /**
   * setBulletOwner changes spriteType to either PlayerBullet or EnemyBullet.
   * Used to determine collision action.
   * 
   */
  public void setBulletOwner(SpriteType newOwner) {
    this.type = newOwner;
  }

  /**
   * updateBulletVelocity sets a new velocity vector of the bullet-object.
   * How the bullet gets updated should depend on it's bullet state.
   * 
   */
  public void updateBulletVelocity(Vector newVel) {
    this.Velocity = newVel;
  }

  /**
   * updateBulletDirection sets a new aim vector of the bullet-object.
   * How the bullet gets updated should depend on it's bullet state
   * 
   */
  public void updateBulletDirection(Vector newAim) {
    this.Direction = newAim;
  }

  /**
   * displaceBy moves the bullet position vector by velocity vector, where
   * the scalar either represents distance or speed. 
   */
  public Bullets displaceBy(Vector Velocity) {
    // Math: Matrix((1, 0, 0), (0, 1, 0), delta) x Position = Position + delta, 
    // where Position and delta are Vectors
    Vector[] translate = Matrix.TranslationMatrix(Velocity); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Bullets displacedBullet = new Bullets(this.type, this.Variation, this.Radius, displacedPosition, this.Direction, this.Velocity, this.damage);
    return displacedBullet;
  }

  /**
   * rotateAxisBy rotates the bullet around its own axis, where 
   * the angle theta either depends on time or fixed rotations.
   * Rotation can be done at any point on field, but note that the vector being rotated must be position.vect + direction.Vect. 
   * example: rotate direction vector(5, 0, 1) to vector(0, 5, 1) "- 90 degrees" at position vector(25, 25, 1). Then, 
   * (direction + pos) * rotationMatrix(- pi / 2, pos) = vector(25, 30, 1), since (direction + pos) = vector(30, 25, 1).
   */
  public Bullets rotateAxisBy(double theta) {
    Vector[] rotateAroundPosition = Matrix.RotationMatrix(theta, new Vector(0, 0, 1)); // get rotation matrix // check unit tests
  
    Vector rotatedDirection = this.Direction.transformVect(rotateAroundPosition);

    Bullets rotatedEnemy = new Bullets(this.type, this.Variation, this.Radius, this.Position, rotatedDirection, this.Velocity, this.damage);
    return rotatedEnemy;
  }


  
    
}
