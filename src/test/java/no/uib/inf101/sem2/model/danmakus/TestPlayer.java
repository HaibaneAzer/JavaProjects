package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TestPlayer {
    
@Test
public void testHashCodeAndEquals() {
  Player t1 = Player.newPlayer("P1c");
  Player t2 = Player.newPlayer("P1c");
  Player t3 = Player.newPlayer("P1c").shiftedBy(1, 0);
  Player s1 = Player.newPlayer("P2c");
  Player s2 = Player.newPlayer("P2c").shiftedBy(0, 0);

  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

}

}
