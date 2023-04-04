package no.uib.inf101.sem2.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Testing the class Matricies
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

}
