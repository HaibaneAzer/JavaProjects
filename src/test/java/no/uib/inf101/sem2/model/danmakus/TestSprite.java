package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;

public class TestSprite {
  
  @Test
  public void testHashCodeAndEquals() {

  Vector direction = new Vector(1, 0, 1);
  Vector zeroVector = new Vector(0, 0, 1);
  double distance = 1;
  
  // testing for Player
  Sprite<SpriteType, SpriteState> p1 = Player.newPlayer("P1c");
  Sprite<SpriteType, SpriteState> p2 = Player.newPlayer("P1c");
  Sprite<SpriteType, SpriteState> p3 = Player.newPlayer("P1c").displaceBy(direction);
  Sprite<SpriteType, SpriteState> q1 = Player.newPlayer("P2c");
  // displacing with the zero-vector should not move player
  Sprite<SpriteType, SpriteState> q2 = Player.newPlayer("P2c").displaceBy(zeroVector); 
  assertEquals(p1, p2);
  assertEquals(q1, q2);
  assertEquals(p1.hashCode(), p2.hashCode());
  assertEquals(q1.hashCode(), q2.hashCode());
  assertNotEquals(p1, p3);
  assertNotEquals(p1, q1);

  // testing for Enemies
  Sprite<SpriteType, SpriteState> t1 = Enemies.newEnemy("monster1");
  Sprite<SpriteType, SpriteState> t2 = Enemies.newEnemy("monster1");
  Sprite<SpriteType, SpriteState> t3 = Enemies.newEnemy("monster1").displaceBy(direction);
  Sprite<SpriteType, SpriteState> s1 = Enemies.newEnemy("monster2");
  // displacing with the zero-vector should not move player
  Sprite<SpriteType, SpriteState> s2 = Enemies.newEnemy("monster2").displaceBy(zeroVector); 
  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

  // testing for Bullets

}

}
