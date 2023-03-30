package no.uib.inf101.sem2;

import no.uib.inf101.sem2.model.DanmakuField;
import no.uib.inf101.sem2.model.DanmakuModel;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.DanmakuSpawner;
import no.uib.inf101.sem2.view.DanmakuView;

import javax.swing.JFrame;

public class Main {

  static final int x = 10;
  static final int y = 10;
  static final int Width = 500;
  static final int Height = 600;

  public static void main(String[] args) {

    DanmakuField Field = new DanmakuField(x, y, Width, Height);
    DanmakuFactory playableC = new DanmakuSpawner();
    DanmakuModel Model = new DanmakuModel(Field, playableC);
    DanmakuView view = new DanmakuView(Model);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("INF101");
    frame.setContentPane(view);
    frame.pack();
    frame.setVisible(true);
  }
}
