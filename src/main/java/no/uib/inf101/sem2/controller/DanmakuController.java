package no.uib.inf101.sem2.controller;

import no.uib.inf101.sem2.view.DanmakuView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

public class DanmakuController implements KeyListener{
    
  private final ControllableDanmakuModel controllModel;
  private final DanmakuView danView;
  private Timer danTimer;

  /**
   * 
   */
  public DanmakuController(ControllableDanmakuModel controllModel, DanmakuView danView) {
    this.controllModel = controllModel;
    this.danView = danView;

    // key input
    this.danView.setFocusable(true);
    this.danView.addKeyListener(this);

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int speed = 9;
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      // Left arrow was pressed
      this.controllModel.movePlayer(-speed, 0);
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      // Right arrow was pressed
      this.controllModel.movePlayer(speed, 0);
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      // Down arrow was pressed
      this.controllModel.movePlayer(0, speed);
    }
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      // Up arrow was pressed
      this.controllModel.movePlayer(0, -speed);
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      // Spacebar was pressed
    }
    this.danView.repaint();
  
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    
  }

}
