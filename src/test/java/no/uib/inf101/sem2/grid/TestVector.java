package no.uib.inf101.sem2.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Testing the class Vector and it's operation methods
 */
public class TestVector {
  
  @Test
  void sanityTest() {
    Vector cp = new Vector(4, 3, 1);
    assertEquals(4, cp.x());
    assertEquals(3, cp.y());
  }
  
  @Test
  void coordinateEqualityTest() {
    Vector a = new Vector(2, 3, 1);
    Vector b = new Vector(2, 3, 1);
    
    assertFalse(a == b);
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
    assertTrue(Objects.equals(a, b));
  }
  
  @Test
  void coordinateInequalityTest() {
    Vector a = new Vector(2, 3, 1);
    Vector b = new Vector(3, 2, 1);
    
    assertFalse(a == b);
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
    assertFalse(Objects.equals(a, b));
  }
  
  @Test
  void coordinateHashcodeTest() {
    Vector a = new Vector(2, 3, 1);
    Vector b = new Vector(2, 3, 1);
    assertTrue(a.hashCode() == b.hashCode());
    
    Vector c = new Vector(100, 100, 1);
    Vector d = new Vector(100, 100, 1);
    assertTrue(c.hashCode() == d.hashCode());
  }

  // Vector operations tests

  @Test
  void VectorAdditionTest() {
    Vector a = new Vector(3, 0, 1);
    Vector b = new Vector(0, 2, 1);
    Vector c = new Vector(3, 2, 1);

    assertFalse(a.equals(c));
    assertEquals(c, a.addVect(b));
    assertTrue(c.equals(a.addVect(b)));

  }

  @Test
  void VectorScalingTest() {
    int scalar = 2;
    Vector a = new Vector(3, 2, 1);
    Vector b = new Vector(6, 4, 1);
    Vector c = new Vector(6, 3, 1);

    assertEquals(b, a.multiplyScalar(scalar));
    assertTrue(b.equals(a.multiplyScalar(scalar)));
    assertFalse(c.equals(a.multiplyScalar(scalar)));

  }

  @Test
  void VectorNormalizationTest() {

    Vector a = new Vector(3, 4, 1);
    Vector b = new Vector(3, 4, 1);
    
    a = a.normaliseVect();

    double normalOfb = 1 / Math.sqrt(3*3 + 4*4);
    b = b.multiplyScalar(normalOfb);

    assertEquals(a, b);
    
  }
}
