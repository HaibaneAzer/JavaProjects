package no.uib.inf101.sem2.model.danmakus;

public interface DanmakuFactory {
  
  
  /**
  * getNewPlayer returns a new Player-object
  * @return new Player-object. Replace old before new object
  */
  Player getNewPlayer(String C);

  /**
  * getNewEnemy returns a new Enemies-object
  * @return next Enemies-object
  */
  Enemies getNewEnemy(String C);

  /**
  * getNewBullet returns a new Bullets-object
  * @return next Bullets-object
  */
  Bullets getNewBullet(String C);
  
}
