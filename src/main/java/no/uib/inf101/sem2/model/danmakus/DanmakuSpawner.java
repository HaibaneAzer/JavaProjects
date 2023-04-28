package no.uib.inf101.sem2.model.danmakus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.uib.inf101.sem2.model.EnemySpawnPos;

public class DanmakuSpawner implements DanmakuFactory{

  private final SpriteVariations[][][] enemySpawnList = {
    // stage 1
    {
    {SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.highFairy},
    {SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.fairy}
    },
    // stage 2
    {
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.fairy},
    {SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.yokai, SpriteVariations.fairy, SpriteVariations.yokai},
    {SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy}
    },
    // stage 3
    {
    {SpriteVariations.highFairy, SpriteVariations.highFairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai, SpriteVariations.yokai},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy}
    },
    // stage 4
    {
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.yokai, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy}
    },
    // stage 5
    {
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy}
    },
    // stage 6
    {
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy},
    {SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy, SpriteVariations.fairy}
    },
    
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
  public Consumables getNewCollectible(SpriteVariations C) {
    Consumables newCollectible = Consumables.newConsumable(C);
    return newCollectible;
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

  @Override
  public List<EnemySpawnPos> getSpawnOrder(int stage) {
    switch (stage) {
      case 1: 
        return Arrays.asList(
          EnemySpawnPos.horisontal, EnemySpawnPos.verticalRight, EnemySpawnPos.verticalLeft, 
          EnemySpawnPos.verticalMiddleR, EnemySpawnPos.verticalMiddelL, EnemySpawnPos.zigzag);
      case 2: 
        return Arrays.asList(
          EnemySpawnPos.zigzag, EnemySpawnPos.verticalLeft, EnemySpawnPos.verticalRight, 
          EnemySpawnPos.verticalMiddleR, EnemySpawnPos.verticalMiddelL, EnemySpawnPos.horisontal);
      case 3: 
        return Arrays.asList(
          EnemySpawnPos.horisontal, EnemySpawnPos.verticalRight, EnemySpawnPos.verticalMiddleR, 
          EnemySpawnPos.verticalMiddelL, EnemySpawnPos.verticalLeft, EnemySpawnPos.zigzag);
      case 4: 
        return Arrays.asList(
          EnemySpawnPos.zigzag, EnemySpawnPos.horisontal, EnemySpawnPos.verticalLeft, 
          EnemySpawnPos.verticalRight, EnemySpawnPos.verticalMiddelL, EnemySpawnPos.verticalMiddleR);
      case 5: 
        return Arrays.asList(
          EnemySpawnPos.horisontal, EnemySpawnPos.zigzag, EnemySpawnPos.verticalMiddelL, 
          EnemySpawnPos.verticalMiddleR, EnemySpawnPos.verticalRight, EnemySpawnPos.verticalLeft);
      case 6: 
        return Arrays.asList(
          EnemySpawnPos.verticalLeft, EnemySpawnPos.verticalRight, EnemySpawnPos.horisontal, 
          EnemySpawnPos.verticalMiddleR, EnemySpawnPos.verticalMiddelL, EnemySpawnPos.zigzag);
      default:
        throw new IllegalArgumentException("Invalid token " + stage + ".\n Please use integers between 1 and 6");
    }
  }
  
  
  
  
}
