package no.uib.inf101.sem2.grid;

/**
 * A vector consists of a x-coordinate and a y-coordinate.
 * Can be used to represent position and acceleration.
 * @param x is x coordinate of object
 * @param y is y coordinate of object
 * @param constant exists to make the Vector homogenous. Must be fixed at value 1.
 * (or 0 for the 2 first vectors of transformation matrix)
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
   * subVectors performs subtraction between two vectors
   * Math: Vect(x1, y1) - Vect(x2, y2) = Vect(x1 - x2, y1 - y2)
   * @param Vect2 is our secondary vector
   */
  public Vector subVect(Vector Vect2) {
    return new Vector(this.x - Vect2.x, this.y - Vect2.y, 1);
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
   * rotateVect rotates the vector around origin with theta as angle.
   */
  public Vector rotateVect(double Theta, Vector Pos) {
    double rotX = this.x*Math.cos(Theta) - this.y*Math.sin(Theta);
    double rotY = this.y*Math.cos(Theta) + this.x*Math.sin(Theta);
    Vector rotatedVect = new Vector(rotX, rotY, 1);
    return rotatedVect.roundVector();
  }

  /**
   * sineVect changes vector coordinates given displacement from y/x axis and displacement vector.
   * @param sineWave returns sinewave movement along x-axis if true, false returns sine movement along y-axis.
   * @param dt is displacement from current position. with vector you use it's length.
   * @param center is distance from either axis. if sineWave is true, then it uses distance from y-axis
   */
  public Vector sineVect(int center, double dt, boolean sineWave) {
    double newY = Math.sin(this.y + dt);
    double newX = newY + center;
    if (sineWave) {
      newX = Math.sin(this.x + dt);
      newY = newX + center;
    }
    return new Vector(newX, newY, 1);
  }

  /**
   * 
   */
  public double length() {
    return Math.sqrt(x*x + y*y);
  }

  /**
   * normaliseVector is used to make a Vector unit length.
   * Math: Vect(x, y) * 1 / |Vect(x, y)|, if zero vector scale vector by zero;
   */
  public Vector normaliseVect() {
    double invSqrt = 0; 
    if (x != 0 || y != 0) { // prevent divison by zero
      invSqrt = 1 / Math.sqrt(x*x + y*y); 
    }
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

  /**
   * round down vector coordinates to prevent super small increments
   * rounding to 4 decimal places (enough for smoother changes in animation)
   */
  public Vector roundVector() {
    int decimals = 10000;
    double roundedX = Math.round(this.x*decimals);
    double roundedY = Math.round(this.y*decimals);
    return new Vector(roundedX / decimals, roundedY / decimals, 1);
  }

  // equals for vector
  public boolean equals(Object obj) {
    final double TOLERANCE = 1e-8; 
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Vector)) {
      return false;
    }
    Vector other = (Vector) obj;
    return Math.abs(this.x - other.x) < TOLERANCE &&
    Math.abs(this.y - other.y) < TOLERANCE &&
    this.constant == other.constant;
  }

}
