package no.uib.inf101.sem2.grid;

public class Matricies {
  
  private Vector transform[] = {null, null, null}; 
  
  /**
   * the Matricies class is used to make sprite movement more customizable 
   * and produce interesting movement patterns for bullets
   * 
   */
  public Matricies() {
    this.transform[0] = new Vector(0, 0, 0);
    this.transform[1] = new Vector(0, 0, 0);
    this.transform[2] = new Vector(0, 0, 0);
  }

  /**
   * create a translation matrix
   * 
   */
  public void Translate(Vector velocity) {
    this.transform[0] = new Vector(1, 0, 0);
    this.transform[1] = new Vector(0, 1, 0);
    this.transform[2] = velocity;
  }

  /**
   * create a rotation matrix rotating the objects
   * velocity vector around its starting point.
   */
  public void Rotate(double angle, Vector pos) {
    this.transform[0] = new Vector(Math.cos(angle), Math.sin(angle), 0);
    this.transform[1] = new Vector(- Math.sin(angle), Math.cos(angle), 0);
    this.transform[2] = new Vector(
     - pos.x()*Math.cos(angle) + pos.y()*Math.sin(angle) + pos.x(),
     - pos.x()*Math.sin(angle) - pos.y()*Math.cos(angle) + pos.y(),
     1);
  }

  /**
   * getter for transformation matrix. NB: call this after peforming
   * a translation or rotation.
   */
  public Vector[] getTransform() {
    return this.transform;
  }

}
