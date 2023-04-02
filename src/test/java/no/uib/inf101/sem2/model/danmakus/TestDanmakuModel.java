package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.DanmakuField;
import no.uib.inf101.sem2.model.DanmakuModel;

public class TestDanmakuModel {

  private static final int x = 0;
  private static final int y = 0;
  private static final int width = 100;
  private static final int height = 200;
  
  @Test
  public void testSuccessfulPlayerMovement() {

  DanmakuField field = new DanmakuField(x, y, width, height);
  DanmakuFactory factory = new DanmakuSpawner();
  DanmakuModel model = new DanmakuModel(field, factory);

  Vector direction = new Vector(0, -1); // move up
  double distance = 5;
  
  // move up by 5 pixels should return true when player is at spawn.
  assertTrue(model.movePlayer(direction, distance));
    
  }

  @Test
  public void testFailedPlayerMovement() {

  DanmakuField field = new DanmakuField(x, y, width, height);
  DanmakuFactory factory = new DanmakuSpawner();
  DanmakuModel model = new DanmakuModel(field, factory);

  // check spawn point is correct:
  // xPos = width / 2 - radius
  // yPos = 0.8 * height - radius
  int radius = model.getPlayer().getRadius();
  Vector checkSpawn = new Vector(
    Math.round(width / 2) - radius, 
    Math.round(0.8*height) - radius
  );  
  assertEquals(checkSpawn, model.getPlayer().getPosition());

  Vector direction = new Vector(0, 1);
  double distance = 1;
  // move player to field lower bound
  for (int i = 0; i < 50; i++) {
    model.movePlayer(direction, distance);
  }
  // should return false when player can't move past field border
  assertFalse(model.movePlayer(direction, 1));

  // check if player is still on field
  assertTrue(model.getPlayer().getPosition().y() < height - radius);
  }

}
