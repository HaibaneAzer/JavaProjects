package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TestDanmakuFactory {
  
  private static final DanmakuFactory getSprite = new DanmakuSpawner();

  @Test
  public void sanityTestEnemies() {

    SpriteType typeEnemy = SpriteType.Enemy;
    List<List<Enemies>> enemyList = getSprite.getTotalEnemies(1);
    Enemies enemy = getSprite.getNewEnemy(SpriteVariations.yokai1);

    assertEquals(typeEnemy, enemyList.get(0).get(0).getType());

    assertEquals(SpriteVariations.yokai1, enemy.getVariation());

    // default state
    assertEquals(SpriteState.aim, enemy.getState());

    assertEquals(enemy.getHealthPoints(), enemy.getMaxhealth());

    // default spawn
    assertEquals(-enemy.getRadius(), enemy.getPosition().x());


  }

  @Test
  public void sanityTestPlayer() {

    Player player = getSprite.getNewPlayer(SpriteVariations.player2);

    assertEquals(SpriteVariations.player2, player.getVariation());

    // default state
    assertEquals(SpriteState.aim, player.getState());

    // default spawn
    assertEquals(-player.getRadius(), player.getPosition().x());

    // starting default lives
    assertEquals(3, player.getLives());


  }

  @Test
  public void sanityTestBullets() {

    Bullets bullet = getSprite.getNewBullet(SpriteVariations.arrow);

    assertEquals(SpriteVariations.arrow, bullet.getVariation());

    // default state
    assertEquals(SpriteState.relative, bullet.getState());

    // default spawn
    assertEquals(-bullet.getRadius(), bullet.getPosition().x());

    bullet.setBulletOwner(SpriteVariations.player1);
    assertFalse(bullet.getBulletOwner().equals(SpriteVariations.player2));

    assertEquals(SpriteType.Bullet, bullet.getType());
    bullet.setBulletType(SpriteType.BossBullet);
    assertEquals(SpriteType.BossBullet, bullet.getType());


  }

}
