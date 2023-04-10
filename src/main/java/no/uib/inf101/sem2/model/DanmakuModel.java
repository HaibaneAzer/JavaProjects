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
  private GameState gameState;
  private Player currentPlayer;
  private List<Bullets> playerBullets = new ArrayList<Bullets>(); // number of bullets player shoots at the same time.
  private int playerFireDelay;
  private List<Enemies> currentEnemies = new ArrayList<Enemies>(); // number of enemies spawned per wave)
  private List<List<Enemies>> TotalEnemies = new ArrayList<List<Enemies>>(); // total enemies per stage.
  // handle waves and stages
  private int currentStage;
  private int currentWaveIndex;
  private final int WaveMaxInterval = 200; // time between each wave
  private int waveDelay;
  // setSpawnWaveEnemies method: 
  private final int spawnEnemyInterval = 100; // enemy spawn rate
  private int spawnEnemyTimer;
  private int nextEnemyIndex;
  /* private List<Bullets> enemyBullets = new ArrayList<Bullets>(); */ // number of bullets enemy shoots at the same time.
  private List<Bullets> bulletsOnField = new ArrayList<Bullets>(); // total bullets from both player and enemies.
  private double FPSCounter = 60.0;
  
  public DanmakuModel(DanmakuField Field, DanmakuFactory getSprite) {
    this.Field = Field;
    this.getSprite = getSprite;
    this.gameState = GameState.GAME_MENU;
    // handle waves and stages
    this.currentStage = 1;
    this.currentWaveIndex = 0;
    this.waveDelay = 0;
    // player stuff:
    this.currentPlayer = getSprite.getNewPlayer("P1c").shiftedToStartPoint(Field);
    this.playerFireDelay = 0;
    // enemies stuff:
    this.TotalEnemies = getSprite.getTotalEnemies(this.currentStage);
    this.spawnEnemyTimer = spawnEnemyInterval; 
    this.nextEnemyIndex = 0;
    
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
  public int getCurrentStage() {
    return this.currentStage;
  }

  @Override
  public void setFPSValue(double newFPS) {
    this.FPSCounter = newFPS;
  }

  @Override
  public GameState getGameState() {
    return this.gameState;
  }

  @Override
  public void setGameState(GameState newState) {
    this.gameState = newState;
  }

  /* ################################################ */
  /* #################### Player #################### */
  /* ################################################ */

  @Override
  public void playerFire(int fireRate, boolean holdingShift) {
    if (this.playerFireDelay == 0) {
      // determine shooting pattern
      playerShootingPatterns(this.currentPlayer.getVariation(), holdingShift);
      // add current bullet to bullet list.
      for (Bullets bullet : this.playerBullets) {
        this.bulletsOnField.add(bullet);
      }
      this.playerBullets.clear();
      
    }
    this.playerFireDelay = (this.playerFireDelay + 1) % fireRate;
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
  public boolean movePlayer(Vector targetVel) {
    
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

  /* ############################################### */
  /* #################### Enemy #################### */
  /* ############################################### */

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

    // game won when all stages complete
    if (this.currentStage < 3) {
      // get 3 or 1 enemies from total and put them in one wave (extend later to 8 per wave)
      if (this.spawnEnemyTimer >= this.spawnEnemyInterval && this.waveDelay == 0) {
        this.currentEnemies.add(this.TotalEnemies.get(this.currentWaveIndex).get(this.nextEnemyIndex));
        setSpawnWaveEnemies(this.currentWaveIndex);
        this.nextEnemyIndex++;
        this.spawnEnemyTimer = 0;
      }
      this.spawnEnemyTimer = (this.spawnEnemyTimer + 1) % (this.spawnEnemyInterval + 1);
      // when all enemies has spawned, update wave.
      if (this.nextEnemyIndex > this.TotalEnemies.get(this.currentWaveIndex).size() - 1) {
        // delay next wave
        if (this.waveDelay <= this.WaveMaxInterval) {
          this.waveDelay++;
        }
        else {
          this.nextEnemyIndex = 0;  
          this.waveDelay = 0;
          this.spawnEnemyTimer = this.spawnEnemyInterval;
        }
        // move to next wave
        if (this.currentWaveIndex < this.TotalEnemies.size() - 1 && this.waveDelay == 0) {
          this.currentWaveIndex++; 
        }
        else if (this.waveDelay == 0) {
          this.currentWaveIndex = 0; // NB: change when adding stages.
          this.currentStage++;
        }
      }
    }
      // basic movement complete.
      // NB: make method containing a set of different enemy movements. input movement type (string)
      // for given movement to enemy.
      moveAllEnemies();
    
    
  }

  /**
   * findEnemyVariation goes through the total list of enemies and retrieves only the enemy of a spesific variation.
   * Code snippet from stackoverflow by oleg.cherednik.
   * link: https://stackoverflow.com/questions/17526608/how-to-find-an-object-in-an-arraylist-by-property.
   */
  /* private Enemies findEnemyVariation(String variation, int currentWave) {
    return this.TotalEnemies.get(currentWave).stream().filter(enemy -> variation.equals(enemy.getVariation())).findFirst().orElse(null);
  } */

  /**
   * setSpawnWaveEnemies is a method that contains preset spawns positions for enemies depending on wave number, stage
   * and list of wave enemies.
   * helper method for moveEnemiesInWaves.
   */
  private void setSpawnWaveEnemies(int wave) {
    // note: make spawnpoint dependent on enemy variation?
    int nextSpawnIndex = this.currentEnemies.size() - 1;
    // set spawns: 
    if (wave == 0 && this.currentStage == 1) {
      // spawn left from top center
      System.out.println("wave 1");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field).displaceBy(new Vector(1, 30, 1)));
    }
    else if (wave == 1 && this.currentStage == 1) {
      // spawn right from top center
      System.out.println("wave 2");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field).displaceBy(new Vector(50, 0, 1)));
    }
    else if (wave == 2 && this.currentStage == 1) {
      // spawn left from top center
      System.out.println("wave 3");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field).displaceBy(new Vector(-50, 0, 1)));
    }
    else if (wave == 3 && this.currentStage == 1) {
      // spawn at top center
      System.out.println("wave 4");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field));
    }
    else if (wave == 0 && this.currentStage == 2) {
      // spawn left from top center
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field)).displaceBy(new Vector(-100, 0, 1));
    }
    else if (wave == 1 && this.currentStage == 2) {
      // spawn left from top center
      System.out.println("wave 3");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field).displaceBy(new Vector(100, 0, 1)));
    }
    else if (wave == 2 && this.currentStage == 2) {
      // spawn at top center
      System.out.println("wave 4");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field)).displaceBy(new Vector(-100, 0, 1));
    }
    else if (wave == 3 && this.currentStage == 2) {
      // spawn at top center
      System.out.println("wave 4");
      this.currentEnemies.set(nextSpawnIndex, this.currentEnemies.get(nextSpawnIndex).shiftedToStartPoint(Field)).displaceBy(new Vector(100, 0, 1));;
    }

  }

  /**
   * moveALLEnemies is helper method that contains preset movement patterns for enemies. Used by {@link #moveEnemiesInWaves}
   * to move all spawned enemies.
   */
  private void moveAllEnemies() {
    // NB: make "controll pattern by enemy variation" system...
    for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
      // move in sine wave pattern
      Enemies displacedEnemy;
      Enemies enemy = this.currentEnemies.get(i);
      if (enemy.getVariation().equals("monster1")) {
        enemy.setVelocity(new Vector(0, 3, 1));
        double newY = enemy.getPosition().y() + 0.5*enemy.getVelocity().length();
        double newX = 80*Math.sin(0.01*enemy.getPosition().y()) + 120;
        displacedEnemy = enemy.setNewPosition(new Vector(newX, newY, 1));
      }
      // move downwards
      else {
        enemy.setVelocity(new Vector(0, 1, 1));
        displacedEnemy = enemy.displaceBy(enemy.getVelocity());
      }
      // eliminate enemy when they're outside screen.
      if (!enemyInsideScreen(enemy)) {
        this.currentEnemies.remove(i);
        if (this.currentEnemies.isEmpty()) {
          this.currentWaveIndex++;
        }
      }
      else {
        this.currentEnemies.set(i, displacedEnemy);  
      }
      
    } 

  }

  /**
   * enemyInsideScreen is a helper method for {@link #moveEnemiesInWaves}. Checks if enemy is within screen bounds + enemy diameter from bounds. 
   * bound is larger than screen to make enemy vanishing more seemless.
   * @param shiftedEnemy is the shifted position of enemy being checked.
   * @return true if enemy is still inside screen bounds.
   */
  private boolean enemyInsideScreen(Enemies shiftedEnemy) {
    if (!(shiftedEnemy.getPosition().x() + 2*shiftedEnemy.getRadius() >= 0
       && shiftedEnemy.getPosition().y() + 2*shiftedEnemy.getRadius() >= 0
       && shiftedEnemy.getPosition().x() - 2*shiftedEnemy.getRadius() < 2*this.Field.getFieldX() + this.Field.width()
       && shiftedEnemy.getPosition().y() - 2*shiftedEnemy.getRadius() < 2*this.Field.getFieldY() + this.Field.height())) {
        return false;
    }
    return true;
  }

  /* ############################################### */
  /* ################### Bullets ################### */
  /* ############################################### */

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
    boolean withinScreen = (
      shiftedBullet.getPosition().x() + 2*shiftedBullet.getRadius() >= 0 &&
      shiftedBullet.getPosition().y() + 2*shiftedBullet.getRadius() >= 0 &&
      shiftedBullet.getPosition().x() - 2*shiftedBullet.getRadius() < 2*this.Field.getFieldX() + this.Field.width() &&
      shiftedBullet.getPosition().y() - 2*shiftedBullet.getRadius() < 2*this.Field.getFieldY() + this.Field.height()
    );
    if (!withinScreen) {
      return false;
    }
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
