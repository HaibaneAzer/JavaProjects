package no.uib.inf101.sem2.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import no.uib.inf101.sem2.grid.FieldDimension;
import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;

public class DanmakuView extends JPanel{
  
  private final ViewableDanmakuModel Model;
  private final ColorTheme setColor;
  private double SCOREBOARDWIDTH;
  private static final double WFactor = 0.7;
  private double SCOREBOARDHEIGHT;
  private static final double HFactor = 0.1;
  
  public DanmakuView(ViewableDanmakuModel Model) {
    this.setFocusable(true);
    this.Model = Model;
    
    // prefered field dimensions
    int x = this.Model.getDimension().getFieldX();
    int y = this.Model.getDimension().getFieldY();
    int Width = this.Model.getDimension().width();
    int Height = this.Model.getDimension().height();
    // prefered scoreboard dimension
    this.SCOREBOARDWIDTH = WFactor*(Width);
    this.SCOREBOARDHEIGHT = Height + 2*y;
    
    int preferredWidth = (int) (Width + 2*x + this.SCOREBOARDWIDTH);
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
    drawPlayer(Canvas, this.Model.getPlayer() ,this.setColor);
    drawEnemy(Canvas, this.Model.getEnemiesOnField(), this.setColor);
    drawBulletsOnField(Canvas, this.Model.getBulletsOnField(), this.setColor);
    drawStatistics(Canvas, this.Model.getDimension(), setColor, this.Model);
    
  }
  
  private void drawPlayer(Graphics2D Canvas, Player player, ColorTheme Color) {
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

  private void drawEnemy(Graphics2D Canvas, Iterable<Enemies> enemies, ColorTheme Color) {
    double x;
    double y;
    Vector aimLength;
    double aimX;
    double aimY;
    double diameter;
    Ellipse2D enemyBall;
    Line2D enemyAimArrow;

    for (Enemies enemy : enemies) {
      Vector aimvectStartPoint = enemy.getPosition(); // aim vector starts at position.
    
      x = enemy.getPosition().x() - enemy.getRadius();
      y = enemy.getPosition().y() - enemy.getRadius();
      diameter = 2*enemy.getRadius();
    
      enemyBall = new Ellipse2D.Double(x, y, diameter, diameter);
    
      Canvas.setColor(Color.getSpriteColor('b'));
      Canvas.fill(enemyBall);

      aimX = aimvectStartPoint.x();
      aimY = aimvectStartPoint.y();
      aimLength = aimvectStartPoint.addVect(enemy.getAimVector());
      enemyAimArrow = new Line2D.Double(aimX, aimY, aimLength.x(), aimLength.y());

      Canvas.setColor(Color.getSpriteColor('c'));
      Canvas.setStroke(new BasicStroke(2));
      Canvas.draw(enemyAimArrow);
      //System.out.println(aimX + " and " + aimY);
    }

  }

  private void drawBulletsOnField(Graphics2D Canvas, Iterable<Bullets> bulletList, ColorTheme Color) {
    double x;
    double y;
    Vector aimVectStart;
    Vector aimLength;
    double aimX;
    double aimY;
    double diameter;
    Ellipse2D bulletHitbox;
    Line2D bulletTrajectory;
    
    for (Bullets bullet : bulletList) {

      x = bullet.getPosition().x() - bullet.getRadius();
      y = bullet.getPosition().y() - bullet.getRadius();
      diameter = 2*bullet.getRadius();

      bulletHitbox = new Ellipse2D.Double(x, y, diameter, diameter);

      Canvas.setColor(Color.getSpriteColor('g'));
      Canvas.fill(bulletHitbox);

      aimVectStart = bullet.getPosition();
      aimX = aimVectStart.x();
      aimY = aimVectStart.y();
      aimLength = aimVectStart.addVect(bullet.getAimVector());
      bulletTrajectory = new Line2D.Double(aimX, aimY, aimLength.x(), aimLength.y());

      Canvas.setColor(Color.getSpriteColor('y'));
      Canvas.setStroke(new BasicStroke(1));
      Canvas.draw(bulletTrajectory);

    }


  }
  
  private void drawField(Graphics2D Canvas, FieldDimension Field, ColorTheme Color) {
    double x = (double) Field.getFieldX();
    double y = (double) Field.getFieldY();
    double width = (this.getWidth() - this.SCOREBOARDWIDTH) - 2*x;
    double height = this.getHeight() - 2*y;
    
    Rectangle2D newRect = new Rectangle2D.Double(x, y, width, height);
    Canvas.setColor(Color.getFieldColor());
    Canvas.fill(newRect);

    // draw line of collection
    Canvas.setColor(Color.getSpriteColor('r'));
    Line2D collectionLine = new Line2D.Double(x, y + (height*(0.28)), x + width, y + (height*(0.28)));
    Canvas.draw(collectionLine);
    
  }

  private void drawStatistics(Graphics2D Canvas, FieldDimension field, ColorTheme color, ViewableDanmakuModel model) {
    // fps counter
    int x = field.getFieldX();
    int y = field.getFieldY();
    
    Canvas.setColor(Color.RED);
    Canvas.setFont(new Font("Arial", Font.BOLD, 18));
    Inf101Graphics.drawCenteredString(Canvas, "fps: " + model.getFPSValue(), 2*x, 2*y, 50, 50);
    
    // current stage
    x = field.getFieldX() + field.width();
    y = field.getFieldY();
    double Width = this.SCOREBOARDWIDTH;
    double HeightStageBoard = 0.2*this.SCOREBOARDHEIGHT;

    Canvas.setColor(Color.DARK_GRAY);
    Canvas.setFont(new Font("Arial", Font.BOLD, 25));
    Inf101Graphics.drawCenteredString(Canvas, "Stage: " + model.getCurrentStage(), x, y, Width, HeightStageBoard);

  }
  
  
  
}
