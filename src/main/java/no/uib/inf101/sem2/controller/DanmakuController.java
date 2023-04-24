package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.midi.DanmakuSong;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;
import no.uib.inf101.sem2.view.DanmakuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class DanmakuController implements ActionListener{
    
  private final ControllableDanmakuModel controllModel;
  private final DanmakuView danView;
  private KeyInputPoller keyBoard;
  private int playerSpeed;
  private static final int playerFireRate = 9; // about 13 shots per second
  // gameTick and adaptable frameRate
  private Timer Timer;
  private final int updateTick = 1000 / 90; // 90 frames per second
  // variables for fps calculation
  private double oldTime;
  private double newTime;
  private double[] tickList;
  private double tickSum;
  private int tickIndex;
  private final int maxDeltaSamples = 100; // enough for 2 decimal places.
  private double tick;
  // music
  private DanmakuSong music;
  private int currentStage;
  private SpriteVariations currentBoss;
  private boolean gameOverExecuted;
  // player movement direction
  private static final Vector PlayerMove[] = {
    new Vector(0, -1, 1),
    // up
    new Vector(0, 1, 1),
    // down
    new Vector(-1, 0, 1),
    // left
    new Vector(1, 0, 1),
    // right
    new Vector(0, 0, 1)
    // no movement
  };

  
  public DanmakuController(ControllableDanmakuModel controllModel, DanmakuView danView) {
    this.controllModel = controllModel;
    this.danView = danView;
    this.playerSpeed = 6;
    // game tick
    this.Timer = new Timer(updateTick, this);
    this.Timer.start();
    // fps counter
    this.tickIndex = 0;
    this.tickSum = 0;
    this.tickList = new double[maxDeltaSamples];
    this.tick = 0.0;
    this.oldTime = System.nanoTime();
    // key input
    this.keyBoard = new KeyInputPoller();
    this.danView.setFocusable(true);
    this.danView.addKeyListener(this.keyBoard);
    // music
    this.music = new DanmakuSong("01_a_shadow_in_the_blue_sky.mid");
    this.music.run();
    this.currentStage = 0;
    this.currentBoss = null;
    this.gameOverExecuted = true;

  }
  
  /**
   * updateModel is the main method that processes all input from both the player and 
   * the ingame envoirment. 
   */
  public void updateModel(ActionEvent arg0) {
    // enemy movement and spawning
    this.controllModel.moveEnemiesInWaves();
    // enemy bullet fire
    this.controllModel.enemyFire();
    // update bullet pos
    if (this.controllModel.getBulletsOnField().iterator().hasNext()) {
      this.controllModel.moveAllBullets();
    }
    updateActiveGameMusic(this.controllModel.getCurrentStage(), this.controllModel.getBossEnemyOnField());
    
  }

  private void updateActiveGameMusic(int stage, Enemies boss) {
    if (boss != null) {
      if (this.currentBoss != boss.getVariation()) {
        this.currentBoss = boss.getVariation();
  
        this.music.doStopMidiSounds();
        if (stage == 1) {
          this.music = new DanmakuSong("youkai_mountain_mysterious_mountain.mid");
        }
        else if (stage == 2) {
          this.music = new DanmakuSong("the_primal_scene_of_japan_the_girl_.mid");
        }
        this.music.run();
      }
    }
    else if (this.currentStage != stage) {
      this.currentStage = stage; // update stage to prevent multiple resets

      this.music.doStopMidiSounds();
      if (stage == 1) {
        this.music = new DanmakuSong("fall_of_fall_autumnal_waterfall_has.mid");
      }
      else if (stage == 2) {
        this.music = new DanmakuSong("faith_is_for_the_transient_people_s_af6ee.mid");
      }
      else if (stage == 3) {
        this.music = new DanmakuSong("lullaby_of_deserted_hell_sagittariu.mid");
      }
      this.music.run();

    }
  }

  /**
   * keyBoardInput uses KeyInputPoller to handle input and their corrosponding actions.
   */
  protected void keyboardInput() {
    if (controllModel.getGameState().equals(GameState.GAME_MENU)) {
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ENTER)) {
        this.controllModel.setGameState(GameState.SELECT_SCREEN);
      }
    }
    else if (controllModel.getGameState().equals(GameState.SELECT_SCREEN)) {
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_1)) {
        this.controllModel.SelectPlayer(SpriteVariations.player1);
        this.controllModel.setGameState(GameState.ACTIVE_GAME);
      }
      else if (this.keyBoard.keyDownOnce(KeyEvent.VK_2)) {
        this.controllModel.SelectPlayer(SpriteVariations.player2);
        this.controllModel.setGameState(GameState.ACTIVE_GAME);
      }
    }
    else if (controllModel.getGameState().equals(GameState.ACTIVE_GAME)) {
      // set movement to zero when none of arrow keys are pressed.
      if (!(this.keyBoard.keyDown(KeyEvent.VK_UP) || this.keyBoard.keyDown(KeyEvent.VK_DOWN) 
         || this.keyBoard.keyDown(KeyEvent.VK_RIGHT) || this.keyBoard.keyDown(KeyEvent.VK_LEFT))) {
        this.controllModel.movePlayer(PlayerMove[4]);
      }
      if (this.keyBoard.keyDown(KeyEvent.VK_UP)) { 
        this.controllModel.movePlayer(PlayerMove[0].multiplyScalar(this.playerSpeed));
      }
      if (this.keyBoard.keyDown(KeyEvent.VK_DOWN)) {
        this.controllModel.movePlayer(PlayerMove[1].multiplyScalar(this.playerSpeed));
      }
      if (this.keyBoard.keyDown(KeyEvent.VK_LEFT)) {
        this.controllModel.movePlayer(PlayerMove[2].multiplyScalar(this.playerSpeed));
      }
      if (this.keyBoard.keyDown(KeyEvent.VK_RIGHT)) {
        this.controllModel.movePlayer(PlayerMove[3].multiplyScalar(this.playerSpeed));
      }
      if (this.keyBoard.keyDown(KeyEvent.VK_SHIFT)) {
        this.playerSpeed = 3;
      }
      else {
        this.playerSpeed = 5;
      }
      if (this.keyBoard.keyDown(KeyEvent.VK_Z)) {
        this.controllModel.playerFire(playerFireRate, this.keyBoard.keyDown(KeyEvent.VK_SHIFT));
      }
      // pause menu
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ESCAPE)) {
        this.controllModel.setGameState(GameState.PAUSE_GAME);
        this.music.doPauseMidiSounds();
        
      }
    }
    else if (controllModel.getGameState().equals(GameState.PAUSE_GAME)) {
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ENTER)) {
        // reset field and set game active
        controllModel.setGameState(GameState.ACTIVE_GAME);
        this.music.doUnpauseMidiSounds();
      }
      else if (this.keyBoard.keyDownOnce(KeyEvent.VK_BACK_SPACE)) {
        // reset field and set game menu
        this.controllModel.resetField();
        this.currentStage = 0;
        this.currentBoss = null;
        this.music.doStopMidiSounds();
        this.music = new DanmakuSong("Pokemon - Farewell, Pikachu!.mid");
        this.music.run();
        controllModel.setGameState(GameState.GAME_MENU);
      }    
    }
    else if (controllModel.getGameState().equals(GameState.GAME_OVER)) {
      if (this.gameOverExecuted) {
        this.gameOverExecuted = false;
        this.music.doStopMidiSounds();
        this.music = new DanmakuSong("touhou_-_player_s_score (1).mid");
        this.music.run();
      }
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ENTER)) {
        // reset field and set game active
        this.controllModel.resetField();
        this.gameOverExecuted = true;
        this.currentStage = 0;
        this.currentBoss = null;
        controllModel.setGameState(GameState.ACTIVE_GAME);
      }
      else if (this.keyBoard.keyDownOnce(KeyEvent.VK_BACK_SPACE)) {
        // reset field and set game menu
        this.controllModel.resetField();
        this.currentStage = 0;
        this.currentBoss = null;
        this.music.doStopMidiSounds();
        this.music = new DanmakuSong("Pokemon - Farewell, Pikachu!.mid");
        this.music.run();
        controllModel.setGameState(GameState.GAME_MENU);
      }    
    }
      
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {

    // keyboard input
    this.keyBoard.poll();
    // process input for player
    keyboardInput();

    if (this.controllModel.getGameState().equals(GameState.ACTIVE_GAME)) {
      updateModel(arg0);
    }
    // run other processes

    this.danView.repaint();

    // update FPS
    newTime = System.nanoTime();
    double fpsTick = Math.round(1000000000 / (newTime - oldTime));
    calcAverageFPS(fpsTick);
    oldTime = newTime;

  }

  /**
   * calcAverageFPS updates values in sum of N measured frame rates and divides by N to get average fps value.
   * Method comes from author KPexEA and editor Sigod at stackoverflow ( + minor optimizations).
   * Link: https://stackoverflow.com/questions/87304/calculating-frames-per-second-in-a-game.
   * 
   */
  private void calcAverageFPS(double newTick) {
    
    tickSum -= tickList[tickIndex];
    tickSum += newTick;
    tickList[tickIndex] = newTick;
    tickIndex = (tickIndex + 1) % maxDeltaSamples;
    double avgTick = tickSum/maxDeltaSamples;
     
    if (this.tick >= this.updateTick*60) {
      controllModel.setFPSValue(avgTick);
      this.tick = 0.0;
    }
    this.tick += this.updateTick;
  }

}
