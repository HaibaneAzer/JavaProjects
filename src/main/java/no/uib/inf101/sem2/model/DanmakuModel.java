package no.uib.inf101.sem2.model;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.view.ViewableDanmakuModel;

public class DanmakuModel implements ViewableDanmakuModel{

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

    
    

}
