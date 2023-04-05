package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.Vector;

public abstract class Sprite<E, T> {
  
  protected T directionState; // determine bullet and enemy pointing direction
  protected E type; // Player, Enemy or Bullet
  protected String Variation; // player1, player2, enemy1, enemy2, ball, arrow, etc...
  protected int Radius;
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
  public Sprite(E type, String Variation, T directionState, int Radius, Vector Position, Vector Direction, Vector Velocity) {
    this.type = type;
    this.Variation = Variation;
    this.directionState = directionState;
    this.Radius = Radius;
    this.Position = Position;
    this.Direction = Direction;
    this.Velocity = Velocity;
  }

  /**
   * getter for type
   */
  public E getType() {
    return this.type;
  }

  /**
   * getter for state
   */
  public T getState() {
    return this.directionState;
  }

  /**
   * getter for Radius
   */
  public int getRadius() {
    return this.Radius;
  }

  /**
   * getter for position vector
   */
  public Vector getPosition() {
    return this.Position;
  }

  /**
   * getter for Direction vector in which the sprite is facing
   */
  public Vector getAimVector() {
    return this.Direction;
  }

  /**
   * getter for velocity vector
   */
  public Vector getVelocity() {
    return this.Velocity;
  }

  /**
   * setter for velocity vector
   */
  public void setVelocity(Vector newVel) {
    this.Velocity = newVel;
  }
  
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
    if (Variation == null) {
      if (other.Variation != null)
        return false;
    } else if (!Variation.equals(other.Variation))
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

  // equals and hashcode
  

  

}
