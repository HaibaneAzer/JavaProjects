package no.uib.inf101.sem2.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Testing the class Matricies, NB: problems with matrix rotation.
 */
public class TestMatricies {

  @Test
  void TranselateMatrixTest() {
    Matricies Matrix = new Matricies();
    Vector a = new Vector(3, 0, 1);
    Vector b = new Vector(0, 1.7, 1);
    Vector c = new Vector(1.2, 2, 1);
    Vector cInverse = new Vector(-1.2, -2, 1);
    // axioms for vectors
    // Associativity: a + (b + c) = (a + b) + c
    Vector bPlusC = b.transformVect(Matrix.TranslationMatrix(c));
    Vector aPlusb = a.transformVect(Matrix.TranslationMatrix(b));
    assertEquals(
      a.transformVect(Matrix.TranslationMatrix(bPlusC)), 
      aPlusb.transformVect(Matrix.TranslationMatrix(c))
    );
    // Commutativity: a + b = b + a
    assertEquals(
      a.transformVect(Matrix.TranslationMatrix(b)), 
      b.transformVect(Matrix.TranslationMatrix(a))
    );
    // identity element: c + 0 = c
    assertEquals(c, c.transformVect(Matrix.TranslationMatrix(new Vector(0, 0, 1))));
    // inverse elements: c + (-c) = 0
    assertEquals(
      new Vector(0, 0, 1), 
      c.transformVect(Matrix.TranslationMatrix(cInverse))
    );
  }

  @Test
  void ScaleMatrixTest() {
    Matricies M = new Matricies();
    int a = 2;
    double b = 3.2;
    Vector u = new Vector(2.1, 0.2, 1);
    Vector v = new Vector(0, 3, 1);
    Vector w = new Vector(0.3, 3, 1);
    Vector uTimesb = u.transformVect(M.ScaleMatrix(b));

    // Compatability of scalar multiplication with field multiplication: 
    // a * (b * u) = (a * b) * u
    assertEquals(uTimesb.transformVect(M.ScaleMatrix(a)), u.transformVect(M.ScaleMatrix(a*b)));
    // identity element of scalar: 1*w = w;
    assertEquals(w, w.transformVect(M.ScaleMatrix(1)));
    // Scalar Distributivity respects vector addition:
    // a*(u + v) = a*u + a*v
    Vector uPlusv = u.transformVect(M.TranslationMatrix(v));
    Vector uTimesa = u.transformVect(M.ScaleMatrix(a));
    Vector vTimesa = v.transformVect(M.ScaleMatrix(a));
    assertEquals(
      uPlusv.transformVect(M.ScaleMatrix(a)), 
      uTimesa.transformVect(M.TranslationMatrix(vTimesa))
    );
    // Scalar Distributivity respects field addition:
    // (a + b) * w = a*w + b*w
    Vector aTimesw = w.transformVect(M.ScaleMatrix(a));
    Vector bTimesw = w.transformVect(M.ScaleMatrix(b));
    assertEquals(
      w.transformVect(M.ScaleMatrix(a + b)),
      aTimesw.transformVect(M.TranslationMatrix(bTimesw)) 
    );

  }

  @Test
  void RotationMatrixTest() {
    Matricies M = new Matricies();
    Vector a = new Vector(5, 0, 1);
    Vector origin = new Vector(0, 0, 1);

    // test -90 degree rotation around origin. Since our coordinate system has positive y down, a 
    // 90 degrees clockwise rotation should put the vector parallell to y axis.
    // pos is the starting point of vector a, which in this case is origin so we subtract it by it's length.
    Vector rotatedA = a.transformVect(M.RotationMatrix( Math.PI / 2, origin));
    assertEquals(new Vector(0, 5, 1), rotatedA);

    Vector b = new Vector(25, 25, 1);
    Vector c = new Vector(30, 25, 1);

    // test 270 degree rotation of vector "a" at spesific point "b"
    Vector rotatedAatB = c.transformVect(M.RotationMatrix( - Math.PI * 1.5 , b));
    assertEquals(new Vector(25, 30, 1), rotatedAatB);

    // test to see if turning by 2*pi/N, N times returns original vector
    int N = 5;
    Vector rotateA = a.transformVect(M.RotationMatrix(((2*Math.PI) / N), origin));

    for (int i = 0; i < 2; ++i) {
      rotateA = rotateA.transformVect(M.RotationMatrix(((2*Math.PI) / N), origin));
    }

    assertEquals(a, rotateA);

    // large angle will not increase vector length.
    Vector rotatedLargeCatB = c.transformVect(M.RotationMatrix(Math.PI*613, b));
    assertEquals(5, rotatedLargeCatB.subVect(b).length());
  }

}
