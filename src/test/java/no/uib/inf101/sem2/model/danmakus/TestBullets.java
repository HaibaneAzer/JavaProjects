package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;

public class TestBullets {
  
  @Test
  public void testHashCodeAndEquals() {

  Vector direction = new Vector(1, 0, 1);
  Vector zeroVector = new Vector(0, 0, 1);

  Bullets t1 = Bullets.newBullet(SpriteVariations.arrow);
  Bullets t2 = Bullets.newBullet(SpriteVariations.arrow);
  Bullets t3 = Bullets.newBullet(SpriteVariations.arrow).displaceBy(direction);
  Bullets s1 = Bullets.newBullet(SpriteVariations.ellipseLarge);
  // displacing with the zero-vector should not move player
  Bullets s2 = Bullets.newBullet(SpriteVariations.ellipseLarge).displaceBy(zeroVector); 
  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

  }

}
