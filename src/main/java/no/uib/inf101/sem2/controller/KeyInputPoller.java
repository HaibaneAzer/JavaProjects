
// Code source: Article written by Tim Wright.
// Class: KeyBoardInput
// link: https://www.gamedev.net/tutorials/_/technical/general-programming/java-games-keyboard-and-mouse-r2439/

package no.uib.inf101.sem2.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputPoller implements KeyListener{
  
  private static final int KEY_COUNT = 93; // increase if more keys needed
  // KeyCode List:
  // https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
  
  private enum KeyState {
    RELEASED, // Not down
    PRESSED,  // Down, but not the first time
    ONCE      // Down for the first time
  }
        
  // Current state of the keyboard. 
  private boolean[] currentKeys = null;
        
  // Polled keyboard state. 
  private KeyState[] keys = null;

  /** 
  * The KeyInputPoller constructor is used to make a list for multiple different key inputs.
  */      
  public KeyInputPoller() {
    
    currentKeys = new boolean[ KEY_COUNT ];
    keys = new KeyState[ KEY_COUNT ];
    // set all keys to released by default.
    for( int i = 0; i < KEY_COUNT; ++i ) {
      keys[i] = KeyState.RELEASED;
    }

  }

  /**
   * Poll method checks wether any keys are being pressed, and then sets
   * their key status. 
   * Poll uses the KeyListener methods to check for key presses and releases.
   */
  public synchronized void poll() {
    for( int i = 0; i < KEY_COUNT; ++i ) {
      // Set the key state 
      if( currentKeys[i] ) {
        // If the key is down now, but was not
        // down last frame, set it to ONCE,
        // otherwise, set it to PRESSED
        if( keys[i] == KeyState.RELEASED )
          keys[i] = KeyState.ONCE;
        else
          keys[i] = KeyState.PRESSED;
      } else {
        keys[i] = KeyState.RELEASED;
      }
    }
  }

  /**
   * keyDown is used when key(s) are being pressed continuosly.
   * Useful for keeping movement of player smooth.
   */
  public boolean keyDown( int keyCode ) {
    return keys[keyCode] == KeyState.ONCE ||
           keys[keyCode] == KeyState.PRESSED;
  }
  
  /**
   * keyDownOnce only returns true when any key is being tapped.
   * Is used for single tap interactions like pressing "ESC" to leave game.
   */
  public boolean keyDownOnce( int keyCode ) {
    return keys[keyCode] == KeyState.ONCE;
  }
  
  @Override
  public synchronized void keyPressed( KeyEvent e ) {
    int keyCode = e.getKeyCode();
    if( keyCode >= 0 && keyCode < KEY_COUNT ) {
      currentKeys[keyCode] = true;
    }
  }

  @Override
  public synchronized void keyReleased( KeyEvent e ) {
    int keyCode = e.getKeyCode();
    if( keyCode >= 0 && keyCode < KEY_COUNT ) {
      currentKeys[keyCode] = false;
    }
  }

  @Override
  public void keyTyped( KeyEvent e ) {/* not needed */}


}
