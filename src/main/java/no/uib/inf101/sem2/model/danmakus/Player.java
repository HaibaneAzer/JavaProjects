package no.uib.inf101.sem2.model.danmakus;

import java.lang.Math;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.GridPosition;

public final class Player {
    
    private String playerType;
    private int Width;
    private int Height;
    private int Radius;
    private GridPosition Pos;

    public Player(String playerType, int Width, int Height, GridPosition Pos) {
        this.playerType = playerType;
        this.Width = Width;
        this.Height = Height;
        this.Pos = Pos;

    }

    public Player(String playerType, int Radius, GridPosition Pos) {
        this.playerType = playerType;
        this.Radius = Radius;
        this.Width = this.Height = 2*Radius;
        this.Pos = Pos;

    }

    /**
     * newPlayer is a method that contains a list of valid playable characters.
     * playable: Circular hitbox: "P1c" and "P2c", rectangular hitbox: "P1r" and "P2r".
     * 
     */
    static Player newPlayer(String newPlayerType) {
        Player playableC = switch(newPlayerType) {
            case "P1c" -> new Player(newPlayerType, 5, new GridPosition(0, 0));
            case "P2c" -> new Player(newPlayerType, 7, new GridPosition(0, 0));
            case "P1r" -> new Player(newPlayerType, 9, 9, new GridPosition(0, 0));
            case "P2r" -> new Player(newPlayerType, 15 ,15, new GridPosition(0, 0));
            default -> throw new IllegalArgumentException("Type '" + newPlayerType + "' does not match one of two playable characters");
        };
        return playableC;

    }

    /**
     * getter
     */
    public int getWidth() {
        return this.Width;
    }

    /**
     * getter
     */
    public int getHeight() {
        return this.Height;
    }

    /**
     * getter
     */
    public int getRadius() {
        return this.Radius;
    }

    /**
     * getter
     * 
     */
    public String getType() {
        return this.playerType;
    }

    /**
     * getter
     * 
     */
    public GridPosition getPos() {
        return this.Pos;
    }

    /**
     * moves player
     * 
     */
    public Player shiftedBy(int dx, int dy) {
        Player shiftedPlayer = new Player(this.playerType, this.Width, this.Height, new GridPosition(this.Pos.x() + dx, this.Pos.y() + dy));
        return shiftedPlayer;
    }

    /**
     * sets player spawn on field
     * 
     */
    public Player shiftedToStartPoint(FieldDimension dimension) {
        return shiftedBy((int) Math.floor(dimension.width()/2) ,(int) Math.floor(0.8*dimension.height()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerType == null) ? 0 : playerType.hashCode());
        result = prime * result + Width;
        result = prime * result + Height;
        result = prime * result + Radius;
        result = prime * result + ((Pos == null) ? 0 : Pos.hashCode());
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
        } 
        else if (!playerType.equals(other.playerType))
            return false;

        if (Width != other.Width)
            return false;
        if (Height != other.Height)
            return false;
        if (Radius != other.Radius)
            return false;

        if (Pos == null) {
            if (other.Pos != null)
                return false;
        } 
        else if (!Pos.equals(other.Pos))
            return false;

        return true;
    }


    // remake hashcode and equals later.
    

    

}
