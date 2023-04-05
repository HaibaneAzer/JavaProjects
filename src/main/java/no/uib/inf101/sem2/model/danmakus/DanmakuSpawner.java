package no.uib.inf101.sem2.model.danmakus;

public class DanmakuSpawner implements DanmakuFactory{
  
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getNewBullet'");
  }
  
  
  
  
}
