package no.uib.inf101.sem2.model;
  
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.DanmakuSpawner;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public class TestDanmakuModel {

  // used for all tests
  private static final int x = 0;
  private static final int y = 0;
  private static final int width = 400;
  private static final int height = 800;
  
  @Test
  public void testSuccessfulPlayerMovement() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);

    Vector direction = new Vector(0, -1, 1); // move up
    double distance = 5;
  
    // move up by 5 pixels should return true when player is at spawn.
    assertTrue(model.movePlayer(direction.multiplyScalar(distance)));
    
  }

  @Test
  public void testFailedPlayerMovement() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);

    // check spawn point is correct:
    // xPos = width / 2
    // yPos = 0.8 * height
    int radius = model.getPlayer().getRadius();
    Vector checkSpawn = new Vector(
      Math.round(width / 2), 
      Math.round(0.8*height),
      1
    );  
    assertEquals(checkSpawn, model.getPlayer().getPosition());

    Vector direction = new Vector(0, 1, 1);
    double distance = 1;

    // move player to field lower bound
    for (int i = 0; i < 500; i++) {
      model.movePlayer(direction.multiplyScalar(distance));
    }

    // should return false when player can't move past field border
    assertFalse(model.movePlayer(direction.multiplyScalar(distance)));

    // check if player is still on field
    assertTrue(model.getPlayer().getPosition().y() < height - radius);
  }

  @Test
  public void testPlayerFire() {
    int fireRate = 5;
    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);

    model.playerFire(fireRate, false);

    // check if a bullet spawns, that belongs to the player.
    assertEquals(model.getPlayer().getVariation(), model.getBulletsOnField().iterator().next().getBulletOwner());
    
    int count = 0;
    Iterator<Bullets> bulletIt = model.getBulletsOnField().iterator();
    while (bulletIt.hasNext()) {
      count++;
      bulletIt.next();
    }

    // check if player2 shoots 3 bullets
    assertEquals(3, count);

    // calling playerFire again will not spawn more bullets until firerate limit
    // is reached (number of calls = fireRate, shoots new set of bullets).
    model.playerFire(fireRate, false);

    count = 0;
    bulletIt = model.getBulletsOnField().iterator();
    while (bulletIt.hasNext()) {
      count++;
      bulletIt.next();
    }

    assertEquals(3, count);

    // new bullets spawn after limit is reached
    for (int i = 0; i <= 3; i++) {
      model.playerFire(fireRate, false);
    }

    count = 0;
    bulletIt = model.getBulletsOnField().iterator();
    while (bulletIt.hasNext()) {
      count++;
      bulletIt.next();
    }

    assertEquals(6, count);

  }

  @Test
  public void testEnemyMoveAndFire() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);

    // get enemies on field (fastforward past stage delay)
    for (int i = 0; i <= 500; i++) {
      model.moveEnemiesInWaves();
    }

    // first wave spawns yokai
    Enemies curEnemy = model.getEnemiesOnField().iterator().next();
    assertEquals(SpriteVariations.yokai, curEnemy.getVariation());
    
    // move 50 steps until enemy can shoot (past 0.95 line)
    for (int i = 0; i < 80; i++) {
      model.moveEnemiesInWaves();
    }
    // check if enemy can shoot when spawned.
    model.enemyFire();

    // check if a bullet spawns, that belongs to the enemy.
    assertEquals(model.getEnemiesOnField().iterator().next().getVariation(), model.getBulletsOnField().iterator().next().getBulletOwner());
    
    int count = 0;
    Iterator<Bullets> bulletIt = model.getBulletsOnField().iterator();
    while (bulletIt.hasNext()) {
      count++;
      bulletIt.next();
    }
    
    // check if yokai shoots 3 bullets
    assertEquals(3, count);
    
    // calling enemyFire again will not spawn more bullets until firerate limit
    // is reached (number of calls = fireRate, shoots new set of bullets).
    model.enemyFire();
    
    count = 0;
    bulletIt = model.getBulletsOnField().iterator();
    while (bulletIt.hasNext()) {
      count++;
      bulletIt.next();
    }
    
    assertEquals(3, count);
    
    // new bullets spawn after limit is reached
    for (int i = 0; i < model.getEnemiesOnField().iterator().next().getFireDelay(); i++) {
      model.enemyFire();
    }
    
    count = 0;
    bulletIt = model.getBulletsOnField().iterator();
    while (bulletIt.hasNext()) {
      count++;
      bulletIt.next();
    }
    
    assertEquals(6, count);

    // check if next enemy spawns in order
    model.moveEnemiesInWaves();
    
    List<Enemies> enemies = new ArrayList<>();
    if (model.getEnemiesOnField().iterator().hasNext()) {
      for (Enemies enemy : model.getEnemiesOnField()) {
        enemies.add(enemy);
      }
    }

    assertTrue(enemies.size() == 2);

  }

  @Test
  public void testBossAndStageContinuation() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);

    // start is at stage 1
    assertTrue(model.getCurrentStage() == 1);

    for (int i = 0; i < 5500; i++) {
      model.moveEnemiesInWaves();
    }

    // boss spawns after set time
    assertEquals(SpriteVariations.MoFboss1, model.getBossEnemyOnField().getVariation());

    // stage doesnt change before boss is dead
    for (int i = 0; i < 7550; i++) {
      model.moveEnemiesInWaves();
    }

    assertTrue(model.getCurrentStage() == 1);

    // kill boss leads to next stage + player bullets can harm boss
    for (int i = 0; i < 4000; i++) {
      model.playerFire(3, true);
      if (model.getBulletsOnField().iterator().hasNext()) {
        model.moveAllBullets();
      }
    }
    // advance stage
    for (int i = 0; i < 10; i++) {
      model.moveEnemiesInWaves();
    }
    
    assertTrue(model.getBossEnemyOnField() == null);

    assertTrue(model.getCurrentStage() == 2);

    
  }

  @Test
  public void testBulletCollisionEnemy() {

    Enemies enemy = null;
    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);
    Player player = model.getPlayer();

    // setup
    for (int i = 0; i < 550; i++) {
      model.moveEnemiesInWaves();
    }
    
    if (model.getEnemiesOnField().iterator().hasNext()) {
      enemy = model.getEnemiesOnField().iterator().next();
    }
    Vector lineUp = new Vector(enemy.getPosition().x() - player.getPosition().x(), 0, 1);
    model.movePlayer(lineUp);
    
    // check enemy health 
    assertEquals(enemy.getHealthPoints(), enemy.getMaxhealth());

    // run simulation for 80 ticks
    model.movePlayer(new Vector(2, 0, 1));
    for (int i = 0; i < 80; i++) {
      model.playerFire(5, true);
      model.moveEnemiesInWaves();
      if (model.getBulletsOnField().iterator().hasNext()) {
        model.moveAllBullets();
      }
      if (model.getEnemiesOnField().iterator().hasNext()) {
        enemy = model.getEnemiesOnField().iterator().next();
      }
      lineUp = new Vector(enemy.getPosition().x() - player.getPosition().x(), 0, 1);
      model.movePlayer(lineUp);
      
    }

    if (model.getEnemiesOnField().iterator().hasNext()) {
      enemy = model.getEnemiesOnField().iterator().next();
    }
    // check collision + dmg taken

    assertTrue(enemy.getHealthPoints() < enemy.getMaxhealth());

  }

  @Test
  public void testBulletCollisionPlayer() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuFactory factory = new DanmakuSpawner();
    DanmakuModel model = new DanmakuModel(field, factory);
    Player player = model.getPlayer();
    Vector checkSpawn = new Vector(
      Math.round(width / 2), 
      Math.round(0.8*height),
      1
    );  

    // setup
    for (int i = 0; i < 1550; i++) {
      model.moveEnemiesInWaves();
    }
    
    // check player lives health 
    assertEquals(3, player.getLives());

    // run simulation for 300 ticks
    model.movePlayer(new Vector(0, -250, 1));

    assertEquals(model.getPlayer().getPosition(), checkSpawn.subVect(new Vector(0, 250, 1)));
    for (int i = 0; i < 400; i++) {
      model.enemyFire();
      model.moveAllBullets();
      player = model.getPlayer();
    }

    // check collision + dmg taken
    assertTrue(player.getLives() < 3);

    // check player respawned position
    assertEquals(model.getPlayer().getPosition(), checkSpawn);
  
  }

}

