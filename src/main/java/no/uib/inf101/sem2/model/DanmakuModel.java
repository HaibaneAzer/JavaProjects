package no.uib.inf101.sem2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf101.sem2.controller.ControllableDanmakuModel;
import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteState;
import no.uib.inf101.sem2.model.danmakus.SpriteType;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel, ControllableDanmakuModel{
  
  private DanmakuField Field;
  private final DanmakuFactory getSprite;
  private GameState gameState;
  private final BulletPattern patterns;
  // player variables
  private Player currentPlayer;
  private List<Bullets> playerBullets = new ArrayList<Bullets>(); // number of bullets player shoots at the same time.
  private int playerFireDelay;
  // enemy variables
  private List<Enemies> currentEnemies = new ArrayList<Enemies>(); // number of enemies spawned per wave
  private List<List<Enemies>> TotalEnemies = new ArrayList<List<Enemies>>(); // total enemies per stage.
  // boss variables
  private boolean bossBattle; // false by default, true when all waves has finished for each stage.
  private boolean stillSpawning;
  private Enemies currentBoss;
  private boolean attackType;
  private int bossWaitTimer;
  private final int maxWaitInterval = 150; // max wait time before next movement.
  private int bossAttackTimer;
  private final int maxAttackInterval = 225; // time before switching attack type.
  private int bossAttackReloader;
  private final int maxReloadInterval = 150; // time between each attack barrage.
  // handle waves and stages
  private int currentStage;
  private final int stageMaxInterval = 450; // time between each stage
  private int stageDelay;
  private int currentWaveIndex;
  private final int WaveMaxInterval = 300; // time between each wave
  private int waveDelay;
  // setSpawnWaveEnemies method: 
  private final int spawnEnemyInterval = 50; // enemy spawn rate
  private int spawnEnemyTimer;
  private int nextEnemyIndex;
  private List<Bullets> enemyBullets = new ArrayList<Bullets>();
  private List<Bullets> bossBullets = new ArrayList<Bullets>(); 
  private List<Bullets> bulletsOnField = new ArrayList<Bullets>(); // total bullets from both player and enemies.
  private double FPSCounter;
  // score
  private int score;

  /**
   * DanmakuModel is main constructor for handling all ingame data and interactions
   */
  public DanmakuModel(DanmakuField Field, DanmakuFactory getSprite) {
    this.Field = Field;
    this.getSprite = getSprite;
    this.patterns = new BulletPattern(this.getSprite);
    this.gameState = GameState.GAME_MENU;
    this.FPSCounter = 0.0;
    // handle waves and stages
    this.currentStage = 1;
    this.currentWaveIndex = 0;
    this.waveDelay = 0;
    this.stageDelay = 0;
    // player stuff:
    // default player
    this.currentPlayer = getSprite.getNewPlayer(SpriteVariations.player2).shiftedToStartPoint(Field);
    this.playerFireDelay = 0;
    // enemies stuff:
    this.TotalEnemies = getSprite.getTotalEnemies(this.currentStage);
    this.spawnEnemyTimer = 0; 
    this.nextEnemyIndex = 0;
    // boss stuff:
    this.bossBattle = false;
    this.attackType = false;
    this.stillSpawning = true;
    this.currentBoss = null;
    this.bossWaitTimer = 0;
    this.bossAttackTimer = 0;
    this.bossAttackReloader = 0;
    // score
    this.score = 0;
    // load spawn
    loadEnemySpawnpoints(this.Field, this.TotalEnemies);
    loadBossList();
    
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
  public Enemies getBossEnemyOnField() {
    return this.currentBoss;
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

  @Override
  public boolean getBossAttackType() {
    return this.attackType;
  }

  @Override
  public int getCurrentScore() {
    return this.score;
  }

  @Override 
  public void resetField() {
    this.currentEnemies.clear();
    this.TotalEnemies.clear();
    this.bulletsOnField.clear();
    // handle waves and stages
    this.currentStage = 1;
    this.currentWaveIndex = 0;
    this.waveDelay = 0;
    this.stageDelay = 0;
    // player stuff:
    this.currentPlayer = getSprite.getNewPlayer(this.currentPlayer.getVariation()).shiftedToStartPoint(Field);
    this.playerFireDelay = 0;
    // enemies stuff:
    this.TotalEnemies = getSprite.getTotalEnemies(this.currentStage);
    this.spawnEnemyTimer = 0; 
    this.nextEnemyIndex = 0;
    // boss stuff: 
    this.bossBattle = false;
    this.stillSpawning = true;
    this.currentBoss = null;
    this.bossWaitTimer = 0;
    this.bossAttackTimer = 0;
    this.bossAttackReloader = 0;
    // score
    this.score = 0;
  }

  @Override
  public void SelectPlayer(SpriteVariations variations) {
    this.currentPlayer = getSprite.getNewPlayer(variations).shiftedToStartPoint(Field);
  }

  private static Map<Integer, Map<EnemySpawnPos, Map<Integer, Vector>>> SpawnStage = new HashMap<>();
  private static Map<EnemySpawnPos, Map<Integer, Vector>> SpawnSet = new HashMap<>();
  private static Map<Integer, Vector> enemiesPerWave = new HashMap<>();
  /**
   * loadEnemySpawnpoints loads all possible spawn positions given by a Vector,
   * depending on wave number and stage.
   * spawnpoint format (integer stage, integer set, Integer enemies per wave).
   */
  private static void loadEnemySpawnpoints(FieldDimension field, List<List<Enemies>> waveEnemies) {
    int fieldX = field.getFieldX();
    int fieldWidth = field.width();
    
    // stage 1
    double x = fieldX + fieldWidth*(0.1);
    // spawn enemies in horisontal line
    for (int i = 0; i < waveEnemies.get(0).size(); i++) {
      enemiesPerWave.put(i, new Vector(x, 0, 1));
      if (x < fieldX + fieldWidth*(0.8)) {
        x += fieldWidth*(0.1);
      }
    }
    SpawnSet.put(EnemySpawnPos.horisontal, enemiesPerWave);

    // spawn enemies in vertical line
    // far right
    enemiesPerWave = new HashMap<>();
    x = fieldX + fieldWidth*(0.2);
    for (int i = 0; i < waveEnemies.get(1).size(); i++) {
      enemiesPerWave.put(i, new Vector(x, 0, 1));
      
    }
    SpawnSet.put(EnemySpawnPos.verticalRight, enemiesPerWave);

    // far left
    enemiesPerWave = new HashMap<>();
    x = fieldX + fieldWidth*(0.8);
    for (int i = 0; i < waveEnemies.get(2).size(); i++) {
      enemiesPerWave.put(i, new Vector(x, 0, 1));
    }
    SpawnSet.put(EnemySpawnPos.verticalLeft, enemiesPerWave);
    
    // middle right
    enemiesPerWave = new HashMap<>();
    x = fieldX + fieldWidth*(0.4);
    for (int i = 0; i < waveEnemies.get(2).size(); i++) {
      enemiesPerWave.put(i, new Vector(x, 0, 1));
    }
    SpawnSet.put(EnemySpawnPos.verticalMiddleR, enemiesPerWave);
    
    // middle left
    enemiesPerWave = new HashMap<>();
    x = fieldX + fieldWidth*(0.6);
    for (int i = 0; i < waveEnemies.get(2).size(); i++) {
      enemiesPerWave.put(i, new Vector(x, 0, 1));
    }
    SpawnSet.put(EnemySpawnPos.verticalMiddelL, enemiesPerWave);

    // spawn enemies from both sides simultaniously
    enemiesPerWave = new HashMap<>();
    x = fieldX + fieldWidth*(0.1);
    boolean switcher = true;
    for (int i = 0; i < waveEnemies.get(2).size(); i++) {
      enemiesPerWave.put(i, new Vector(x, 0, 1));
      if (switcher) {
        switcher = false;
        x = fieldX + fieldWidth*(0.9) - (0.05)*fieldWidth*i;
      }
      else {
        switcher = true;
        x = fieldX + fieldWidth*(0.1) + (0.05)*fieldWidth*i;
      }

    }
    SpawnSet.put(EnemySpawnPos.zigzag, enemiesPerWave);
    SpawnStage.put(1, SpawnSet);

  }

  /**
   * getBulletImage gets image for bullet using variation and ownership to determine 
   * the image.
   */
  private static Vector getEnemySpawn(Integer Stage, EnemySpawnPos pos, Integer num) {
    return SpawnStage.get(Stage).get(pos).get(num);
  }

  private static Map<Integer, SpriteVariations> bossList = new HashMap<>();
  /**
   * loadBossList is a list of bosses given their corrosponding stage.
   */
  private static void loadBossList() {
    bossList.put(1, SpriteVariations.boss4);
    bossList.put(2, SpriteVariations.boss5);
  }

  private static SpriteVariations getBossList(Integer stage) {
    return bossList.get(stage);
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
   * number of bullets returned depends on pattern type (given by player variation), 
   * player power and wether shots are focused or not.
   * @param variation is a spesific character. Used to determine shooting pattern.
   * @return number of bullets being shot at the same time.
   */
  private List<Bullets> playerShootingPatterns(SpriteVariations variation, boolean holdingShift) {
    // triple shot:
    if (variation.equals(SpriteVariations.player1)) {
      this.playerBullets = this.patterns.playerShoot(this.currentPlayer, holdingShift);
    }
    // homing shot
    else if (variation.equals(SpriteVariations.player2)) {
      this.playerBullets = this.patterns.playerShoot(this.currentPlayer, holdingShift);
    }
    return this.playerBullets;
  }
  
  @Override
  public boolean movePlayer(Vector targetVel) {
    
    Vector displacement = targetVel;
    this.currentPlayer.setVelocity(displacement);
    if (!insideField(this.currentPlayer.displaceBy(this.currentPlayer.getVelocity()))) {
      return false;
    }
    this.currentPlayer = this.currentPlayer.displaceBy(this.currentPlayer.getVelocity());
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
    if (!checkEnemyCollision(shiftedplayer)) {
      return false;
    }
    return true;

  }

  /**
   * checkEnemyCollision is a helper method for {@link #insideField}, which is used to check if player hitbox
   * overlaps enemy hitbox or boss hitbox.
   */
  private boolean checkEnemyCollision(Player shiftedPlayer) {
    for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
      if (shiftedPlayer.getPosition().subVect(this.currentEnemies.get(i).getPosition()).length() < shiftedPlayer.getRadius() + this.currentEnemies.get(i).getRadius()) {
        // kill and respawn player if lives left
        if (this.currentPlayer.isAlive()) {
          this.currentPlayer = this.currentPlayer.respawnPlayer(this.currentPlayer.getLives() - 1).shiftedToStartPoint(Field);
        }
        if (!this.currentPlayer.isAlive()) {
          this.gameState = GameState.GAME_OVER;
        }
        // kill enemy
        this.currentEnemies.remove(i);
        return false;
      }
    }
    if (this.currentBoss != null) {
      if (shiftedPlayer.getPosition().subVect(this.currentBoss.getPosition()).length() < shiftedPlayer.getRadius() + this.currentBoss.getRadius()) {
        // kill and respawn player if lives left
        if (this.currentPlayer.isAlive()) {
          this.currentPlayer = this.currentPlayer.respawnPlayer(this.currentPlayer.getLives() - 1).shiftedToStartPoint(Field);
        }
        if (!this.currentPlayer.isAlive()) {
          this.gameState = GameState.GAME_OVER;
        }
        // dmg boss (suicide dmg fixed at 300)
        this.currentBoss.attackEnemy(300);
        return false;
      }
    }
    return true;
  }

  /* ##################################################### */
  /* #################### Boss Battle #################### */
  /* ##################################################### */

  /**
   * runBossBattle is the main method used to controll all of the bosses actions.
   */
  private void runBossBattle() {
    // spawn and move boss to starting position
    if (stillSpawning) {
      spawnAndMoveToStart();
    }
    // start movement cycle (left and right) and start shooting (start boss battle timer)
    else {
      // if alive, boss moves and shoots
      if (this.currentBoss != null) {
        // wait
        if (this.bossWaitTimer <= this.maxWaitInterval) {
          this.bossWaitTimer++;
        }
        // move left and right
        else {
          // stop when nearing edges of field
          if (this.currentBoss.getPosition().x() >= Field.getFieldX() + Field.width()*(0.75) ||
              this.currentBoss.getPosition().x() <= Field.getFieldX() + Field.width()*(0.25)) {
            this.currentBoss.setVelocity(this.currentBoss.getVelocity().multiplyScalar(-1));
            this.bossWaitTimer = 0;
            this.currentBoss = this.currentBoss.displaceBy(this.currentBoss.getVelocity());
          }
          // stop whenever crossing the middle
          else if (Math.abs(this.currentBoss.getPosition().x() - (Field.getFieldX() + Field.width()*(0.5))) <= 0.1) {
            this.bossWaitTimer = 0;
            this.currentBoss = this.currentBoss.displaceBy(this.currentBoss.getVelocity());
          }
          else {
            this.currentBoss = this.currentBoss.displaceBy(this.currentBoss.getVelocity());
          }
        }
        bossFire();
      }
      // boss dead? stop battle and continue to next stage
      else {
        this.bossBattle = false;
        this.stillSpawning = true;
        this.bossAttackTimer = 0;
        this.bossAttackReloader = 0;
        this.currentStage++;
      }
    }
  }

  /**
   * spawnAndMoveToStart is used at end of each stage when summoning the next boss.
   */
  private void spawnAndMoveToStart() {
    
    // boss not spawned? spawn and continue to movement
    if (this.currentBoss == null) {
      // boss depends on stage
      this.currentBoss = getSprite.getNewEnemy(getBossList(this.currentStage)).shiftedToStartPoint(Field);
      this.currentBoss.setVelocity(new Vector(0, 1, 1));
      this.stillSpawning = true;
    }
    // still moving? return true
    else if (this.currentBoss.getPosition().y() <= Field.getFieldY() + Field.height()*(0.15)) {
      this.currentBoss = this.currentBoss.displaceBy(this.currentBoss.getVelocity());
      this.stillSpawning = true;
    }
    // reached starting position? return false.
    else {
      this.currentBoss.setVelocity(new Vector(2, 0, 1));
      this.stillSpawning = false;
    }
  }

  /**
   * bossFire uses 2 timers to controll when boss is shooting or reloading and when boss uses normal or super attack.
   */
  private void bossFire() {
    // reload timer
    if (this.bossAttackReloader < this.maxReloadInterval) {
      // timer for swapping attack types
      if (bossAttackTimer < 4*this.maxAttackInterval) {
        this.attackType = false;
        this.bossAttackTimer++;
      }
      else if (bossAttackTimer < 6*this.maxAttackInterval) {
        this.attackType = true;
        this.bossAttackTimer++;
      }
      else {
        this.bossAttackTimer = 0;
      }
      // attack
      if (this.bossAttackReloader < this.maxReloadInterval) {
        bossAttackPatterns(this.attackType);
        for (Bullets bullet : this.bossBullets) {
          this.bulletsOnField.add(bullet);
        }
        this.bossBullets.clear();
        this.bossAttackReloader++;
      }
    }
    else if (this.bossAttackReloader < 1.5*this.maxReloadInterval) {
      this.bossAttackReloader++;
    }
    else {
      this.bossAttackReloader = 0;
    }
  }

  /** 
   * bossAttackPatterns handles boss normal and super attack with timers.
   */
  private void bossAttackPatterns(boolean unleashSuper) {
    this.bossBullets = this.patterns.bossShoot(this.currentBoss, unleashSuper);
    // shorter delay with super
    if (unleashSuper) {
      this.currentBoss = this.currentBoss.setFireTimer((this.currentBoss.getFireTimer() + 1) % (int) Math.round(0.3*this.currentBoss.getFireDelay()));
    }
    else {
      this.currentBoss = this.currentBoss.setFireTimer((this.currentBoss.getFireTimer() + 1) % (int) Math.round(this.currentBoss.getFireDelay()));
    }
    
    
  }

  /* ############################################### */
  /* #################### Enemy #################### */
  /* ############################################### */

  @Override
  public void moveEnemiesInWaves() {
    // game won when all stages complete
    if (this.currentStage < 3) {
      if (this.stageDelay < this.stageMaxInterval) {
        if (this.bossBattle) {
          runBossBattle(); // continues until boss is dead
        }
        else {
          this.stageDelay++;
        }
      }
      else {
        // wait before next spawn
        this.TotalEnemies = getSprite.getTotalEnemies(this.currentStage);
        if (this.spawnEnemyTimer < this.spawnEnemyInterval) {
          this.spawnEnemyTimer++; 
        }
        // when all enemies has spawned, update wave.
        else if (this.nextEnemyIndex > this.TotalEnemies.get(this.currentWaveIndex).size() - 1) {  
          // delay next wave
          if (this.waveDelay < this.WaveMaxInterval) {
            this.waveDelay++;
          }
          else {
            this.nextEnemyIndex = 0;  
            this.waveDelay = 0;
            this.spawnEnemyTimer = this.spawnEnemyInterval;
            // move to next wave
            if (this.currentWaveIndex < this.TotalEnemies.size() - 1) {
              this.currentWaveIndex++; 
            }
            // if max wave number, start boss battle, then move to next stage
            else {
              this.currentWaveIndex = 0; 
              this.stageDelay = 0;
              this.bossBattle = true;
            }
          }
        }
        // spawn new enemy until all are spawned
        else {
          this.currentEnemies.add(this.TotalEnemies.get(this.currentWaveIndex).get(this.nextEnemyIndex));
          setSpawnWaveEnemies(this.currentWaveIndex);
          this.nextEnemyIndex++;
          this.spawnEnemyTimer = 0; 
        }
      }
    }
    moveAllEnemies(); 
  }

  /**
   * setSpawnWaveEnemies is a method that contains preset spawns positions for enemies depending on wave number, stage
   * and list of wave enemies.
   * helper method for {@link #moveEnemiesInWaves}.
   */
  private void setSpawnWaveEnemies(int wave) {
    // note: make spawnpoint dependent on enemy variation?
    int nextSpawnIndex = this.currentEnemies.size() - 1;
    // set spawns: 
    EnemySpawnPos setPos = EnemySpawnPos.horisontal;
    if (wave == 1) {
      setPos = EnemySpawnPos.verticalRight;
    }
    else if (wave == 2) {
      setPos = EnemySpawnPos.verticalLeft;
    }
    else if (wave == 3) {
      setPos = EnemySpawnPos.zigzag;
    }
    else if (wave == 4) {
      setPos = EnemySpawnPos.verticalMiddelL;
    }
    else if (wave == 5) {
      setPos = EnemySpawnPos.verticalMiddleR;
    }
    Enemies enemy = this.currentEnemies.get(nextSpawnIndex);
    Vector pos = getEnemySpawn(1, setPos, this.nextEnemyIndex);
    this.currentEnemies.set(nextSpawnIndex, enemy.setNewPosition(pos));

  }

  /**
   * moveALLEnemies is helper method that contains preset movement patterns for enemies. Used by {@link #moveEnemiesInWaves}
   * to move all spawned enemies.
   */
  private void moveAllEnemies() {
    for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
      // move downwards
      Enemies displacedEnemy;
      Enemies enemy = this.currentEnemies.get(i);
      if (enemy.getVariation().equals(SpriteVariations.yokai1)) {
        enemy.setVelocity(new Vector(0, 1, 1));
        displacedEnemy = enemy.displaceBy(enemy.getVelocity());
      }
      // move downwards
      else {
        enemy.setVelocity(new Vector(0, 0.5, 1));
        displacedEnemy = enemy.displaceBy(enemy.getVelocity());
      }
      // eliminate enemy when they're outside screen.
      if (!enemyInsideScreen(enemy)) {
        this.currentEnemies.remove(i);
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

  @Override
  public void enemyFire() {
    // determine shooting pattern
    enemyBulletPattern();
    // add current bullet to bullet list. 
    for (Bullets bullet : this.enemyBullets) {
      this.bulletsOnField.add(bullet);
    }
    this.enemyBullets.clear();  
  }
  
  private void enemyBulletPattern() {

    for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
      Enemies enemy = this.currentEnemies.get(i);
      // shoot only after passing 0.05 from top of screen
      if (enemy.getPosition().y() > this.Field.getFieldY() + 0.05*this.Field.height()) {
        this.enemyBullets.addAll(this.patterns.enemyShoot(enemy, this.currentPlayer));
        this.currentEnemies.set(i, enemy.setFireTimer((enemy.getFireTimer() + 1) % enemy.getFireDelay()));
      }
    }
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
        // check state and update movement
        if (!bullet.getState().equals(SpriteState.aim)) {
          bullet = updateBulletMovement(bullet);
        }
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
   * updateBulletMovement is a helper method for {@link #moveAllBullets} which updates bullet movement given it's spriteState.
   */
  private Bullets updateBulletMovement(Bullets bullet) {
    // relative means action given enemies/player on field
    if (bullet.getState().equals(SpriteState.relative)) {
      // player change direction to closest bullet
      double min;
      Vector newDirection;
      Vector overShoot;
      if (bullet.getBulletOwner().equals(SpriteVariations.player1) || bullet.getBulletOwner().equals(SpriteVariations.player2)) {
        if (!this.currentEnemies.isEmpty()) {
          min = this.currentEnemies.get(this.currentEnemies.size() - 1).getPosition().subVect(bullet.getPosition()).length();
          Enemies enemy = null;
          // reset distance to max
          for (int j = this.currentEnemies.size() - 1; j >= 0; j--) {
            enemy = this.currentEnemies.get(j);
            if (enemy.getPosition().subVect(bullet.getPosition()).length() > min) {
              min = enemy.getPosition().subVect(bullet.getPosition()).length();
            }
          }
          newDirection = bullet.getAimVector();
          overShoot = new Vector(0, 0, 1);
          // find closest enemy
          for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
            enemy = this.currentEnemies.get(i);
            if (enemy.getPosition().subVect(bullet.getPosition()).length() <= min) {
              min = enemy.getPosition().subVect(bullet.getPosition()).length();
              // overshoot to make bullet shoot infront of enemy
              overShoot = enemy.getVelocity().multiplyScalar(50);
              newDirection = enemy.getPosition().addVect(overShoot).subVect(bullet.getPosition()).normaliseVect();
              bullet.updateBulletDirection(newDirection.multiplyScalar(bullet.getAimVector().length()));
              bullet.updateBulletVelocity(bullet.getAimVector());
            }
          }
        }
        else if (this.currentBoss != null) {
          min = this.currentBoss.getPosition().subVect(bullet.getPosition()).length();
          newDirection = this.currentBoss.getPosition().subVect(bullet.getPosition()).normaliseVect();
          bullet.updateBulletDirection(newDirection.multiplyScalar(bullet.getAimVector().length()));
          bullet.updateBulletVelocity(bullet.getAimVector());
        }
      }
    }
    return bullet;
  }

  /**
   * checkPlayerBulletCollision is a helper method used by {@link #bulletInsideScreen}.
   * checks if the hitboxes of player/enemy overlap bullets from enemy/player.
   */
  private boolean checkBulletCollision(Bullets shiftedBullet) {
    // player bullet hit enemy/boss
    if (shiftedBullet.getType().equals(SpriteType.PlayerBullet)) { 
      if (this.currentBoss != null) {
        Enemies boss = this.currentBoss;
        if (shiftedBullet.getPosition().subVect(boss.getPosition()).length() < shiftedBullet.getRadius() + boss.getRadius()) {
          boss.attackEnemy(shiftedBullet.getDamage());
          this.currentBoss = boss;
          if (!boss.isAlive()) {
            this.currentBoss = null;
            // calculate score 
            this.score += 10000;
            
          }
          return false;
        }
      }
      for (int i = this.currentEnemies.size() - 1; i >= 0; i--) {
        // collision formula: |P1 - P2| < r1 + r2, where r is radius and P is position vectors
        Enemies enemy = this.currentEnemies.get(i);
        if (shiftedBullet.getPosition().subVect(enemy.getPosition()).length() < shiftedBullet.getRadius() + enemy.getRadius()) {
          enemy.attackEnemy(shiftedBullet.getDamage());
          this.currentEnemies.set(i, enemy);
          if (!enemy.isAlive()) {
            // calculate score
            if (enemy.getVariation().equals(SpriteVariations.yokai1)) {
              this.score += 300;
            }
            else if (enemy.getVariation().equals(SpriteVariations.yokai2)) {
              this.score += 500;
            }
            this.currentEnemies.remove(i);
          }
          return false;
        }
      }
    }
    // enemy/boss bullet hit player
    else if (shiftedBullet.getType().equals(SpriteType.EnemyBullet) || shiftedBullet.getType().equals(SpriteType.BossBullet)) {
      if (shiftedBullet.getPosition().subVect(this.currentPlayer.getPosition()).length() < shiftedBullet.getRadius() + this.currentPlayer.getRadius()) {
        if (this.currentPlayer.isAlive()) {
          this.currentPlayer = this.currentPlayer.respawnPlayer(this.currentPlayer.getLives() - 1).shiftedToStartPoint(Field);
        }
        if (!this.currentPlayer.isAlive()) {
          this.gameState = GameState.GAME_OVER;
        }
        return false;
      }
    }
    return true;
  }

}
