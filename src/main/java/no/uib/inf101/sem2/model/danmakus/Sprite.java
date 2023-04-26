package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;

public abstract class Sprite<E, T> {
  
  protected T directionState; // determine bullet and enemy pointing direction
  protected E type; // Player, Enemy or Bullet
  protected SpriteVariations Variation; // player1, player2, enemy1, enemy2, ball, arrow, etc...
  protected final int Radius;
  protected Vector Position;
  protected Vector Direction;
  protected Vector Velocity;

  /**
   * Constructor for abstract class sprite, used by child classes:
   * Player, Enemies and Bullets.
   * @param type represents the sprite type (bullet, player, etc...). used for collision check.
   * @param Variation represents the any sub-type of given type. example: Enemy has variations monster1 and monster2.
   * @param directionState determines how a given sprite should move. 
   * Example: "aim" makes target move in the same direction as it faces (Direction vector) and
   * sequence makes the sprite move in the same direction as a different sprite-object that spawned before it.
   */
  public Sprite(E type, SpriteVariations Variation, T directionState, int Radius, Vector Position, Vector Direction, Vector Velocity) {
    this.type = type;
    this.Variation = Variation;
    this.directionState = directionState;
    this.Radius = Radius;
    this.Position = Position;
    this.Direction = Direction;
    this.Velocity = Velocity;
  }

  /** @return type of sprite. Valid types are Player, Enemy, Bullets and Consumables. */
  public E getType() {
    return this.type;
  }

  /** @return state which determines sprite behavoir. */
  public T getState() {
    return this.directionState;
  }

  /** @return variation of the Sprite's type. Example: type Enemy has variations yokai1 or boss4. */
  public SpriteVariations getVariation() {
    return this.Variation;
  }

  /** @return radius of sprite's circular hitbox. */
  public int getRadius() {
    return this.Radius;
  }

  /** @return position vector coordinates of sprite. */
  public Vector getPosition() {
    return this.Position;
  }

  /** @return Direction vector in which the sprite is facing. */
  public Vector getAimVector() {
    return this.Direction;
  }

  /** @return velocity vector of sprite. */
  public Vector getVelocity() {
    return this.Velocity;
  }

  /** change velocity vector of sprite.
   * @param newVel is new velocity that the sprite moves with. */
  public void setVelocity(Vector newVel) {
    this.Velocity = newVel;
  }

  /**
   * rotateAxisBy rotates the enemy around its own axis, where 
   * the angle theta either depends on time or fixed rotations.
   * Rotation can be done at any point on field, but note that the vector being rotated must be position.vect + direction.Vect. 
   * example: rotate direction vector(5, 0, 1) to vector(0, 5, 1) "- 90 degrees" at position vector(25, 25, 1). Then, 
   * (direction + pos) * rotationMatrix(- pi / 2, pos) = vector(25, 30, 1), since (direction + pos) = vector(30, 25, 1).
   */
  public abstract Sprite<E, T> rotateAxisBy(double theta);

  /**
   * displaceBy moves the sprite position vector by velocity vector.
   * @return new Spirte-object displaced by {@link #getVelocity}
   */
  public abstract Sprite<E, T> displaceBy(Vector Velocity);

  /** @return new sprite displaced to a fixed spawnpoint on the field. */
  public abstract Sprite<E, T> shiftedToStartPoint(FieldDimension dimension);

  /**
   * setNewPosition sets current position to any Vector point on field. New Vector can be a transformed version of old position
   * example: Vect(sin(x), sin(y) + center, 1) returns a displacement moving along a sine wave center, 
   * where center is a line parallell to x-axis. Is used by {@link #shiftedToStartPoint}).
   */
  public abstract Sprite<E, T> setNewPosition(Vector displacedPosition);

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((directionState == null) ? 0 : directionState.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((Variation == null) ? 0 : Variation.hashCode());
    result = prime * result + Radius;
    result = prime * result + ((Position == null) ? 0 : Position.hashCode());
    result = prime * result + ((Direction == null) ? 0 : Direction.hashCode());
    result = prime * result + ((Velocity == null) ? 0 : Velocity.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Sprite<E, T> other = (Sprite<E, T>) obj;
    if (directionState == null) {
      if (other.directionState != null)
        return false;
    } else if (!directionState.equals(other.directionState))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (Variation != other.Variation)
      return false;
    if (Radius != other.Radius)
      return false;
    if (Position == null) {
      if (other.Position != null)
        return false;
    } else if (!Position.equals(other.Position))
      return false;
    if (Direction == null) {
      if (other.Direction != null)
        return false;
    } else if (!Direction.equals(other.Direction))
      return false;
    if (Velocity == null) {
      if (other.Velocity != null)
        return false;
    } else if (!Velocity.equals(other.Velocity))
      return false;
    return true;
  }
  
  
  

}
