package no.uib.inf101.sem2.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public class DanmakuView extends JPanel{
  
  private final ViewableDanmakuModel Model;
  private final ColorTheme setColor;
  private double SCOREBOARDWIDTH;
  private static final double WFactor = 0.7;
  private double SCOREBOARDHEIGHT;
  
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
    // Field
    double xField = (double) Model.getDimension().getFieldX();
    double yField = (double) Model.getDimension().getFieldY();
    double widthField = (this.getWidth() - this.SCOREBOARDWIDTH) - 2*xField;
    double heightField = this.getHeight() - 2*yField;
    
    Rectangle2D fieldRect = new Rectangle2D.Double(xField, yField, widthField, heightField);

    // statistics
    int xStat = Model.getDimension().getFieldX();
    int yStat = Model.getDimension().getFieldY();
    double widthStat = this.SCOREBOARDWIDTH;
    double heightStat = this.SCOREBOARDHEIGHT;

    Rectangle2D statRect = new Rectangle2D.Double(xStat, yStat, widthStat, heightStat);

    // menu screen
    Rectangle2D drawScreenRect = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());

    drawMenuScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    if (!Model.getGameState().equals(GameState.GAME_MENU)) {
      // draw bullet field
      drawField(Canvas, fieldRect, setColor);
      // draw player on field
      drawPlayer(Canvas, Model.getPlayer(), setColor);
      // draw enemies on field
      drawEnemy(Canvas, Model.getEnemiesOnField(), setColor);
      // draw bosses on field
      if (Model.getBossEnemyOnField() != null) {
        drawBoss(Canvas, Model.getBossEnemyOnField(), Model.getBossAttackType(), setColor);
      }
      // draw bullets on field
      drawBulletsOnField(Canvas, Model.getBulletsOnField(), setColor);
      // draw statistics like score or boss healthbar...
      drawStatistics(Canvas, statRect, setColor, Model);
      // draw pause screen
      drawPauseScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
      // draw game over screen when player has no lifes
      drawEndGameScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    }
  }
  
  private static void drawPlayer(Graphics2D Canvas, Player player, ColorTheme Color) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    Ellipse2D playerBall;
    BufferedImage playerImg;

    // player2
    // update image given player state
    playerImg = Inf101Graphics.loadImageFromResources("/player2.png");
    if (player.getVelocity().x() > 0) {
      playerImg = Inf101Graphics.loadImageFromResources("/player2Right.png");
    }
    else if (player.getVelocity().x() < 0) {
      playerImg = Inf101Graphics.loadImageFromResources("/player2Left.png");
    }
    // player1
    if (player.getVariation().equals(SpriteVariations.player1)) {
      // update image given player state
      playerImg = Inf101Graphics.loadImageFromResources("/player1.png");
      if (player.getVelocity().x() > 0) {
        playerImg = Inf101Graphics.loadImageFromResources("/player1Right.png");
      }
      else if (player.getVelocity().x() < 0) {
        playerImg = Inf101Graphics.loadImageFromResources("/player1Left.png");
      }
    }
    
    // initialize variables
    imgWidth = playerImg.getWidth();
    imgHeight = playerImg.getHeight();
    x = player.getPosition().x();
    y = player.getPosition().y();
    diameter = 2*player.getRadius();

    // scale by smallest image length.
    scaleFactor = 2*(diameter/imgHeight);
    if (imgWidth < imgHeight) {
      scaleFactor = 2*(diameter/imgWidth);
    }
    
    // draw player
    Inf101Graphics.drawCenteredImage(Canvas, playerImg, x, y, scaleFactor);
    x -= player.getRadius();
    y -= player.getRadius();
    playerBall = new Ellipse2D.Double(x, y, diameter, diameter);
    Canvas.setColor(Color.getSpriteColor('r'));
    Canvas.fill(playerBall);
    
  }

  private static void drawEnemy(Graphics2D Canvas, Iterable<Enemies> enemies, ColorTheme Color) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    Ellipse2D enemyBall;
    BufferedImage enemyImg;


    for (Enemies enemy : enemies) {
      // get image for corrosponding enemy variation
      enemyImg = Inf101Graphics.loadImageFromResources("/Yokai2.png");
      if (enemy.getVariation().equals(SpriteVariations.yokai1)) {
        enemyImg = Inf101Graphics.loadImageFromResources("/Yokai1.png");
      }
      
      // initialize variables
      imgWidth = enemyImg.getWidth();
      imgHeight = enemyImg.getHeight();
      x = enemy.getPosition().x();
      y = enemy.getPosition().y();
      diameter = 2*enemy.getRadius();

      // scale by smallest image length.
      scaleFactor = 2*(diameter/imgHeight);
      if (imgWidth < imgHeight) {
        scaleFactor = 2*(diameter/imgWidth);
      }
      
      // draw enemy
      Inf101Graphics.drawCenteredImage(Canvas, enemyImg, x, y, scaleFactor);
      // draw hitbox
      /* x -= enemy.getRadius();
      y -= enemy.getRadius();
      enemyBall = new Ellipse2D.Double(x, y, diameter, diameter);
      Canvas.setColor(Color.getSpriteColor('b'));
      Canvas.fill(enemyBall); */

    }
  }

  private static void drawBoss(Graphics2D Canvas, Enemies boss, boolean activateSuper, ColorTheme Color) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    Ellipse2D enemyBall;
    BufferedImage bossImg;
    // variables for image manipulations
    AffineTransform tx;
    AffineTransformOp op;

    // get image for corrosponding boss variation
    // boss 5
    bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss5.png");
    if (boss.getVelocity().x() > 0) {
      bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss5Right.png");
    }
    else if (boss.getVelocity().x() < 0) {
      bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss5Right.png");
      // flip image horisontally
      tx = AffineTransform.getScaleInstance(-1, 1);
      tx.translate(-bossImg.getWidth(null), 0);
      op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      bossImg = op.filter(bossImg, null);
    }
    // using super?
    if (activateSuper) {
      bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss5Super.png");
    }
    // boss 4
    if (boss.getVariation().equals(SpriteVariations.boss4)) {
      bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss4.png");
      if (boss.getVelocity().x() < 0) {
        bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss4Left.png");
      }
      else if (boss.getVelocity().x() > 0) {
        bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss4Left.png");
        // flip image horisontally
        tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bossImg.getWidth(null), 0);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bossImg = op.filter(bossImg, null);
      }
      // using super?
      if (activateSuper) {
        bossImg = Inf101Graphics.loadImageFromResources("/MoFBoss4Super.png");
      }
    }
    
    // initialize variables
    imgWidth = bossImg.getWidth();
    imgHeight = bossImg.getHeight();
    x = boss.getPosition().x();
    y = boss.getPosition().y();
    diameter = 2*boss.getRadius();

    // scale by smallest image length.
    scaleFactor = 2*(diameter/imgHeight);
    if (imgWidth < imgHeight) {
      scaleFactor = 2*(diameter/imgWidth);
    }
    
    // draw enemy
    Inf101Graphics.drawCenteredImage(Canvas, bossImg, x, y, scaleFactor);

    // draw hitbox
    /* x = boss.getPosition().x() - boss.getRadius();
    y = boss.getPosition().y() - boss.getRadius();
    diameter = 2*boss.getRadius();
    
    enemyBall = new Ellipse2D.Double(x, y, diameter, diameter);
    
    Canvas.setColor(Color.getSpriteColor('r'));
    Canvas.fill(enemyBall); */
  
  }

  private static void drawBulletsOnField(Graphics2D Canvas, Iterable<Bullets> bulletList, ColorTheme Color) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    Ellipse2D bulletHitbox;
    BufferedImage bulletImg;
    
    for (Bullets bullet : bulletList) {
      // get image for corrosponding bullet variation.
      // also depends on bullet owners variation to determine color.
      bulletImg = Inf101Graphics.loadImageFromResources("/playerBullet.png");
      if (bullet.getVariation().equals(SpriteVariations.arrow)) {
        bulletImg = Inf101Graphics.loadImageFromResources("/arrowMagenta.png");
      }
      else if (bullet.getVariation().equals(SpriteVariations.circleSmall)) {
        if (bullet.getBulletOwner().equals(SpriteVariations.yokai1)) {
          bulletImg = Inf101Graphics.loadImageFromResources("/circleSmallBlue.png");
        }
        else if (bullet.getBulletOwner().equals(SpriteVariations.boss4)) {
          bulletImg = Inf101Graphics.loadImageFromResources("/circleSmallRed.png");
        }
        else if (bullet.getBulletOwner().equals(SpriteVariations.boss5)) {
          bulletImg = Inf101Graphics.loadImageFromResources("/circleSmallRed.png");
        }
      }
      else if (bullet.getVariation().equals(SpriteVariations.ellipseLarge)) {
        bulletImg = Inf101Graphics.loadImageFromResources("/Yokai1.png");
        if (bullet.getBulletOwner().equals(SpriteVariations.yokai2)) {
          bulletImg = Inf101Graphics.loadImageFromResources("/ellipseLargeRed.png");
        }
        else if (bullet.getBulletOwner().equals(SpriteVariations.boss4)) {
          bulletImg = Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png");
        }
        else if (bullet.getBulletOwner().equals(SpriteVariations.boss5)) {
          bulletImg = Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png");
        }
      }
      
      // initialize variables
      imgWidth = bulletImg.getWidth();
      imgHeight = bulletImg.getHeight();
      x = bullet.getPosition().x();
      y = bullet.getPosition().y();
      diameter = 2*bullet.getRadius();

      // scale by smallest image length.
      scaleFactor = 2*(diameter/imgHeight);
      if (imgWidth < imgHeight) {
        scaleFactor = 2*(diameter/imgWidth);
      }
      
      // draw bullet
      Inf101Graphics.drawCenteredImage(Canvas, bulletImg, x, y, scaleFactor);

      // draw hitbox
      x -= bullet.getRadius();
      y -= bullet.getRadius();
      bulletHitbox = new Ellipse2D.Double(x, y, diameter, diameter);
      Canvas.setColor(Color.getSpriteColor('g'));
      Canvas.fill(bulletHitbox);

    }

  }
  
  private static void drawField(Graphics2D Canvas, Rectangle2D fieldRect, ColorTheme Color) {
    double x = (double) fieldRect.getX();
    double y = (double) fieldRect.getY();
    double width = fieldRect.getWidth();
    double height = fieldRect.getHeight();
    
    Canvas.setColor(Color.getFieldColor());
    Canvas.fill(fieldRect);

    // draw line of collection
    Canvas.setColor(Color.getSpriteColor('r'));
    Line2D collectionLine = new Line2D.Double(x, y + (height*(0.28)), x + width, y + (height*(0.28)));
    Canvas.draw(collectionLine);
    
  }

  private static void drawStatistics(Graphics2D Canvas, Rectangle2D statRect, ColorTheme color, ViewableDanmakuModel model) {
    // fps counter
    double x = statRect.getX();
    double y = statRect.getY();
    
    Canvas.setColor(Color.RED);
    Canvas.setFont(new Font("Arial", Font.BOLD, 18));
    Inf101Graphics.drawCenteredString(Canvas, "fps: " + model.getFPSValue(), 2*x, 2*y, 50, 50);
    
    // current stage
    x = model.getDimension().getFieldX() + model.getDimension().width();
    y = model.getDimension().getFieldY();
    double Width = statRect.getWidth();
    double HeightStageBoard = statRect.getHeight()*0.2;

    Canvas.setColor(Color.DARK_GRAY);
    Canvas.setFont(new Font("Arial", Font.BOLD, 25));
    Inf101Graphics.drawCenteredString(Canvas, "Stage: " + model.getCurrentStage(), x, y, Width, HeightStageBoard);
    
  }

  private static void drawMenuScreen(Graphics2D Canvas, Rectangle2D MenuBackground, ColorTheme color, GameState gameStatus) {
    Rectangle2D title = new Rectangle2D.Double(0, 0.2*MenuBackground.getHeight(), MenuBackground.getWidth(), 100);
    Rectangle2D pressKey = new Rectangle2D.Double(0, 0.6*MenuBackground.getHeight(), MenuBackground.getWidth(), 60);
    if (gameStatus.equals(GameState.GAME_MENU)) {
      Canvas.setColor(color.getMenuScreenColor("back"));
      Canvas.fill(MenuBackground);
      
      Canvas.setColor(color.getMenuScreenColor("title"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 100));
      Inf101Graphics.drawCenteredString(Canvas, "Danmaku", title);

      Canvas.setColor(color.getMenuScreenColor("key"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 40));
      Inf101Graphics.drawCenteredString(Canvas, "Press enter to start", pressKey);
    }
  }

  private static void drawPauseScreen(Graphics2D Canvas, Rectangle2D MenuBackground, ColorTheme color, GameState gameStatus) {
    Rectangle2D title = new Rectangle2D.Double(0, 0.2*MenuBackground.getHeight(), MenuBackground.getWidth(), 100);
    Rectangle2D pressContinue = new Rectangle2D.Double(0, 0.6*MenuBackground.getHeight(), MenuBackground.getWidth(), 60);
    Rectangle2D pressMenu = new Rectangle2D.Double(0, 0.7*MenuBackground.getHeight(), MenuBackground.getWidth(), 60);
    if (gameStatus.equals(GameState.PAUSE_GAME)) {
      Canvas.setColor(color.getPauseScreenColor("back"));
      Canvas.fill(MenuBackground);
      
      Canvas.setColor(color.getPauseScreenColor("paused"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 100));
      Inf101Graphics.drawCenteredString(Canvas, "Paused", title);

      Canvas.setColor(color.getPauseScreenColor("key"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 40));
      Inf101Graphics.drawCenteredString(Canvas, "Press enter to continue", pressContinue);
      Inf101Graphics.drawCenteredString(Canvas, "Press backspace to menu", pressMenu);
    }
  }

  private static void drawEndGameScreen(Graphics2D Canvas, Rectangle2D endGameBackground, ColorTheme color, GameState gameStatus) {
    Rectangle2D pressContinue = new Rectangle2D.Double(0, 0.6*endGameBackground.getHeight(), endGameBackground.getWidth(), 30);
    Rectangle2D pressMenu = new Rectangle2D.Double(0, 0.7*endGameBackground.getHeight(), endGameBackground.getWidth(), 30);
    if (gameStatus.equals(GameState.GAME_OVER)) {

      Canvas.setColor(color.getGameOverColor("back"));
      Canvas.fill(endGameBackground);
      Canvas.setColor(color.getGameOverColor("gameover"));
      Canvas.setFont(new Font("Times New Roman", Font.PLAIN, 69));

      Inf101Graphics.drawCenteredString(Canvas, "GAME OVER", endGameBackground);

      Canvas.setColor(color.getGameOverColor("key"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 20));
      Inf101Graphics.drawCenteredString(Canvas, "Retry? (enter)", pressContinue);
      Inf101Graphics.drawCenteredString(Canvas, "Go back to menu? (backspace)", pressMenu);
    }
  } 
  
  
  
}
