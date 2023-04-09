package no.uib.inf101.sem2.model.danmakus;

import java.util.List;

public class DanmakuSpawner implements DanmakuFactory{

  private final String[][] Stages = {
    {},
    {},
    {},
    
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
  public List<Enemies> getTotalEnemies(int StageNumber) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getTotalEnemies'");
  }
  
  
  
  
}
