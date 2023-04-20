package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;

public class TestPlayer {
    
@Test
public void testHashCodeAndEquals() {

  Vector direction = new Vector(1, 0, 1);
  Vector zeroVector = new Vector(0, 0, 1);

  Player t1 = Player.newPlayer(SpriteVariations.player1);
  Player t2 = Player.newPlayer(SpriteVariations.player1);
  Player t3 = Player.newPlayer(SpriteVariations.player1).displaceBy(direction);
  Player s1 = Player.newPlayer(SpriteVariations.player2);
  // displacing with the zero-vector should not move player
  Player s2 = Player.newPlayer(SpriteVariations.player2).displaceBy(zeroVector); 
  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

}

}
