package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.view.DanmakuView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DanmakuController {
    
  private final ControllableDanmakuModel controllModel;
  private final DanmakuView danView;
  private KeyInputPoller keyBoard;
  private int playerSpeed;
  private int pollTimer;

  /**
   * 
   */
  public DanmakuController(ControllableDanmakuModel controllModel, DanmakuView danView) {
    this.controllModel = controllModel;
    this.danView = danView;
    this.playerSpeed = 3;
    this.pollTimer = 44000;

    // key input
    this.keyBoard = new KeyInputPoller();
    this.danView.setFocusable(true);
    this.danView.addKeyListener(this.keyBoard);

  }
  // idea: to make smoother key transitions, make a keyboard polling class
  // to keep track of all the keys being pressed/typed/released at the same time.
  // 
  /**
   * runPoller is the main method that processes all input from both the player and 
   * the ingame envoirment. 
   * 
   */
  public void runPoller() {

    // exit with esc (change later with gamestate)
    while (!this.keyBoard.keyDownOnce(KeyEvent.VK_ESCAPE)) {
      this.keyBoard.poll();
      // only process inputs every 44000 tick to prevent player teleportation.
      if (this.pollTimer == 44000) {
        // process input for player
        keyboardInput();
        this.pollTimer = 0;
      }
      this.pollTimer += 1;
    }
    
    
  }

  /**
   * 
   * 
   */
  protected void keyboardInput() {
    // move player down
    if (this.keyBoard.keyDown(KeyEvent.VK_DOWN)) {
      
      this.controllModel.movePlayer(0, this.playerSpeed);
    }
    // move player up
    if (this.keyBoard.keyDown(KeyEvent.VK_UP)) {
      this.controllModel.movePlayer(0, -this.playerSpeed);
    }
    // move player left
    if (this.keyBoard.keyDown(KeyEvent.VK_LEFT)) {
      this.controllModel.movePlayer(-this.playerSpeed, 0);
    }
    // move player right
    if (this.keyBoard.keyDown(KeyEvent.VK_RIGHT)) {
      this.controllModel.movePlayer(this.playerSpeed, 0);
    }
    // change player speed
    if (this.keyBoard.keyDown(KeyEvent.VK_SHIFT)) {
      this.playerSpeed = 1;
    }
    else {
      this.playerSpeed = 3;
    }
    this.danView.repaint();

  }


}
