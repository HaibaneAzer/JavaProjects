package no.uib.inf101.sem2.model.danmakus;

import java.util.ArrayList;
import java.util.List;

public class DanmakuSpawner implements DanmakuFactory{

  private final String[][][] enemySpawnList = {
    // stage 1
    {
    {"monster1", "monster1", "monster1"}, // wave 1
    {"monster2"},
    {"monster1", "monster1", "monster1"},
    {"monster2", "monster2"}
    },
    // stage 2
    {
    {"monster1", "monster1", "monster1"},
    {"monster1", "monster1"},
    {"monster1", "monster2", "monster1", "monster1"},
    {"monster2", "monster1", "monster1"}
    }
    
  };
  
  @Override
  public Player getNewPlayer(String C) {
    Player playableC = Player.newPlayer(C);
    return playableC;
  }

  @Override
  public Enemies getNewEnemy(String C) {
    Enemies newEnemy = Enemies.newEnemy(C);
    return newEnemy;
  }

  @Override
  public Bullets getNewBullet(String C) {
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
