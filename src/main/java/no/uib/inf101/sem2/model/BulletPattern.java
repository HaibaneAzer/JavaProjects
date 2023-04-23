package no.uib.inf101.sem2.model;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteType;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public class BulletPattern implements IBulletPattern {

  private final DanmakuFactory getSprite;
  // boss variables
  private double bossAngleIncrement;

  public BulletPattern(DanmakuFactory getSprite) {
    this.getSprite = getSprite;
    this.bossAngleIncrement = 0;
  }

  @Override
  public List<Bullets> enemyShoot(Enemies enemy, Player player) {
    if (enemy.getVariation().equals(SpriteVariations.yokai1)) {
      return shootAimedPattern(enemy, player);
    }
    else if (enemy.getVariation().equals(SpriteVariations.yokai2)) {
      return shootSpreadPattern(enemy, 5, -(0.25)*Math.PI);
    }
    else {
      return new ArrayList<Bullets>();
    }
  }

  @Override
  public List<Bullets> bossShoot(Enemies boss, boolean unleashSuper) {
    if (boss.getVariation().equals(SpriteVariations.boss4)) {
      if (unleashSuper) {
        return shootRotatingFullSpreadPattern(boss);
      }
      else {
        return shootSpreadPattern(boss, 9, -(0.45)*Math.PI);
      }
    }
    else if (boss.getVariation().equals(SpriteVariations.boss5)) {
      if (unleashSuper) {
        return shootRotatingFullSpreadPattern(boss);
      }
      else {
        return shootSpreadPattern(boss, 9, -(0.45)*Math.PI);
      }
    }
    else {
      return new ArrayList<Bullets>();
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
        return shootPlayer1Pattern(player, 15);
      }
      else {
        return shootPlayer1Pattern(player, 30);
      }
    }
    else {
      return new ArrayList<Bullets>();
    }
  }

  private List<Bullets> shootAimedPattern(Enemies enemy, Player player) {
    //
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
      aimedShot = aimedShot.multiplyScalar(-1);
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
        newBullet.setBulletType(SpriteType.BossBullet);
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
    //
    List<Bullets> playerBullets = new ArrayList<>();
    Vector displaceFromDefaultSpawn = new Vector(-spread, 0, 1); 
    // add 3 + N bullets per shot, where N is determined by player power
    // power >= 2 -> N = 1, power >= 3 -> N = 2, power >= 4 -> N = 3
    for (int i = 0; i < 3; i++) {
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
