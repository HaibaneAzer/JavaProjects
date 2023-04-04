package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;

public class TestPlayer {
    
@Test
public void testHashCodeAndEquals() {

  Vector direction = new Vector(1, 0, 1);
  double distance = 1;

  Player t1 = Player.newPlayer("P1c");
  Player t2 = Player.newPlayer("P1c");
  Player t3 = Player.newPlayer("P1c").displaceBy(direction.multiplyScalar(distance));
  Player s1 = Player.newPlayer("P2c");
  // displacing with the zero-vector should not move player
  Player s2 = Player.newPlayer("P2c").displaceBy(direction.multiplyScalar(0)); 

  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

}

}
