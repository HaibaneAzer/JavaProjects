package no.uib.inf101.sem2.model.danmakus;

import no.uib.inf101.sem2.grid.Vector;

public abstract class Sprite<E, T> {
  
  private T directionState; // determine bullet and enemy pointing direction
  private final E type; // Player, Enemy or Bullet
  private final String Variation; // player1, player2, enemy1, enemy2, ball, arrow, etc...
  private int Radius;
  private Vector Position;
  private Vector Direction;
  private Vector Velocity;

  /**
   * Constructor for abstract class sprite, used by child classes:
   * Player, Enemies and Bullets.
   * 
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

  public E Type() {
    return this.type;
  };

  public T State() {
    return this.directionState;
  };

  /**
   * getter for Radius
   */
  public int Radius() {
    return this.Radius;
  }

  /**
   * getter for position vector
   */
  public Vector Position() {
    return this.Position;
  }

  /**
   * getter for Direction vector
   */
  public Vector AimVector() {
    return this.Direction;
  }

  /**
   * getter for velocity vector
   */
  public Vector Velocity() {
    return this.Velocity;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Radius;
    result = prime * result + ((Position == null) ? 0 : Position.hashCode());
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
    if (Radius != other.Radius)
      return false;
    if (Position == null) {
      if (other.Position != null)
        return false;
    } else if (!Position.equals(other.Position))
      return false;
    if (Velocity == null) {
      if (other.Velocity != null)
        return false;
    } else if (!Velocity.equals(other.Velocity))
      return false;
    return true;
  }

  

}
