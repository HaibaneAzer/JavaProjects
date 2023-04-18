package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.GameState;
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
  private final int updateTick = 1000 / 120; // 60 frames per second
  // variables for fps calculation
  private double oldTime;
  private double newTime;
  private double[] tickList;
  private double tickSum;
  private int tickIndex;
  private final int maxDeltaSamples = 100;
  private double tick;
  // list of common sprite movements
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

  }
  
  /**
   * updateModel is the main method that processes all input from both the player and 
   * the ingame envoirment. 
   * NB: try using an ActionEvent method and Timer for ingame inputs.
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
    
  }

  /**
   * 
   * 
   */
  protected void keyboardInput() {
    if (controllModel.getGameState().equals(GameState.GAME_MENU)) {
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ENTER)) {
        this.controllModel.setGameState(GameState.ACTIVE_GAME);
        
      }
    }
    else if (controllModel.getGameState().equals(GameState.ACTIVE_GAME)) {
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
        this.controllModel.playerFire(playerFireRate, this.keyBoard.keyDown(KeyEvent.VK_SHIFT)); // 1 bullets per second
      }
      // pause menu
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ESCAPE)) {
        this.controllModel.setGameState(GameState.PAUSE_GAME);
      }
    }
    else if (controllModel.getGameState().equals(GameState.PAUSE_GAME)) {
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ENTER)) {
        // reset field and set game active

        controllModel.setGameState(GameState.ACTIVE_GAME);
      }
      else if (this.keyBoard.keyDownOnce(KeyEvent.VK_BACK_SPACE)) {
        // reset field and set game menu
        this.controllModel.resetField();
        controllModel.setGameState(GameState.GAME_MENU);
      }    
    }
    else if (controllModel.getGameState().equals(GameState.GAME_OVER)) {
      if (this.keyBoard.keyDownOnce(KeyEvent.VK_ENTER)) {
        // reset field and set game active
        this.controllModel.resetField();
        controllModel.setGameState(GameState.ACTIVE_GAME);
      }
      else if (this.keyBoard.keyDownOnce(KeyEvent.VK_BACK_SPACE)) {
        // reset field and set game menu
        this.controllModel.resetField();
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
