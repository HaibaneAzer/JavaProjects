package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public class Enemies extends Sprite<SpriteType, SpriteState>{

  private Matricies Matrix;
  private static final Vector standStill = new Vector(0, 0, 1);
  private static final Vector startingAim = new Vector(0, 1, 1);
  private int healthPoints;
  private int healthBars;

  /**
   * constructor 
   */
  public Enemies(String EnemyVar, int healthPoints, int healthBars, int Radius, Vector Position, Vector Velocity) {
    super(SpriteType.Enemy, EnemyVar, SpriteState.aim, Radius, Position, startingAim, Velocity);
    this.healthPoints = healthPoints;
    this.healthBars = healthBars;
  }

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

  /**
   * displaceBy moves the player position vector by direction vector, where
   * the scalar either represents distance (when used with {@link #shiftedToStartPoint}) 
   * or speed. 
   */
  public Enemies displaceBy(Vector Velocity) {
    // Math: Matrix((1, 0, 0), (0, 1, 0), delta) x Position = Position + delta, 
    // where Position and delta are Vectors
    Vector[] translate = Matrix.TranslationMatrix(Velocity); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position

    Enemies displacedPlayer = new Enemies(this.Variation, this.healthPoints, this.healthBars, this.Radius, displacedPosition, this.Velocity);
    return displacedPlayer;
  }

  /**
  * sets player spawn on field
  * 
  */
  public Enemies shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (Math.round(0.1*dimension.height()) + dimension.getFieldY());

    Vector originToSpawn = new Vector(startX, startY, 1);

    return displaceBy(originToSpawn);
  }

  
}
