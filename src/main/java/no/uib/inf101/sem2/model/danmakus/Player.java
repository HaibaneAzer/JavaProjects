package no.uib.inf101.sem2.model.danmakus;

import java.awt.Point;
import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;

public final class Player {
  
  private String playerType; // used to determine player weapon (homing shot or concentrated)
  private int Radius;
  private Vector Position;
  private int Lives; // default 3
  private double Power; // dmg multiplier from 0 to 5
  
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
      case "P1c" -> new Player(newPlayerType, 8, new Vector(-8, -8, 1)); // want center at (0, 0)
      case "P2c" -> new Player(newPlayerType, 10, new Vector(-10, -10, 1));
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
  * NB: make into a common method for enemies, bullets and player
  */
  public Player shiftedBy(int dx, int dy) {
    
    Vector shiftedPos = this.Position.addVect(new Vector(dx, dy, 1));
    Player shiftedPlayer = new Player(this.playerType, this.Radius, shiftedPos);
    return shiftedPlayer;
  }

  /**
   * displaceBy moves the player in a direction vector where
   * the scalar either represents distance (when used with {@link #shiftedToStartPoint}) 
   * or speed (when called by movePlayer method from model).  
   */
  public Player displaceBy(Vector direction, double scalar) {
    // math: position += direction * speed
    // note: changing position over time can be done by
    // multiplying with a scalar T, where T is time elapsed.
    direction = direction.normaliseVect();
    Vector newPosition = direction.multiplyScalar(scalar);
    Vector displacedPosition = this.Position.addVect(newPosition);
    Player displacedPlayer = new Player(this.playerType, this.Radius, displacedPosition);
    return displacedPlayer;
  }
  
  /**
  * sets player spawn on field
  * 
  */
  public Player shiftedToStartPoint(FieldDimension dimension) {
    int startX = (int) (Math.round(dimension.width()/2) + dimension.getFieldX());
    int startY = (int) (Math.round(0.8*dimension.height()) + dimension.getFieldY());

    Vector direction = new Vector(startX, startY, 1);
    direction = direction.normaliseVect();
    double distance = Math.sqrt(startX*startX + startY*startY);

    return displaceBy(direction, distance);
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
