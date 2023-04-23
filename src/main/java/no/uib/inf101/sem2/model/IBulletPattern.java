package no.uib.inf101.sem2.model;

import java.util.List;

import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;

public interface IBulletPattern {
  
  /**
   * enemyShoot handles all bullet patterns for regular enemies.
   * @param enemy is used to determine pattern, given it's SpriteVariation.
   * @return a list of bullets being summoned per shot.
   */
  List<Bullets> enemyShoot(Enemies enemy, Player player);

  /**
   * bossShoot handles all bullet patterns for bosses.
   * @param boss is used to determine pattern, given it's SpriteVariation.
   * @param unleashSuper is used to switch between normal (false) and super attack (true).
   * @return a list of bullets being summoned per shot.
   */
  List<Bullets> bossShoot(Enemies boss, boolean unleashSuper);

  /**
   * playerShoot handles all bullet patterns for players.
   * @param player is used to determine pattern, given it's SpriteVariation.
   * @param focusedShot is used to switch between spread (false) or focused shot (true).
   * @return a list of bullets being summoned per shot.
   */
  List<Bullets> playerShoot(Player player, boolean focusedShot);

}
