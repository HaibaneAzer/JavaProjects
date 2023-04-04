package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.grid.Vector;
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
  // gameTick and adaptable frameRate
  private Timer Timer;
  private final int updateTick = 1000 / 120; // 60 frames per second
  private double oldTime;
  private double newTime;
  private double[] tickList;
  private double tickSum;
  private int tickIndex;
  private final int maxDeltaSamples = 100;
  private double tick;
  // list of common sprite movements
  // player movement direction
  private static final Vector playerDirections[] = {
    new Vector(0, -1, 1),
    // up
    new Vector(0, 1, 1),
    // down
    new Vector(-1, 0, 1),
    // left
    new Vector(1, 0, 1)
    // right
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
    
    
  }

  /**
   * 
   * 
   */
  protected void keyboardInput() {
    // move player down
    if (this.keyBoard.keyDown(KeyEvent.VK_UP)) { 
      
      this.controllModel.movePlayer(playerDirections[0].multiplyScalar(this.playerSpeed));
    }
    // move player up
    if (this.keyBoard.keyDown(KeyEvent.VK_DOWN)) {
      this.controllModel.movePlayer(playerDirections[1].multiplyScalar(this.playerSpeed));
    }
    // move player left
    if (this.keyBoard.keyDown(KeyEvent.VK_LEFT)) {
      this.controllModel.movePlayer(playerDirections[2].multiplyScalar(this.playerSpeed));
    }
    // move player right
    if (this.keyBoard.keyDown(KeyEvent.VK_RIGHT)) {
      this.controllModel.movePlayer(playerDirections[3].multiplyScalar(this.playerSpeed));
    }
    // change player speed
    if (this.keyBoard.keyDown(KeyEvent.VK_SHIFT)) {
      this.playerSpeed = 3;
    }
    else {
      this.playerSpeed = 6;
    }

  }

  @Override
  public void actionPerformed(ActionEvent arg0) {

    // keyboard input
    this.keyBoard.poll();
    // process input for player
    keyboardInput();

    // run other processes
    updateModel(arg0);

    this.danView.repaint();

    // update FPS
    newTime = System.nanoTime();
    double fpsTick = Math.round(1000000000 / (newTime - oldTime));
    calcAverageFPS(fpsTick);
    oldTime = newTime;

  }

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
