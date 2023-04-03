package no.uib.inf101.sem2.model;

import no.uib.inf101.sem2.controller.ControllableDanmakuModel;
import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel, ControllableDanmakuModel{
  
  private DanmakuField Field;
  private final DanmakuFactory playableC;
  private Player currentPlayer;
  private double FPSCounter = 60.0;
  
  public DanmakuModel(DanmakuField Field, DanmakuFactory playableC) {
    this.Field = Field;
    this.playableC = playableC;
    this.currentPlayer = playableC.getNewPlayer("P1c").shiftedToStartPoint(Field);
    
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
  public double getFPSValue() {
    return this.FPSCounter;
  }

  @Override
  public void setFPSValue(double newFPS) {
    this.FPSCounter = newFPS;
  }
  
  @Override
  public boolean movePlayer(Vector direction, double speed) {

    direction = direction.normaliseVect();
    if (!insideField(this.currentPlayer.displaceBy(direction, speed))) {
      return false;
    }
    this.currentPlayer = this.currentPlayer.displaceBy(direction, speed);
    return true;

  }
  
  private boolean insideField(Player shiftedplayer) {
    boolean withinField = (
      shiftedplayer.getPosition().x() - shiftedplayer.getRadius() > this.Field.getFieldX() && 
      shiftedplayer.getPosition().y() - shiftedplayer.getRadius() > this.Field.getFieldY() &&
      shiftedplayer.getPosition().x() + shiftedplayer.getRadius() < this.Field.getFieldX() + this.Field.width() &&
      shiftedplayer.getPosition().y() + shiftedplayer.getRadius() < this.Field.getFieldY() + this.Field.height()
    );
    if (!withinField) {
      return false;
    }
    return true;

  }
  
  
}
