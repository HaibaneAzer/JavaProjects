package no.uib.inf101.sem2.grid;

/**
 * Matricies is a class doing operations with Vector-objects {@link no.uib.inf101.sem2.grid.Vector}. 
 * Mostly uses Affine transformation matricies to perform scaling and rotation, as well as translation made
 * possible using a 3x3 matrix instead for 2x2 in 2D coordinate system. Vectors must therefore have 3 values, 
 * but third coordinate can stay fixed since it will not be used.
 */
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
   * 
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
   * @param pos is the current starting position of the given Vector you want to rotate.
   * remember to subtract the Vector by it's self to use as pos vector if you want to rotate
   * something like the direction vector.
   */
  public Vector[] RotationMatrix(double angle, Vector pos) {
    this.transform[0] = new Vector(
      Math.cos(angle), 
      Math.sin(angle), 
      0);
    this.transform[1] = new Vector(
      - Math.sin(angle), 
      Math.cos(angle), 
      0);
    this.transform[2] = new Vector(
     - pos.x()*Math.cos(angle) + pos.y()*Math.sin(angle) + pos.x(),
     - pos.x()*Math.sin(angle) - pos.y()*Math.cos(angle) + pos.y(),
     1);
    // round down matrix Vectors.
    this.transform[0] = this.transform[0];
    this.transform[1] = this.transform[1];
    this.transform[2] = this.transform[2];
    return this.transform;
  }

  /**
   * getter for transformation matrix. NB: call this after peforming
   * a transformation.
   */
  public Vector[] getTransform() {
    return this.transform;
  }

  // equals for matricies

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Matricies)) {
      return false;
    }
    Matricies matrix = (Matricies) obj;
    return transform[0].equals(matrix.transform[0]) &&
    transform[1].equals(matrix.transform[1]) &&
    transform[2].equals(matrix.transform[2]);
  }

  

}
