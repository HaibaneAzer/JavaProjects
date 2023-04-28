package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.DanmakuField;

public class TestSprite {
  
  @Test
  public void testHashCodeAndEquals() {

  Vector direction = new Vector(1, 0, 1);
  Vector zeroVector = new Vector(0, 0, 1);
  
  // testing for Player
  Sprite<SpriteType, SpriteState> p1 = Player.newPlayer(SpriteVariations.player1);
  Sprite<SpriteType, SpriteState> p2 = Player.newPlayer(SpriteVariations.player1);
  Sprite<SpriteType, SpriteState> p3 = Player.newPlayer(SpriteVariations.player1).displaceBy(direction);
  Sprite<SpriteType, SpriteState> q1 = Player.newPlayer(SpriteVariations.player2);
  // displacing with the zero-vector should not move player
  Sprite<SpriteType, SpriteState> q2 = Player.newPlayer(SpriteVariations.player2).displaceBy(zeroVector); 
  assertEquals(p1, p2);
  assertEquals(q1, q2);
  assertEquals(p1.hashCode(), p2.hashCode());
  assertEquals(q1.hashCode(), q2.hashCode());
  assertNotEquals(p1, p3);
  assertNotEquals(p1, q1);

  // testing for Enemies
  Sprite<SpriteType, SpriteState> t1 = Enemies.newEnemy(SpriteVariations.fairy);
  Sprite<SpriteType, SpriteState> t2 = Enemies.newEnemy(SpriteVariations.fairy);
  Sprite<SpriteType, SpriteState> t3 = Enemies.newEnemy(SpriteVariations.fairy).displaceBy(direction);
  Sprite<SpriteType, SpriteState> s1 = Enemies.newEnemy(SpriteVariations.highFairy);
  // displacing with the zero-vector should not move player
  Sprite<SpriteType, SpriteState> s2 = Enemies.newEnemy(SpriteVariations.highFairy).displaceBy(zeroVector); 
  assertEquals(t1, t2);
  assertEquals(s1, s2);
  assertEquals(t1.hashCode(), t2.hashCode());
  assertEquals(s1.hashCode(), s2.hashCode());
  assertNotEquals(t1, t3);
  assertNotEquals(t1, s1);

  // testing for Bullets
  Sprite<SpriteType, SpriteState> b1 = Bullets.newBullet(SpriteVariations.arrow);
  Sprite<SpriteType, SpriteState> b2 = Bullets.newBullet(SpriteVariations.arrow);
  Sprite<SpriteType, SpriteState> b3 = Bullets.newBullet(SpriteVariations.arrow).displaceBy(direction);
  Sprite<SpriteType, SpriteState> l1 = Bullets.newBullet(SpriteVariations.circleSmall);
  // displacing with the zero-vector should not move player
  Sprite<SpriteType, SpriteState> l2 = Bullets.newBullet(SpriteVariations.circleSmall).displaceBy(zeroVector); 
  assertEquals(b1, b2);
  assertEquals(l1, l2);
  assertEquals(b1.hashCode(), b2.hashCode());
  assertEquals(l1.hashCode(), l2.hashCode());
  assertNotEquals(b1, b3);
  assertNotEquals(b1, l1);
  }

  @Test
  public void testEnemySpriteVelocity() {
    Vector direction = new Vector(5, 0, 1);
    Sprite<SpriteType, SpriteState> enemy1 = Enemies.newEnemy(SpriteVariations.fairy);
    Sprite<SpriteType, SpriteState> enemy2 = Enemies.newEnemy(SpriteVariations.fairy);

    Vector radius = new Vector(enemy1.Radius, enemy1.Radius, 1);
    enemy1 = enemy1.displaceBy(radius);
    enemy2 = enemy2.displaceBy(radius);

    assertEquals(new Vector(0, 0, 1), enemy1.Velocity); 
    enemy1.setVelocity(direction);
    // testing velocity setter
    assertEquals(direction, enemy1.Velocity); 
    enemy1 = enemy1.displaceBy(enemy1.Velocity);
    // check if enemy moves by velocity
    assertEquals(direction, enemy1.Position);
    enemy1.setVelocity(enemy1.Velocity.multiplyScalar(-1));
    enemy1 = enemy1.displaceBy(enemy1.Velocity);
    // check if enemy moved back to original spawn
    assertEquals(enemy2.Position, enemy1.Position);
    
  }

  @Test
  public void testPlayerSpriteVelocity() {
    Vector direction = new Vector(5, 0, 1);
    Sprite<SpriteType, SpriteState> p1 = Player.newPlayer(SpriteVariations.player1);
    Sprite<SpriteType, SpriteState> p2 = Player.newPlayer(SpriteVariations.player1);

    Vector radius = new Vector(p1.Radius, p1.Radius, 1);
    p1 = p1.displaceBy(radius);
    p2 = p2.displaceBy(radius);

    assertEquals(new Vector(0, 0, 1), p1.Velocity); 
    p1.setVelocity(direction);
    // testing velocity setter
    assertEquals(direction, p1.Velocity); 
    p1 = p1.displaceBy(p1.Velocity);
    // check if enemy moves by velocity
    assertEquals(direction, p1.Position);
    p1.setVelocity(p1.Velocity.multiplyScalar(-1));
    p1 = p1.displaceBy(p1.Velocity);
    // check if enemy moved back to original spawn
    assertEquals(p2.Position, p1.Position);
    
  }

  @Test
  public void testBulletSpriteVelocity() {
    Vector direction = new Vector(5, 0, 1);
    Sprite<SpriteType, SpriteState> b1 = Bullets.newBullet(SpriteVariations.circleSmall);
    Sprite<SpriteType, SpriteState> b2 = Bullets.newBullet(SpriteVariations.circleSmall);

    Vector radius = new Vector(b1.Radius, b1.Radius, 1);
    b1 = b1.displaceBy(radius);
    b2 = b2.displaceBy(radius);

    assertEquals(new Vector(0, 0, 1), b1.Velocity); 
    b1.setVelocity(direction);
    // testing velocity setter
    assertEquals(direction, b1.Velocity); 
    b1 = b1.displaceBy(b1.Velocity);
    // check if enemy moves by velocity
    assertEquals(direction, b1.Position);
    b1.setVelocity(b1.Velocity.multiplyScalar(-1));
    b1 = b1.displaceBy(b1.Velocity);
    // check if enemy moved back to original spawn
    assertEquals(b2.Position, b1.Position);
    
  }

  @Test
  public void testEnemySpriteDisplacement() {
    Vector direction = new Vector(0, 5, 1);
    DanmakuField field = new DanmakuField(0, 0, 100, 100);
    Sprite<SpriteType, SpriteState> e1 = Enemies.newEnemy(SpriteVariations.MoFboss1);
    Sprite<SpriteType, SpriteState> e2 = Enemies.newEnemy(SpriteVariations.MoFboss1);

    /* testing displaceBy */
    // uncenter from origin
    Vector radius = new Vector(e1.Radius, e1.Radius, 1);
    e1 = e1.displaceBy(radius);
    e2 = e2.displaceBy(radius);

    // see whether displaceBy is constant on iteration.
    e1.setVelocity(direction);
    // should be 5 + 5 + 5 = 10
    e1 = e1.displaceBy(e1.Velocity).displaceBy(e1.Velocity).displaceBy(e1.Velocity);

    assertEquals(new Vector(0, 15, 1), e1.Position);

    // dicplacement is relative to current position
    e1.setVelocity(new Vector(5, 2, 1));
    e1 = e1.displaceBy(e1.Velocity);

    assertEquals(new Vector(5, 17, 1), e1.Position);

    /* testing setPosition */
    // check if setNewPosition is absolute regardless of previous position or what speed enemy has.
    Vector move1 = new Vector(12, 3, 1);
    e2.setVelocity(new Vector(-5, 2, 1));
    e2 = e2.setNewPosition(move1);

    assertEquals(move1, e2.Position);

    /* testing shiftedToStartPoint */
    // check if shiftedToStartPoint is absolute regardless of previous position or speed.
    e1 = e1.shiftedToStartPoint(field);
    e2 = e2.shiftedToStartPoint(field);

    assertEquals(e1.Position, e2.Position);
  }

  @Test
  public void testPlayerSpriteDisplacement() {
    Vector direction = new Vector(0, 5, 1);
    DanmakuField field = new DanmakuField(0, 0, 100, 100);
    Sprite<SpriteType, SpriteState> p1 = Player.newPlayer(SpriteVariations.player2);
    Sprite<SpriteType, SpriteState> p2 = Player.newPlayer(SpriteVariations.player2);

    /* testing displaceBy */
    // uncenter from origin
    Vector radius = new Vector(p1.Radius, p1.Radius, 1);
    p1 = p1.displaceBy(radius);
    p2 = p2.displaceBy(radius);

    // see whether displaceBy is constant on iteration.
    p1.setVelocity(direction);
    // should be 5 + 5 + 5 = 10
    p1 = p1.displaceBy(p1.Velocity).displaceBy(p1.Velocity).displaceBy(p1.Velocity);

    assertEquals(new Vector(0, 15, 1), p1.Position);

    // dicplacement is relative to current position
    p1.setVelocity(new Vector(5, 2, 1));
    p1 = p1.displaceBy(p1.Velocity);

    assertEquals(new Vector(5, 17, 1), p1.Position);

    /* testing setPosition */
    // check if setNewPosition is absolute regardless of previous position or what speed enemy has.
    Vector move1 = new Vector(12, 3, 1);
    p2.setVelocity(new Vector(-5, 2, 1));
    p2 = p2.setNewPosition(move1);

    assertEquals(move1, p2.Position);

    /* testing shiftedToStartPoint */
    // check if shiftedToStartPoint is absolute regardless of previous position or speed.
    p1 = p1.shiftedToStartPoint(field);
    p2 = p2.shiftedToStartPoint(field);

    assertEquals(p1.Position, p2.Position);
  }

  @Test
  public void testBulletSpriteDisplacement() {
    Vector direction = new Vector(0, 5, 1);
    DanmakuField field = new DanmakuField(0, 0, 100, 100);
    Sprite<SpriteType, SpriteState> b1 = Bullets.newBullet(SpriteVariations.ellipseLarge);
    Sprite<SpriteType, SpriteState> b2 = Bullets.newBullet(SpriteVariations.ellipseLarge);
    /* testing displaceBy */
    // uncenter from origin
    Vector radius = new Vector(b1.Radius, b1.Radius, 1);
    b1 = b1.displaceBy(radius);
    b2 = b2.displaceBy(radius);

    // see whether displaceBy is constant on iteration.
    b1.setVelocity(direction);
    // should be 5 + 5 + 5 = 10
    b1 = b1.displaceBy(b1.Velocity).displaceBy(b1.Velocity).displaceBy(b1.Velocity);

    assertEquals(new Vector(0, 15, 1), b1.Position);

    // dicplacement is relative to current position
    b1.setVelocity(new Vector(5, 2, 1));
    b1 = b1.displaceBy(b1.Velocity);

    assertEquals(new Vector(5, 17, 1), b1.Position);

    /* testing unused setPosition and shiftedToSpawnPoint */
    // should be true since methods returns null by default
    assertTrue(b1.setNewPosition(b1.Velocity) == null);

    assertTrue(b2.shiftedToStartPoint(field) == null);
    // should be the same for any bullet
    assertEquals(b1.setNewPosition(direction), b2.setNewPosition(direction));

    assertEquals(b2.shiftedToStartPoint(field), b1.shiftedToStartPoint(field));
   
  }

  @Test
  public void testEnemyAxisRotation() {

    Vector defaultdirection = new Vector(0, 2, 1);
    Sprite<SpriteType, SpriteState> e1 = Enemies.newEnemy(SpriteVariations.highFairy);
    Sprite<SpriteType, SpriteState> e2 = Enemies.newEnemy(SpriteVariations.fairy);

    // check same rotation for different enemies
    double angle1 = Math.PI*(0.5); // 90-degree counter clockwise
    Vector aim = e2.getAimVector();
    aim = aim.rotateVect(angle1);

    e1 = e1.rotateAxisBy(angle1);
    e2 = e2.rotateAxisBy(angle1);

    assertTrue(e1.getAimVector().equals(aim));

    assertEquals(e1.getAimVector(), e2.getAimVector());

    // check if 3 more 90-degree rotations leads to default aiming direction
    e1 = e1.rotateAxisBy(angle1).rotateAxisBy(angle1).rotateAxisBy(angle1);

    assertEquals(e1.getAimVector(), defaultdirection);

    // rotation does not affect position
    Sprite<SpriteType, SpriteState> e3 = e1;

    double angle2 = Math.PI*(2.12341);

    assertEquals(e3.Position, e1.rotateAxisBy(angle2).Position);

  }

  @Test
  public void testBulletAxisRotation() {

    Vector defaultdirection = new Vector(0, 0, 1);
    Sprite<SpriteType, SpriteState> b1 = Bullets.newBullet(SpriteVariations.arrow);
    Sprite<SpriteType, SpriteState> b2 = Bullets.newBullet(SpriteVariations.circleSmall);

    // check same rotation for different enemies
    double angle1 = Math.PI*(0.5); // 90-degree counter clockwise
    Vector aim = b2.getAimVector();
    aim = aim.rotateVect(angle1);

    b1 = b1.rotateAxisBy(angle1);
    b2 = b2.rotateAxisBy(angle1);

    assertTrue(b1.getAimVector().equals(aim));

    assertEquals(b1.getAimVector(), b2.getAimVector());

    // check if 3 more 90-degree rotations leads to default aiming direction
    b1 = b1.rotateAxisBy(angle1).rotateAxisBy(angle1).rotateAxisBy(angle1);

    assertEquals(b1.getAimVector(), defaultdirection);

    // rotation does not affect position
    Sprite<SpriteType, SpriteState> b3 = b1;

    double angle2 = Math.PI*(2.12341);

    assertEquals(b3.Position, b1.rotateAxisBy(angle2).Position);

  }

}
