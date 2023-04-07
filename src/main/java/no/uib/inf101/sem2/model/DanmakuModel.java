package no.uib.inf101.sem2.model;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf101.sem2.controller.ControllableDanmakuModel;
import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel, ControllableDanmakuModel{
  
  private DanmakuField Field;
  private final DanmakuFactory getSprite;
  private Player currentPlayer;
  private List<Bullets> playerBullets = new ArrayList<Bullets>(); // number of bullets player shoots at the same time.
  private int playerFireCounter = 0;
  private Enemies currentEnemy;
  private List<Bullets> enemyBullet = new ArrayList<Bullets>(); // number of bullets enemy shoots at the same time.
  private List<Bullets> bulletsOnField = new ArrayList<Bullets>(); // total bullets from both player and enemies.
  private double FPSCounter = 60.0;
  
  public DanmakuModel(DanmakuField Field, DanmakuFactory getSprite) {
    this.Field = Field;
    this.getSprite = getSprite;
    // change how enemies are spawned given stage. 
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
  public void playerFire(int fireRate, boolean holdingShift) {
    if (this.playerFireCounter == 0) {
      // determine shooting pattern
      playerShootingPatterns(this.currentPlayer.getVariation(), holdingShift);
      // add current bullet to bullet list.
      for (Bullets bullet : this.playerBullets) {
        this.bulletsOnField.add(bullet);
      }
      this.playerBullets.clear();
      
    }
    this.playerFireCounter = (this.playerFireCounter + 1) % fireRate;
  }

  /**
   * playerShootingPatterns is a helper method for {@link #playerFire} which contains all patterns that the player can shoot with. 
   * each pattern also changes whenever shift-key is held (turning default spread shot to focused shot).
   * number of bullets returned depends on pattern type (given by player variation), player power and wether shots are focused or not.
   * @param variation is a spesific character. Used to determine shooting pattern.
   * @return number of bullets being shot at the same time.
   */
  private List<Bullets> playerShootingPatterns(String variation, boolean holdingShift) {
    // triple shot:
    if (variation.equals("P1c")) {
      int spacedBy = 30; // distance between bullets spawn points
      // check for focused mode
      if (holdingShift) {
        spacedBy = 15;
      }
      Vector displaceFromDefaultSpawn = new Vector(-spacedBy, 0, 1); 
      // add 3 bullets per shot
      for (int i = 0; i < 3; i++) {
        Bullets newBullet = this.getSprite.getNewBullet("arrow");
        Vector bulletRadius = new Vector(newBullet.getRadius(), 0, 1);
        // set bullets default spawnpoint
        Vector spawnPoint = this.currentPlayer.getPosition().addVect(this.currentPlayer.getAimVector()).addVect(displaceFromDefaultSpawn);
        spawnPoint = spawnPoint.addVect(bulletRadius);
        newBullet = newBullet.displaceBy(spawnPoint);
        // set bullet speed to players aim
        newBullet.updateBulletVelocity(this.currentPlayer.getAimVector());
        // update bullets direction to it's velocity
        newBullet.updateBulletDirection(newBullet.getVelocity());
        this.playerBullets.add(i, newBullet);
        // set spawnpoint for next bullet
        displaceFromDefaultSpawn = displaceFromDefaultSpawn.addVect(new Vector(spacedBy, 0, 1));
      }    
      
    }
    // homing shot
    else if (variation.equals("P2c")) {

    }
    return this.playerBullets;
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
    
    // NB: update or change completely when making advanced movement patterns.
    this.currentEnemy = this.currentEnemy.rotateAxisBy(theta);
    return true;
    
  }

  @Override
  public void moveAllBullets() {
    
    for (int i = this.bulletsOnField.size() - 1; i >= 0; i--) {
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
   * bulletInsideScreen checks if bullet is within screen bounds + bullet diameter from bounds. 
   * bound is larger than screen to make bullet vanishing more seemless.
   * @param shiftedBullet is the shifted position of bullet being checked.
   * @return true if bullet is still inside screen boundary.
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
   * checkHitboxCollision is a helper method used by {@link #bulletInsideScreen} (or implemented seperately?).
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

  
}
