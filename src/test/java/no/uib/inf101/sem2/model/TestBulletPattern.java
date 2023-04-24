package no.uib.inf101.sem2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.DanmakuSpawner;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public class TestBulletPattern {
  
  private final DanmakuFactory sprite = new DanmakuSpawner();
  private final BulletPattern patterns = new BulletPattern(sprite);
  private static final int x = 0;
  private static final int y = 0;
  private static final int width = 400;
  private static final int height = 600;

  @Test
  public void TestPlayerShoot() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuModel model = new DanmakuModel(field, sprite);
    Vector right = new Vector(50, 0, 1);
    Player player1 = sprite.getNewPlayer(SpriteVariations.player1).shiftedToStartPoint(field).displaceBy(right);

    List<Bullets> pBullet = patterns.playerShoot(player1, false);

    model.movePlayer(right);
    model.playerFire(9, false);

    // check that model and shooter method has the same position.
    assertEquals(pBullet.iterator().next().getPosition(), model.getBulletsOnField().iterator().next().getPosition());
    assertEquals(pBullet.iterator().next().getPosition(), model.getBulletsOnField().iterator().next().getPosition());
    assertEquals(pBullet.iterator().next().getPosition(), model.getBulletsOnField().iterator().next().getPosition());

    List<Bullets> pBullet2 = patterns.playerShoot(player1, false);
    List<Bullets> pBulletFocus = patterns.playerShoot(player1, true);

    // check that summon pos is different
    assertEquals(pBullet, pBullet2);
    assertNotEquals(pBullet, pBulletFocus);

  }

  @Test
  public void TestEnemyShoot() {

    DanmakuField field = new DanmakuField(x, y, width, height);
    DanmakuModel model = new DanmakuModel(field, sprite);
    Enemies enemy = sprite.getNewEnemy(SpriteVariations.yokai1).shiftedToStartPoint(field);
    Player p1 = sprite.getNewPlayer(SpriteVariations.player1);

    for (int i = 0; i < 500; i++) {
      model.moveEnemiesInWaves();
    }
    Vector lineUp = model.getEnemiesOnField().iterator().next().getPosition().subVect(enemy.getPosition());
    enemy = enemy.displaceBy(lineUp);
    List<Bullets> eBullet = patterns.enemyShoot(enemy, p1);
    model.enemyFire();

    // check that model and shooter method has same position.
    assertEquals(eBullet.iterator().next().getVariation(), model.getBulletsOnField().iterator().next().getVariation());
    assertEquals(eBullet.iterator().next().getPosition(), model.getBulletsOnField().iterator().next().getPosition());
    assertEquals(eBullet.iterator().next().getBulletOwner(), model.getBulletsOnField().iterator().next().getBulletOwner());

  }

}
