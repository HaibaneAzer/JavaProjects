package no.uib.inf101.sem2.grid;

/**
 * A vector consists of a x-coordinate and a y-coordinate.
 * Can be used to represent position and acceleration.
 * @param x is x coordinate of object
 * @param y is y coordinate of object
 * @param constant exists to make the Vector homogenous. Must be fixed at value 1.
 */
public record Vector(double x, double y, int constant) {


  /**
   * addVectors performs additon between two vectors
   * Math: Vect(x1, y1) + Vect(x2, y2) = Vect(x1 + x2, y1 + y2)
   * @param Vect2 is our secondary vector
   */
  public Vector addVect(Vector Vect2) {
    return new Vector(this.x + Vect2.x, this.y + Vect2.y, 1);
  }

  /**
   * scalarTimesVector performs scalar multiplication
   * Math: t * Vect(x, y) = Vect(x*t, y*t)
   * @param scalar is the 
   */
  public Vector multiplyScalar(double scalar) {
    return new Vector(this.x*scalar, this.y*scalar, 1);
  }

  /**
   * normaliseVector is used to make a Vector unit length.
   * Math: Vect(x, y) * 1 / |Vect(x, y)|
   */
  public Vector normaliseVect() {
    double invSqrt = 1 / Math.sqrt(x*x + y*y);
    return multiplyScalar(invSqrt);
  }

  /**
   * transformVect multiplies a Vector with an Affine transformation matrix
   * useful for translating, rotating, shearing and scaleing vectors
   * Linear algebra: 
   * M((a, b, 0), (c, d, 0), (u, v, 1)) x (x, y) = (a*x + c*y + u, b*x + d*y + v, 1).
   * @param matrix3x3 is the transformation matrix. 
   * Example matrix's: M((1, 0, 0),(0, 1, 0),(tx, ty, 1)) is for translation 
   * 
   */
  public Vector transformVect(Vector[] matrix3x3) {
     
    double newX = matrix3x3[0].x*this.x + matrix3x3[1].x*this.y + matrix3x3[2].x;
    double newY = matrix3x3[0].y*this.x + matrix3x3[1].y*this.y + matrix3x3[2].y;

    return new Vector(newX, newY, 1);
  }

}
