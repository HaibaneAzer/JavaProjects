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
import no.uib.inf101.sem2.model.danmakus.SpriteType;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel, ControllableDanmakuModel{
  
  private DanmakuField Field;
  private final DanmakuFactory getSprite;
  private Player currentPlayer;
  private List<Bullets> playerBullets = new ArrayList<Bullets>(); // number of bullets player shoots at the same time.
  private int playerFireCounter = 0;
  private List<Enemies> currentEnemies = new ArrayList<Enemies>(); // number of enemies spawned per wave)
  private List<Enemies> TotalEnemies = new ArrayList<Enemies>(); // total enemies per stage.
  private int currentStage;
  private int currentWave;
  private boolean createWaves = true;
  private List<Bullets> enemyBullets = new ArrayList<Bullets>(); // number of bullets enemy shoots at the same time.
  private List<Bullets> bulletsOnField = new ArrayList<Bullets>(); // total bullets from both player and enemies.
  private double FPSCounter = 60.0;
  
  public DanmakuModel(DanmakuField Field, DanmakuFactory getSprite) {
    this.Field = Field;
    this.getSprite = getSprite;
    this.currentStage = 1;
    this.currentWave = 1;
    // change how enemies are spawned given stage. 
    this.currentPlayer = getSprite.getNewPlayer("P1c").shiftedToStartPoint(Field);
    this.TotalEnemies = getSprite.getTotalEnemies(this.currentStage);
    
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
  public Iterable<Enemies> getEnemiesOnField() {
    return this.currentEnemies;
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
      // add 3 + N bullets per shot, where N is determined by player power
      // power >= 2 -> N = 1, power >= 3 -> N = 2, power >= 4 -> N = 3
      for (int i = 0; i < 3; i++) {
        Bullets newBullet = this.getSprite.getNewBullet("arrow");
        newBullet.setBulletOwner(SpriteType.PlayerBullet);
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
  
  /**
   * insideField is a helper method for {@link #movePlayer}, which is used to check if player is trying to move out the field.
   * 
   */
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
    
    // placeholder function for enemy movement pattern.
    // NB: update or change completely when making advanced movement patterns.
    /* for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
      this.currentEnemies.set(i, this.currentEnemies.get(i).rotateAxisBy(theta));
    } */
    return true;
    
  }

  @Override
  public void moveEnemiesInWaves() {
   
    List<String> movements = new ArrayList<>();
    movements.add("aim"); // move in direction of aim
    movements.add("cosine"); 
    movements.add("horizontal");
    movements.add("downwards");
    movements.add("circleTurn");
    movements.add("reverse"); // repeat any movement backwards
    movements.add("stop"); // stop current movement path.

    // get 3 or 1 enemies from total and put them in one wave (extend later to 8 per wave)
    if (this.createWaves) {
      // ................
      if (this.TotalEnemies.size() > 2) { 
        for (int i = this.TotalEnemies.size() - 1;i >= 0;i--) {
          if (this.TotalEnemies.get(i).getVariation().equals("monster1")) {
            this.currentEnemies.add(this.TotalEnemies.get(i));
            this.TotalEnemies.remove(i);
            if (this.currentEnemies.size() == 3) {
              break;
            }
          }
        }
      }
      else if (!this.TotalEnemies.isEmpty()) {
        Enemies enemy = findEnemyVariation("monster2");
        if (enemy.equals(null)) {
          enemy = findEnemyVariation("monster1");
        }
        this.currentEnemies.add(enemy);
        this.TotalEnemies.remove(this.TotalEnemies.indexOf(enemy));
      }
      // ..................
      this.createWaves = false;
      System.out.println("repeat: ");
      setSpawnWaveEnemies(this.currentWave);
    }
    if (this.currentWave == 0) {
      this.createWaves = true;
    }
    // start wave
    // spawn first enemy in list and set movement. 
    
    // wait T ticks (based on controller timer) before spawning next enemy in list.
    // when wave list is empty, move to next wave.
    setMovementWaveEnemies(""); // not made yet
    // basic movement complete. need enemies to move seperately and not at the same time.
    // maybe change setspawnwaveenemies?
    for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
      Enemies enemy = this.currentEnemies.get(i);
      enemy.setVelocity(new Vector(0, 3, 1));
      double newY = enemy.getPosition().y() + 0.3*enemy.getVelocity().length();
      double newX = 80*Math.sin(0.01*enemy.getPosition().y()) + 120;
      Enemies displacedEnemy = enemy.setNewPosition(new Vector(newX, newY, 1));
      System.out.println("new pos: " + enemy.getPosition() + " for " + i);
      this.currentEnemies.set(i, displacedEnemy);
      
    }

  }

  /**
   * findEnemyVariation goes through the total list of enemies and retrieves only the enemy of a spesific variation.
   * Code snippet from stackoverflow by oleg.cherednik.
   * link: https://stackoverflow.com/questions/17526608/how-to-find-an-object-in-an-arraylist-by-property.
   */
  private Enemies findEnemyVariation(String variation) {
    return this.TotalEnemies.stream().filter(enemy -> variation.equals(enemy.getVariation())).findFirst().orElse(null);
  }

  /**
   * setSpawnWaveEnemies is a method that contains preset spawns positions for enemies depending on wave number, stage
   * and list of wave enemies.
   * helper method for moveEnemiesInWaves.
   */
  private void setSpawnWaveEnemies(int wave) {
  
    if (wave == 1 && this.currentStage == 1) {
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // spawn left from top center
        this.currentEnemies.set(i, this.currentEnemies.get(i).shiftedToStartPoint(Field).displaceBy(new Vector(1, 30, 1)));
      }
    }
    else if (wave == 2 && this.currentStage == 1) {
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // spawn right from top center
        this.currentEnemies.set(i, this.currentEnemies.get(i).shiftedToStartPoint(Field).displaceBy(new Vector(50, 0, 1)));
      }
    }
    else if (wave == 3 && this.currentStage == 1) {
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // spawn left from top center
        this.currentEnemies.set(i, this.currentEnemies.get(i).shiftedToStartPoint(Field).displaceBy(new Vector(-50, 0, 1)));
      }
    }
    else if (wave == 4 && this.currentStage == 1) {
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // spawn at top center
        this.currentEnemies.set(i, this.currentEnemies.get(i).shiftedToStartPoint(Field));
      }
    }
    else if (wave == 1 && this.currentStage == 2) {
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // spawn left from top center
        this.currentEnemies.set(i, this.currentEnemies.get(i).shiftedToStartPoint(Field));
      }
    }

  }

  /**
   * setMovementWaveEnemies is method that contains preset movement patterns for enemies depending on enemy variation
   */
  private void setMovementWaveEnemies(String enemyVariation) {
    
  }

  @Override
  public void moveAllBullets() {
    
    for (int i = this.bulletsOnField.size() - 1; i >= 0; i--) {
      Bullets bullet = this.bulletsOnField.get(i);
      if (!bulletInsideScreen(bullet.displaceBy(bullet.getVelocity()))) {
        // vanish bullet when outside field
        this.bulletsOnField.remove(i);
      }
      else {
        this.bulletsOnField.set(i, bullet.displaceBy(bullet.getVelocity()));
      }
    }

  }

  /**
   * bulletInsideScreen is a helper method for {@link #moveAllBullets} checks if bullet is within screen bounds + bullet diameter from bounds. 
   * bound is larger than screen to make bullet vanishing more seemless. Continues to {@link #checkBulletCollision}.
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
    // no bullets outside? check bullet collision
    return checkBulletCollision(shiftedBullet);
  }

  /**
   * checkPlayerBulletCollision is a helper method used by {@link #bulletInsideScreen}.
   * checks if the hitboxes of player/enemy overlap bullets from enemy/player.
   * Idea: use grid for whole field (cell dimension same size as smallest sprite) where two for loops loop
   * through the grid until a cell containing atleast one sprite is found, check all neighbouring cells for sprites
   * and compare these with eachother. might make comparisons faster when number of bullets gets up in the 1000s.
   */
  private boolean checkBulletCollision(Bullets shiftedBullet) {
    // player bullet hit enemy
    if (shiftedBullet.getType().equals(SpriteType.PlayerBullet)) { 
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // collision formula: |P1 - P2| < r1 + r2, where r is radius and P is position vectors
        Enemies enemy = this.currentEnemies.get(i);
        if (shiftedBullet.getPosition().subVect(enemy.getPosition()).length() < shiftedBullet.getRadius() + enemy.getRadius()) {
          enemy.attackEnemy(shiftedBullet.getDamage());
          this.currentEnemies.set(i, enemy);
          if (!enemy.isAlive()) {
            this.currentEnemies.remove(i);
          }
          return false;
        }
        
      }
    }
    // enemy bullet hit player
    else if (shiftedBullet.getType().equals(SpriteType.EnemyBullet)) {
      
      if (shiftedBullet.getPosition().subVect(this.currentPlayer.getPosition()).length() < shiftedBullet.getRadius() + this.currentPlayer.getRadius()) {
        this.currentPlayer.killPlayer();
        if (!this.currentPlayer.isAlive()) {
          this.currentPlayer = null; // NB: improve despawning player when game over
        }
        this.currentPlayer = getSprite.getNewPlayer(this.currentPlayer.getVariation()).shiftedToStartPoint(Field);
        return false;
      }
    }

    return true;
  }

  
}
