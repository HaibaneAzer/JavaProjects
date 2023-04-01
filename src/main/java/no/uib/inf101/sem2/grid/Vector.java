package no.uib.inf101.sem2.grid;

/**
 * A vector consists of a x-coordinate and a y-coordinate.
 * Can be used to represent position and acceleration.
 * @param x is x coordinate of object
 * @param y is y coordinate of object
 */
public record Vector(double x, double y) {


  /**
   * addVectors performs additon between two vectors
   * Math: Vect(x1, y1) + Vect(x2, y2) = Vect(x1 + x2, y1 + y2)
   * @param Vect2 is our secondary vector
   */
  public Vector addVectors(Vector Vect2) {
    return new Vector(this.x + Vect2.x, this.y + Vect2.y);
  }

  /**
   * scalarTimesVector performs scalar multiplication
   * Math: t * Vect(x, y) = Vect(x*t, y*t)
   * @param scalar is the 
   */
  public Vector scalarTimesVector(double scalar) {
    return new Vector(this.x*scalar, this.y*scalar);
  }

  /**
   * normaliseVector is used to make a Vector unit length.
   * Math: Vect(x, y) * 1 / |Vect(x, y)|
   */
  public Vector normaliseVector() {
    double invSqrt = 1 / Math.sqrt(x*x + y*y);
    return scalarTimesVector(invSqrt);
  }

}
