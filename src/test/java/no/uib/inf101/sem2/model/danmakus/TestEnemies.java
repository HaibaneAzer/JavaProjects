package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;

public class TestEnemies {
  
  @Test
  public void testHashCodeAndEquals() {

  Vector direction = new Vector(1, 0, 1);
  Vector zeroVector = new Vector(0, 0, 1);

  Enemies t1 = Enemies.newEnemy(SpriteVariations.yokai1);
  Enemies t2 = Enemies.newEnemy(SpriteVariations.yokai1);
  Enemies t3 = Enemies.newEnemy(SpriteVariations.yokai1).displaceBy(direction);
  Enemies s1 = Enemies.newEnemy(SpriteVariations.yokai2);
  // displacing with the zero-vector should not move player
  Enemies s2 = Enemies.newEnemy(SpriteVariations.yokai2).displaceBy(zeroVector); 
  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

  }

  @Test
  public void EnemyDisplacement() {
    //
    Vector displace = new Vector(0, 10, 1);

    Enemies monster1 = Enemies.newEnemy(SpriteVariations.yokai1);
    monster1 = monster1.displaceBy(displace);
    monster1 = monster1.displaceBy(displace);

    // Check if tetro was displaced, then check if displacement is doubled on repeated shift.
    Vector checkPos = new Vector( -monster1.getRadius(), 20 - monster1.getRadius(), 1);
    assertEquals(monster1.getPosition(), checkPos);
    
  }
}
