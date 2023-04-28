package no.uib.inf101.sem2.model;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteState;
import no.uib.inf101.sem2.model.danmakus.SpriteType;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public class BulletPattern implements IBulletPattern {

  private final DanmakuFactory getSprite;
  // boss variables
  private double bossAngleIncrement;

  /**
   * Constructor for different bullet patterns in which enemies and players can shoot with.
   */
  public BulletPattern(DanmakuFactory getSprite) {
    this.getSprite = getSprite;
    this.bossAngleIncrement = 0;
  }

  @Override
  public List<Bullets> enemyShoot(Enemies enemy, Player player) {
    if (enemy.getVariation().equals(SpriteVariations.fairy)) {
      return shootAimedPattern(enemy, player);
    }
    else if (enemy.getVariation().equals(SpriteVariations.highFairy)) {
      return shootSpreadPattern(enemy, 5, -(0.25)*Math.PI);
    }
    // default
    return shootSpreadPattern(enemy, 3, -(0.3)*Math.PI);
  }

  @Override
  public List<Bullets> bossShoot(Enemies boss, boolean unleashSuper) {
    if (boss.getVariation().equals(SpriteVariations.MoFboss1)) {
      if (unleashSuper) {
        return shootRotatingFullSpreadPattern(boss);
      }
      else {
        return shootSpreadPattern(boss, 9, -(0.45)*Math.PI);
      }
    }
    else if (boss.getVariation().equals(SpriteVariations.MoFboss2)) {
      if (unleashSuper) {
        return shootRotatingFullSpreadPattern(boss);
      }
      else {
        return shootSpreadPattern(boss, 9, -(0.45)*Math.PI);
      }
    }
    // default for other bosses
    else {
      return shootSpreadPattern(boss, 11, -(0.35)*Math.PI);
    }
  }

  @Override
  public List<Bullets> playerShoot(Player player, boolean focusedShot) {
    if (player.getVariation().equals(SpriteVariations.player1)) {
      if (focusedShot) {
        return shootPlayer1Pattern(player, 15);
      }
      else {
        return shootPlayer1Pattern(player, 30);
      }
    }
    else if (player.getVariation().equals(SpriteVariations.player2)) {
      if (focusedShot) {
        return shootPlayer2Pattern(player, 15, true);
      }
      else {
        return shootPlayer2Pattern(player, 30, false);
      }
    }
    else {
      return new ArrayList<Bullets>();
    }
  }

  private List<Bullets> shootAimedPattern(Enemies enemy, Player player) {
    List<Bullets> enemyBullets = new ArrayList<>();
    if (enemy.getFireTimer() == 0) {
      Bullets newBullet = this.getSprite.getNewBullet(SpriteVariations.circleSmall);
      newBullet.setBulletType(SpriteType.EnemyBullet);
      newBullet.setBulletOwner(enemy.getVariation());
      Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
      // set bullets default spawnpoint
      Vector spawnPoint = enemy.getPosition().addVect(enemy.getAimVector());
      spawnPoint = spawnPoint.addVect(bulletRadius);
      newBullet = newBullet.displaceBy(spawnPoint);
      // get enemy aim
      Vector aimedShot = enemy.getPosition().subVect(player.getPosition()).normaliseVect();
      aimedShot = aimedShot.multiplyScalar(-1*enemy.getAimVector().length());
      // set bullet speed to enemies aim
      newBullet.updateBulletVelocity(aimedShot);
      // update bullets direction to it's velocity
      newBullet.updateBulletDirection(newBullet.getVelocity());
      enemyBullets.add(newBullet);
    }

    return enemyBullets;
  }

  private List<Bullets> shootSpreadPattern(Enemies enemy, int shootSpread, double startingAngle) {
    //
    List<Bullets> monsterBullets = new ArrayList<>();
    if (enemy.getFireTimer() == 0) {
      double constAngle = (-1)*startingAngle;
      for (int j = 0; j < shootSpread; j++) {
        Bullets newBullet = this.getSprite.getNewBullet(SpriteVariations.ellipseLarge);
        if (enemy.getVariation().equals(SpriteVariations.yokai)) {
          newBullet = this.getSprite.getNewBullet(SpriteVariations.ballLarge);
        }
        newBullet.setBulletType(SpriteType.EnemyBullet);
        newBullet.setBulletOwner(enemy.getVariation());
        Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
        // set bullets default spawnpoint
        Vector spawnPoint = enemy.getPosition().addVect(enemy.rotateAxisBy(startingAngle).getAimVector());
        spawnPoint = spawnPoint.addVect(bulletRadius);
        newBullet = newBullet.displaceBy(spawnPoint);
        // set bullet speed to enemies aim
        newBullet.updateBulletVelocity(enemy.rotateAxisBy(startingAngle).getAimVector());
        // update bullets direction to it's velocity
        newBullet.updateBulletDirection(newBullet.getVelocity());
        monsterBullets.add(newBullet);
        startingAngle += (constAngle*(1/Math.floor(shootSpread/2)));
        
      }
    }

    return monsterBullets;
  }

  private List<Bullets> shootPlayer1Pattern(Player player, int spread) {
    List<Bullets> playerBullets = new ArrayList<>();
    Vector displaceFromDefaultSpawn = new Vector(-spread, 0, 1);
    // add 2 + N bullets per shot, where N is determined by player power
    int maxShooters = 2;
    if (player.getPower() >= 4.0) {
      maxShooters = 6;
      displaceFromDefaultSpawn = new Vector(-2.5*spread, 0, 1);
    }
    else if (player.getPower() >= 3.0) {
      maxShooters = 5;
      displaceFromDefaultSpawn = new Vector(-2*spread, 0, 1);
    }
    else if (player.getPower() >= 2.0) {
      maxShooters = 4;
      displaceFromDefaultSpawn = new Vector(-1.5*spread, 0, 1);
    }
    else if (player.getPower() >= 1.0) {
      maxShooters = 3;
    }
    
    for (int i = 0; i < maxShooters; i++) {
      Bullets newBullet = this.getSprite.getNewBullet(SpriteVariations.arrow);
      newBullet.setBulletType(SpriteType.PlayerBullet);
      newBullet.setBulletOwner(player.getVariation());
      Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
      // set bullets default spawnpoint
      Vector spawnPoint = player.getPosition().addVect(player.getAimVector()).addVect(displaceFromDefaultSpawn);
      spawnPoint = spawnPoint.addVect(bulletRadius);
      newBullet = newBullet.displaceBy(spawnPoint);
      // set bullet speed to players aim
      newBullet.updateBulletVelocity(player.getAimVector());
      // update bullets direction to it's velocity
      newBullet.updateBulletDirection(newBullet.getVelocity());
      playerBullets.add(i, newBullet);
      // set spawnpoint for next bullet
      displaceFromDefaultSpawn = displaceFromDefaultSpawn.addVect(new Vector(spread, 0, 1));
      if (maxShooters == 2) {
        displaceFromDefaultSpawn = new Vector(spread, 0, 1);
      }
    }    
    return playerBullets;
  }

  private List<Bullets> shootPlayer2Pattern(Player player, int spread, boolean isFocused) {
    List<Bullets> playerBullets = new ArrayList<>();
    Vector displaceFromDefaultSpawn = new Vector(-spread, 0, 1); 
    int maxNormalShooters = 2;
    int maxHomingShooters = 0;
    double angleIncrement = 0;
    final double rad = 0.15*Math.PI; // starting radian
    if (player.getPower() >= 4.0) {
      maxNormalShooters = 3;
      maxHomingShooters = 2;
    }
    else if (player.getPower() >= 3.0) {
      maxNormalShooters = 2;
      maxHomingShooters = 2;
    }
    else if (player.getPower() >= 2.0) {
      maxNormalShooters = 2;
      maxHomingShooters = 2;
    }
    else if (player.getPower() >= 1.0) {
      maxNormalShooters = 3;
      maxHomingShooters = 1;
    }

    if (maxNormalShooters % 2 == 0) {
      displaceFromDefaultSpawn = new Vector(-1.5*spread, 0, 1);
    }

    if (!isFocused) {
      angleIncrement = -rad;
    }
    // shoot 2 normal spread shots
    for (int i = 0; i < maxNormalShooters; i++) {
      Bullets newBullet = this.getSprite.getNewBullet(SpriteVariations.ofuda);
      newBullet.setBulletType(SpriteType.PlayerBullet);
      newBullet.setBulletOwner(player.getVariation());
      Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
      // set bullets default spawnpoint
      Vector spawnPoint = player.getPosition().addVect(player.getAimVector()).addVect(displaceFromDefaultSpawn);
      spawnPoint = spawnPoint.addVect(bulletRadius);
      newBullet = newBullet.displaceBy(spawnPoint);
      // set bullet speed to players aim
      newBullet.updateBulletVelocity(player.rotateAxisBy(angleIncrement).getAimVector());
      // update bullets direction to it's velocity
      newBullet.updateBulletDirection(newBullet.getVelocity());
      newBullet.setBulletState(SpriteState.aim);
      playerBullets.add(i, newBullet);
      // set spawnpoint for next bullet
      displaceFromDefaultSpawn = displaceFromDefaultSpawn.addVect(new Vector(spread, 0, 1));
      if (((i - 1) == (maxNormalShooters / 2) && (maxNormalShooters % 2 == 0)) || maxNormalShooters == 2) {
        displaceFromDefaultSpawn = displaceFromDefaultSpawn.addVect(new Vector(2*spread, 0, 1));
        if (!isFocused) {
          angleIncrement += (rad*2) / (maxNormalShooters/2);
        }
      }
      else if (!isFocused) {
        angleIncrement += (rad*2)/(maxNormalShooters-1);
      }
    }
    // shoot homing
    if (maxHomingShooters > 0) {
      Bullets newBullet = this.getSprite.getNewBullet(SpriteVariations.ofudaHoming);
      newBullet.setBulletType(SpriteType.PlayerBullet);
      newBullet.setBulletOwner(player.getVariation());
      Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
      // set bullet spawn
      Vector spawnPoint = player.getPosition().addVect(player.getAimVector());
      spawnPoint = spawnPoint.addVect(bulletRadius);
      newBullet = newBullet.displaceBy(spawnPoint);
      if (maxHomingShooters == 2) {
        displaceFromDefaultSpawn = new Vector(spread, 0, 1);
        Bullets newBullet2 = newBullet;
        newBullet = newBullet.displaceBy(displaceFromDefaultSpawn);
        newBullet2 = newBullet2.displaceBy(displaceFromDefaultSpawn.multiplyScalar(-1));
        newBullet2.updateBulletVelocity(player.getAimVector());
        newBullet2.updateBulletDirection(newBullet2.getVelocity());
        newBullet2.setBulletState(SpriteState.relative);
        playerBullets.add(newBullet2);
      }
      // update bullet
      newBullet.updateBulletVelocity(player.getAimVector());
      newBullet.updateBulletDirection(newBullet.getVelocity());
      newBullet.setBulletState(SpriteState.relative);
      playerBullets.add(newBullet);
    } 
    
    return playerBullets;
  }

  private List<Bullets> shootRotatingFullSpreadPattern(Enemies boss) {
    //
    List<Bullets> bossBullets = new ArrayList<>();
    // super attack
    if (boss.getFireTimer() == 0) {
      for (int j = 0; j < 9; j++) {
        Bullets newBullet = this.getSprite.getNewBullet(SpriteVariations.circleSmall);
        newBullet.setBulletType(SpriteType.BossBullet);
        newBullet.setBulletOwner(boss.getVariation());
        Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
        // set bullets default spawnpoint
        Vector spawnPoint = boss.getPosition().addVect(boss.rotateAxisBy(this.bossAngleIncrement).getAimVector());
        spawnPoint = spawnPoint.addVect(bulletRadius);
        newBullet = newBullet.displaceBy(spawnPoint);
        // set bullet speed to enemies aim
        newBullet.updateBulletVelocity(boss.rotateAxisBy(this.bossAngleIncrement).getAimVector());
        // update bullets direction to it's velocity
        newBullet.updateBulletDirection(newBullet.getVelocity());
        bossBullets.add(newBullet);
        this.bossAngleIncrement += 0.25*Math.PI;
      }
      this.bossAngleIncrement += 0.03*Math.PI;
    }
    return bossBullets;
  }
  
}
