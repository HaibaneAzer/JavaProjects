package no.uib.inf101.sem2;

import no.uib.inf101.sem2.controller.DanmakuController;
import no.uib.inf101.sem2.model.DanmakuField;
import no.uib.inf101.sem2.model.DanmakuModel;
import no.uib.inf101.sem2.model.danmakus.DanmakuFactory;
import no.uib.inf101.sem2.model.danmakus.DanmakuSpawner;
import no.uib.inf101.sem2.view.DanmakuView;

import javax.swing.JFrame;

public class Main {

  static final int x = 25;
  static final int y = 25;
  static final int Width = 480; // keep ratio at 1.154
  static final int Height = 560;

  public static void main(String[] args) {

    DanmakuField Field = new DanmakuField(x, y, Width, Height);
    DanmakuFactory SpriteSpawner = new DanmakuSpawner();
    DanmakuModel Model = new DanmakuModel(Field, SpriteSpawner);
    DanmakuView view = new DanmakuView(Model);

    new DanmakuController(Model, view);

    JFrame frame = new JFrame();
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("INF101");
    frame.setContentPane(view);
    frame.pack();
    frame.setVisible(true);
    
  }
}
