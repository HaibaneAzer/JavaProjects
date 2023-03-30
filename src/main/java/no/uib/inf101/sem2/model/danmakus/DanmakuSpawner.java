package no.uib.inf101.sem2.model.danmakus;

public class DanmakuSpawner implements DanmakuFactory{




    @Override
    public Player getNewPlayer(String C) {
        Player playableC = Player.newPlayer(C);
        return playableC;
    }
    



}
