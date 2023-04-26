package no.uib.inf101.sem2.view;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Consumables;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;

public interface ViewableDanmakuModel {
  
  /**
  * getDimension is getter for FieldDimension object. 
  * Gets field where sprites can move and shoot in.
  * @return FieldDimension of a model.
  */
  FieldDimension getDimension();
  
  /** @return current active player character. */
  Player getPlayer();

  /** @return if player IFrames are active (true). */
  boolean getIFrames();

  /** @return all enemies alive on field. */
  Iterable<Enemies> getEnemiesOnField();

  /** @return current boss on field. is null when outside of boss battle. */
  Enemies getBossEnemyOnField();

  /** @return true if boss uses super attack */
  boolean getBossAttackType();

  /** @return all bullets existing on field. */
  Iterable<Bullets> getBulletsOnField();

  /** @return current stage (int) */
  int getCurrentStage();

  /** @return current Game State */
  GameState getGameState();

  /** @return avg FPS calculated in controller */
  double getFPSValue();

  /** @return current score value calculated in model */
  int getCurrentScore();

  /** @return all consumable items on field  */
  Iterable<Consumables> getCollectiblesOnField();
}
