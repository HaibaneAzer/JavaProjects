package no.uib.inf101.sem2.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.uib.inf101.sem2.controller.ControllableDanmakuModel;
import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteType;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel, ControllableDanmakuModel, Iterable<Bullets>{
  
  private DanmakuField Field;
  private final DanmakuFactory getSprite;
  private Player currentPlayer;
  private Bullets playerBullet;
  private int playerFireCounter = 0;
  private Enemies currentEnemy;
  private Bullets enemyBullet;
  private List<Bullets> bulletsOnField = new ArrayList<Bullets>(); 
  private double FPSCounter = 60.0;
  
  public DanmakuModel(DanmakuField Field, DanmakuFactory getSprite) {
    this.Field = Field;
    this.getSprite = getSprite;
    this.currentPlayer = getSprite.getNewPlayer("P1c").shiftedToStartPoint(Field);
    this.currentEnemy = getSprite.getNewEnemy("monster1").shiftedToStartPoint(Field);
    this.currentEnemy.updateDirectionState(this.currentEnemy.getState());
    
    
  }
  
  @Override
  public FieldDimension getDimension() {
    return this.Field;
  }
  
  @Override
  public Player getPlayer() {
    return this.currentPlayer;
  }

  @Override
  public Enemies getEnemy() {
    return this.currentEnemy;
  }

  @Override
  public Iterable<Bullets> getBulletsOnField() {
    return this.bulletsOnField;
  }

  @Override
  public double getFPSValue() {
    return this.FPSCounter;
  }

  @Override
  public void setFPSValue(double newFPS) {
    this.FPSCounter = newFPS;
  }

  @Override
  public void resetVelocity(boolean horisontal) {
    if (horisontal) {
      this.currentPlayer.setVelocity(new Vector(0, this.currentPlayer.getVelocity().y(), 1));
    }
    else {
      this.currentPlayer.setVelocity(new Vector(this.currentPlayer.getVelocity().x(), 0, 1));
    }

  } 

  @Override
  public void playerFire(int fireRate) {
    if (this.playerFireCounter == 0) {
      // triple shot
      if (this.currentPlayer.getVariation().equals("P1c")) {
        Bullets newBullet = this.getSprite.getNewBullet("arrow");
        this.playerBullet = newBullet;
        this.playerBullet.setBulletOwner(SpriteType.PlayerBullet);
        // spawn infront of player
        this.playerBullet = this.playerBullet.displaceBy(this.currentPlayer.getPosition().addVect(this.currentPlayer.getAimVector()));
        // set bullet speed to players aim
        this.playerBullet.updateBulletVelocity(this.currentPlayer.getAimVector());
        // update bullets aimvector to it's velocity
        this.playerBullet.updateBulletDirection(this.playerBullet.getVelocity());
        // add current bullet to bullet list.
        this.bulletsOnField.add(this.playerBullet);
        System.out.println(this.bulletsOnField.size());
      }
      // homing shot
    }
    this.playerFireCounter = (this.playerFireCounter + 1) % fireRate;
  }
  
  @Override
  public boolean movePlayer(Vector targetVel, double dt) {
    
    /* this.currentPlayer.accelerate(targetVel, dt); */ // NB: acceleration is janky. please fix.
    Vector displacement = targetVel;
    if (!insideField(this.currentPlayer.displaceBy(displacement))) {
      this.currentPlayer.setVelocity(new Vector(0, 0, 1));
      return false;
    }
    this.currentPlayer = this.currentPlayer.displaceBy(displacement);
    return true;

  }
  
  private boolean insideField(Player shiftedplayer) {
    boolean withinField = (
      shiftedplayer.getPosition().x() - shiftedplayer.getRadius() >= this.Field.getFieldX() && 
      shiftedplayer.getPosition().y() - shiftedplayer.getRadius() >= this.Field.getFieldY() &&
      shiftedplayer.getPosition().x() + shiftedplayer.getRadius() < this.Field.getFieldX() + this.Field.width() &&
      shiftedplayer.getPosition().y() + shiftedplayer.getRadius() < this.Field.getFieldY() + this.Field.height()
    );
    if (!withinField) {
      return false;
    }
    return true;

  }

  @Override
  public boolean rotateAxisEnemy(double theta) {
    
    // add conditions for enemy rotation if needed.
    this.currentEnemy = this.currentEnemy.rotateAxisBy(theta);
    return true;
    
  }

  @Override
  public void moveAllBullets() {
    
    /* for (Bullets bullet : this.bulletsOnField) {
      if (!bulletInsideScreen(bullet.displaceBy(bullet.getVelocity()))) {
        // vanish bullet
        this.bulletsOnField.remove(bullet);
      }
      bullet = bullet.displaceBy(bullet.getVelocity());
    } */ // for-each loop gives concurrentModificationException error

    for (int i = 0; i < this.bulletsOnField.size(); i++) {
      Bullets bullet = this.bulletsOnField.get(i);
      if (!bulletInsideScreen(bullet.displaceBy(bullet.getVelocity()))) {
        // vanish bullet
        this.bulletsOnField.remove(i);
      }
      else {
        this.bulletsOnField.set(i, bullet.displaceBy(bullet.getVelocity()));
      }
    }


  }

  /**
   * bulletInsideScreen
   * 
   */
  private boolean bulletInsideScreen(Bullets shiftedBullet) {
    // the entire hitbox of a bullet must be outside the screen before vanishing
    boolean withinScreen = (
      shiftedBullet.getPosition().x() + 2*shiftedBullet.getRadius() >= 0 &&
      shiftedBullet.getPosition().y() + 2*shiftedBullet.getRadius() >= 0 &&
      shiftedBullet.getPosition().x() - 2*shiftedBullet.getRadius() < 2*this.Field.getFieldX() + this.Field.width() &&
      shiftedBullet.getPosition().y() - 2*shiftedBullet.getRadius() < 2*this.Field.getFieldY() + this.Field.height()
    );
    if (!withinScreen) {
      return false;
    }
    return true;
  }

  /**
   * checkHitboxCollision is a helper method used by {@link #bulletInsideScreen}.
   * checks if the hitboxes of sprites overlap. Conditions:
   * First, check if player is touching enemy's hitbox or their bullets hitbox 
   * (not including player bullets). Second, check if player bullets is overlapping enemy hitboxes.
   * Whenever overlapping happens to the player loses 1 life and gets immunity frames for 3 seconds.
   * Whenever overlapping happens to the enemy, subtract their hp by dmg value of bullet.
   * Idea: use grid for whole field (cell dimension same size as smallest sprite) where two for loops loop
   * through the grid until a cell containing atleast one sprite is found, check all neighbouring cells for sprites
   * and compare these with eachother. might make comparisons faster when number of bullets gets up in the 1000s.
   */
  private boolean checkHitboxCollision() {



    return true;
  }

  @Override
  public Iterator<Bullets> iterator() {
    return this.bulletsOnField.iterator();
  }
  
  
}
