package no.uib.inf101.sem2.model.danmakus;

import java.util.List;

public interface DanmakuFactory {
  
  
  /**
  * getNewPlayer returns a new Player-object
  * @return new Player-object. Replace old before new object.
  */
  Player getNewPlayer(SpriteVariations C);

  /**
  * getNewEnemy returns a new Enemies-object
  * @return next Enemies-object
  */
  Enemies getNewEnemy(SpriteVariations C);

  /**
   * getTotalEnemies returns a 2d list where every list inside represents each wave
   * and each list has N amount of enemies. change enemies list by inputing a different stage.
   * @return a 2d list of pre-determined enemies to spawn.
   */
  List<List<Enemies>> getTotalEnemies(int StageNumber);

  /**
  * getNewBullet returns a new Bullets-object
  * @return next Bullets-object
  */
  Bullets getNewBullet(SpriteVariations C);

  /**
   * getNewCollectible gets collectible items
   * @return a new Consumable-object.
   */
  Consumables getNewCollectible(SpriteVariations C);
  
}
