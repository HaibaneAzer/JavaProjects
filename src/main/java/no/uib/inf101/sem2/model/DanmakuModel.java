package no.uib.inf101.sem2.model;

import no.uib.inf101.sem2.controller.ControllableDanmakuModel;
import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel, ControllableDanmakuModel{
  
  private DanmakuField Field;
  private final DanmakuFactory playableC;
  private Player currentPlayer;
  
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
  public boolean movePlayer(int deltaX, int deltaY) {
    
    if (!insideField(this.currentPlayer.shiftedBy(deltaX, deltaY))) {
      return false;
    }
    this.currentPlayer = this.currentPlayer.shiftedBy(deltaX, deltaY);
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
