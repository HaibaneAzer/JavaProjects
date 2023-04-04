package no.uib.inf101.sem2.grid;

public class Matricies {
  
  private Vector transform[] = { // identity matrix
    new Vector(1, 0, 0),
    new Vector(0, 1, 0),
    new Vector(0, 0, 1)
  };
  
  /**
   * the Matricies class is used to make sprite movement more customizable 
   * and produce interesting movement patterns for bullets
   * 
   */
  public Matricies() {
    
  }

  /**
   * choose between custom transformations. 
   * Theory: matrix is based on a Vector field, where the matrix transforms 
   * a vector into the velocity vector at a given position on the Vector field.
   * Examples:
   * 
   * 
   */
  static void transformPath() {

  }

  /**
   * create a translation matrix, that moves a vector.
   * Used for changing position in direction of velocity vector.
   */
  public Vector[] TranslationMatrix(Vector velocity) {
    this.transform[0] = new Vector(1, 0, 0);
    this.transform[1] = new Vector(0, 1, 0);
    this.transform[2] = velocity;
    return this.transform;
  }

  /**
   * create a scaling matrix, that scales a vector.
   * Used for changing speed in any direction
   * @param scalar is scaling factor.
   */
  public Vector[] ScaleMatrix(double scalar) {
    this.transform[0] = new Vector(scalar, 0, 0);
    this.transform[1] = new Vector(0, scalar, 0);
    this.transform[2] = new Vector(0, 0, 1);
    return this.transform;
  }

  /**
   * create a rotation matrix rotating the objects
   * velocity vector around its starting point.
   * The matrix first moves vector to origin, 
   * rotates vector around origin of coordinate system, 
   * then moves it back to its starting point.
   * @param angle determines how much a vector rotates
   * @param pos is the current position of sprite-object.
   */
  public Vector[] RotationMatrix(double angle, Vector pos) {
    this.transform[0] = new Vector(Math.cos(angle), Math.sin(angle), 0);
    this.transform[1] = new Vector(- Math.sin(angle), Math.cos(angle), 0);
    this.transform[2] = new Vector(
     - pos.x()*Math.cos(angle) + pos.y()*Math.sin(angle) + pos.x(),
     - pos.x()*Math.sin(angle) - pos.y()*Math.cos(angle) + pos.y(),
     1);
     return this.transform;
  }

  /**
   * getter for transformation matrix. NB: call this after peforming
   * a transformation.
   */
  public Vector[] getTransform() {
    return this.transform;
  }

}
