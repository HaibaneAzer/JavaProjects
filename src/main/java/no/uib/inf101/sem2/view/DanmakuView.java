package no.uib.inf101.sem2.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.model.danmakus.Player;

public class DanmakuView extends JPanel{
  
  private final ViewableDanmakuModel Model;
  private final ColorTheme setColor;
  
  
  public DanmakuView(ViewableDanmakuModel Model) {
    this.setFocusable(true);
    this.Model = Model;
    
    //
    int x = this.Model.getDimension().getFieldX();
    int y = this.Model.getDimension().getFieldY();
    int Width = this.Model.getDimension().width();
    int Height = this.Model.getDimension().height();
    int preferredWidth = (int) (Width + 2*x);
    int preferredHeight = (int) (Height + 2*y);
    
    this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    this.setColor = new DefaultColorTheme();
    this.setBackground(this.setColor.getBackgroundColor());
  }    
  
  @Override
  public void paintComponent(Graphics GraphComp) {
    super.paintComponent(GraphComp);
    Graphics2D Comp2D = (Graphics2D) GraphComp;
    drawGame(Comp2D);
  }
  
  private void drawGame(Graphics2D Canvas) {
    
    drawField(Canvas, this.Model.getDimension(), this.setColor);
    drawSprites(Canvas, this.Model.getPlayer() ,this.setColor);
    drawFPSCounter(Canvas, this.Model.getDimension(), setColor, this.Model.getFPSValue());
    
  }
  
  private void drawSprites(Graphics2D Canvas, Player player, ColorTheme Color) {
    double x;
    double y;
    double diameter;
    Ellipse2D playerBall;
    
    x = player.getPosition().x() - player.getRadius();
    y = player.getPosition().y() - player.getRadius();
    diameter = 2*player.getRadius();
    
    playerBall = new Ellipse2D.Double(x, y, diameter, diameter);
    
    Canvas.setColor(Color.getSpriteColor('r'));
    Canvas.fill(playerBall);
    
    
  }
  
  private void drawField(Graphics2D Canvas, FieldDimension Field, ColorTheme Color) {
    double x = (double) Field.getFieldX();
    double y = (double) Field.getFieldY();
    double width = this.getWidth() - 2*x;
    double height = this.getHeight() - 2*y;
    
    Rectangle2D newRect = new Rectangle2D.Double(x, y, width, height);
    Canvas.setColor(Color.getFieldColor());
    Canvas.fill(newRect);
    
  }

  private void drawFPSCounter(Graphics2D Canvas, FieldDimension field, ColorTheme color, double fps) {
    int x = field.getFieldX();
    int y = field.getFieldY();
    
    Canvas.setColor(Color.RED);
    Canvas.setFont(new Font("Arial", Font.BOLD, 18));
    Inf101Graphics.drawCenteredString(Canvas, "fps: " + fps, 2*x, 2*y, 50, 50);

  }
  
  
  
}
