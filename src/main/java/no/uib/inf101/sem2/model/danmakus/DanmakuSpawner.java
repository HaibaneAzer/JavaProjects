package no.uib.inf101.sem2.model.danmakus;

import java.util.ArrayList;
import java.util.List;

public class DanmakuSpawner implements DanmakuFactory{

  private final SpriteVariations[][][] enemySpawnList = {
    // stage 1
    {
    {SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1},
    {SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1},
    {SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1},
    {SpriteVariations.yokai2},
    {SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1},
    {SpriteVariations.yokai2, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai2}
    },
    // stage 2
    {
    {SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai2},
    {SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1},
    {SpriteVariations.yokai1, SpriteVariations.yokai2, SpriteVariations.yokai1},
    {SpriteVariations.yokai2, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1, SpriteVariations.yokai1},
    {SpriteVariations.yokai2, SpriteVariations.yokai2, SpriteVariations.yokai2},
    {SpriteVariations.yokai2, SpriteVariations.yokai2, SpriteVariations.yokai2}
    }
    
  };
  
  @Override
  public Player getNewPlayer(SpriteVariations C) {
    Player playableC = Player.newPlayer(C);
    return playableC;
  }

  @Override
  public Enemies getNewEnemy(SpriteVariations C) {
    Enemies newEnemy = Enemies.newEnemy(C);
    return newEnemy;
  }

  @Override
  public Bullets getNewBullet(SpriteVariations C) {
    Bullets newBullet = Bullets.newBullet(C);
    return newBullet;
  }

  @Override
  public List<List<Enemies>> getTotalEnemies(int StageNumber) {
    List<List<Enemies>> newStageEnemies = new ArrayList<List<Enemies>>();
    int waveNumber = 0;
    StageNumber = StageNumber - 1;
    for (int waveCount = 0; waveCount < enemySpawnList[StageNumber].length; waveCount++) {
      List<Enemies> newWaveEnemies = new ArrayList<Enemies>();
      for (int enemyCount = 0; enemyCount < enemySpawnList[StageNumber][waveNumber].length; enemyCount++) {
        newWaveEnemies.add(Enemies.newEnemy(enemySpawnList[StageNumber][waveNumber][enemyCount]));
      }
      newStageEnemies.add(newWaveEnemies);
      waveNumber++;
    }
    return newStageEnemies;
  }
  
  
  
  
}
