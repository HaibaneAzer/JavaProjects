package no.uib.inf101.sem2.grid;


public class Grid implements IGrid {
  
  private final int x;
  private final int y;
  private int width;
  private int height;
  // idea: make gridcells that stores player, enemy and bullet positions when their
  // centers overlap a given gridcell. 
  // might make collision checking easier (on memory) by:
  // only checking all neighbouring cells around player/enemy (+ initial cell) for bullets and then
  // calculate overlap of bullet and player/enemy hitboxes.
  /**
  * constructor for Field
  * 
  */
  public Grid(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    
  }
  
  @Override
  public int width() {
    return this.width;
  }
  
  @Override
  public int height() {
    return this.height;
  }
  
  @Override
  public int getFieldX() {
    return this.x;
  }
  
  @Override
  public int getFieldY() {
    return this.y;
  }
  
  
}
