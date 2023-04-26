package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Matricies;
import no.uib.inf101.sem2.grid.Vector;

public class Consumables extends Sprite<SpriteType, SpriteState>{

  private Matricies Matrix = new Matricies();
  private static final Vector spawnAim = new Vector(0, 0, 1);
  private static final Vector downSlow = new Vector(0, 1, 1);

  /** 
   * Spawning Constructor
   */
  private Consumables(SpriteType type, SpriteVariations Variation, int Radius, Vector Position) {
      super(type, Variation, SpriteState.absolute, Radius, Position, spawnAim, downSlow);
  }

  /**
   * Translation Constructor
   */
  private Consumables(SpriteType type, SpriteVariations variation, SpriteState state,
  int Radius, Vector Position, Vector Direction, Vector Velocity) {
      super(type, variation, state, Radius, Position, Direction, Velocity);
  }

  /**
  * newConsumable is a method that contains a list of valid collectable item variations.
  * items: 'powerSmall' for player power increase, 
  * 'faithMedium' for score Multiplier and 'extend' for more lifes.
  * 
  */
  static Consumables newConsumable(SpriteVariations newConsumeVar) {
    // hitbox variables. Using circular hitboxes for all to make collision calculation easier.
    int powerSmallR = 2;
    Vector powerSmallPos = new Vector(-powerSmallR, -powerSmallR, 1);
    int faithSmallR = 2;
    Vector faithSmallPos = new Vector(-faithSmallR, -faithSmallR, 1);
    int extendR = 5;
    Vector extendPos = new Vector(-extendR, -extendR, 1);

    Consumables consume = switch(newConsumeVar) {
      // collectible items
      case powerSmall -> new Consumables(SpriteType.Power, newConsumeVar, powerSmallR, powerSmallPos);
      case powerMedium -> new Consumables(SpriteType.Power, newConsumeVar, powerSmallR, powerSmallPos); 
      case powerLarge -> new Consumables(SpriteType.Power, newConsumeVar, powerSmallR*2, powerSmallPos.multiplyScalar(2)); 
      case faithSmall -> new Consumables(SpriteType.Faith, newConsumeVar, faithSmallR, faithSmallPos); 
      case faithMedium -> new Consumables(SpriteType.Faith, newConsumeVar, faithSmallR, faithSmallPos); 
      case faithLarge -> new Consumables(SpriteType.Faith, newConsumeVar, faithSmallR*2, faithSmallPos.multiplyScalar(2)); 
      case extend -> new Consumables(SpriteType.Extend, newConsumeVar, extendR, extendPos); 
      default -> throw new IllegalArgumentException("Type '" + newConsumeVar + "' does not match one of two playable characters");
    };
    return consume;
  }

  /**
   * setItemState changes spriteState to either aim, relative, absolute or sequence.
   * Used to determine movement after spawn.
   * @param newState to change collectibles behavoir.
   */
  public void setItemState(SpriteState newState) {
    this.directionState = newState;
  }

  /**
   * updateItemVelocity sets a new velocity vector of the bullet-object.
   * How the bullet gets updated should depend on it's bullet state.
   * @param newVel is the new velocity the collectible moves with.
   */
  public void updateItemVelocity(Vector newVel) {
    this.Velocity = newVel;
  }

  @Override
  public Consumables rotateAxisBy(double theta) {
    /* not needed */
    return null;
  }

  @Override
  public Consumables displaceBy(Vector Velocity) {
    Vector[] translate = Matrix.TranslationMatrix(Velocity); // get translation matrix
    Vector displacedPosition = this.Position.transformVect(translate); // displace position
    return new Consumables(
      this.type, 
      this.Variation, 
      this.directionState, 
      this.Radius, 
      displacedPosition, 
      this.Direction, 
      this.Velocity);
  }

  @Override
  public Consumables shiftedToStartPoint(FieldDimension dimension) {
    /* not needed */
    return null;
  }

  @Override
  public Consumables setNewPosition(Vector displacedPosition) {
    return new Consumables(
        this.type, 
        this.Variation, 
        this.directionState, 
        this.Radius, 
        displacedPosition, 
        this.Direction, 
        this.Velocity);
  }
    
}
