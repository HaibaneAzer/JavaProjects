package no.uib.inf101.sem2.model.danmakus;

import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;

public final class Player {
  
  private String playerType; // used to determine player weapon (homing shot or concentrated)
  private int Radius;
  private Vector Position;
  private Vector oldPosition; 
  private Vector acceleration;
  private int Lives; // default 3
  private int Power; // dmg multiplier from 0 to 5
  
  private Player(String playerType, int Radius, Vector Position) {
    this.playerType = playerType;
    this.Radius = Radius;
    this.Position = Position;
    
  }
  
  /**
  * newPlayer is a method that contains a list of valid playable characters.
  * playable: Circular hitbox: "P1c" and "P2c", rectangular hitbox: "P1r" and "P2r".
  * 
  */
  static Player newPlayer(String newPlayerType) {
    Player playableC = switch(newPlayerType) {
      case "P1c" -> new Player(newPlayerType, 8, new Vector(-7, -7)); // want center at (0, 0)
      case "P2c" -> new Player(newPlayerType, 10, new Vector(-9, -9));
      default -> throw new IllegalArgumentException("Type '" + newPlayerType + "' does not match one of two playable characters");
    };
    return playableC;
    
  }
  
  /**
  * getter
  */
  public int getRadius() {
    return this.Radius;
  }
  
  /**
  * getter
  */
  public String getType() {
    return this.playerType;
  }
  
  /**
  * getter
  */
  public Vector getPosition() {
    return this.Position;
  }
  
  /**
  * moves player
  */
  public Player shiftedBy(int dx, int dy) {
    // Verlet integration formula:
    // newPos = 2*currentPos + oldPos + accel*dt*dt
    // where Pos and accel are vectors
    Vector PosN2 = new Vector(this.Position.x() + dx, this.Position.y() + dy); 
    Player shiftedPlayer = new Player(this.playerType, this.Radius, PosN2);
    return shiftedPlayer;
  }
  
  /**
  * sets player spawn on field
  * 
  */
  public Player shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) Math.round(dimension.width()/2) + 2*this.Radius;
    int startY = (int) Math.round(0.8*dimension.height());
    return shiftedBy(startX, startY);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((playerType == null) ? 0 : playerType.hashCode());
    result = prime * result + Radius;
    result = prime * result + ((Position == null) ? 0 : Position.hashCode());
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
    Player other = (Player) obj;
    if (playerType == null) {
      if (other.playerType != null)
      return false;
    } else if (!playerType.equals(other.playerType))
    return false;
    if (Radius != other.Radius)
    return false;
    if (Position == null) {
      if (other.Position != null)
      return false;
    } else if (!Position.equals(other.Position))
    return false;
    return true;
  }
  
  
  
  // remake hashcode and equals later.
  
  
  
  
}
