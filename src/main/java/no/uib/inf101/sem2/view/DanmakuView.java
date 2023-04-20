package no.uib.inf101.sem2.view;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private double scrollY;
  
  public DanmakuView(ViewableDanmakuModel Model) {
    this.setFocusable(true);
    this.Model = Model;
    
    // prefered field dimensions
    int x = this.Model.getDimension().getFieldX();
    int y = this.Model.getDimension().getFieldY();
    int Width = this.Model.getDimension().width();
    int Height = this.Model.getDimension().height();
    this.scrollY = 2*y;
    // prefered scoreboard dimension
    this.SCOREBOARDWIDTH = WFactor*(Width);
    this.SCOREBOARDHEIGHT = Height + 2*y;
    
    int preferredWidth = (int) (Width + 2*x + this.SCOREBOARDWIDTH);
    int preferredHeight = (int) (Height + 2*y);
    
    this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    this.setColor = new DefaultColorTheme();
    this.setBackground(this.setColor.getBackgroundColor());

    // load images before game start
    loadBulletImages();
    loadCharacterImages();
    loadFieldImages();
  }    
  
  @Override
  public void paintComponent(Graphics GraphComp) {
    super.paintComponent(GraphComp);
    Graphics2D Comp2D = (Graphics2D) GraphComp;
    drawGame(Comp2D);
  }

  // load image files for Sprites before drawing game.
  // preloading images may improve performance
  // sources used: https://www.baeldung.com/java-hashmap
  // and https://stackoverflow.com/questions/49604389/hashmap-with-2-keys. 
  // special thanks to I. Ahmed on stackoverflow
  private static Map<SpriteVariations, Map<SpriteVariations, BufferedImage>> bulletMap = new HashMap<>();
  private static Map<SpriteVariations, BufferedImage> bulletImages = new HashMap<>();
  /**
   * loadBulletImages loads the image of a certain bullet,
   * depending on bullet variation and bullets owner
   */
  private static void loadBulletImages() {
    /* Bullets format (bullet variation, sprite owner) */
    // arrow
    bulletImages.put(SpriteVariations.player1, 
    Inf101Graphics.loadImageFromResources("/arrowMagenta.png"));
    bulletMap.put(SpriteVariations.arrow, bulletImages);
    // ofuda
    bulletImages.put(SpriteVariations.player2, 
    Inf101Graphics.loadImageFromResources("/playerBullet.PNG"));
    bulletMap.put(SpriteVariations.ofuda, bulletImages);
    // circleSmall
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.yokai1, 
    Inf101Graphics.loadImageFromResources("/circleSmallBlue.png"));
    bulletImages.put(SpriteVariations.boss4, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletImages.put(SpriteVariations.boss5, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletMap.put(SpriteVariations.circleSmall, bulletImages);
    // ellipseLarge
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.yokai2, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeRed.png"));
    bulletImages.put(SpriteVariations.boss4, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png"));
    bulletImages.put(SpriteVariations.boss5, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png"));
    bulletMap.put(SpriteVariations.ellipseLarge, bulletImages);

  }

  /**
   * getBulletImage gets image for bullet using variation and ownership to determine 
   * the image.
   */
  private static BufferedImage getBulletImage(SpriteVariations bulletVar, SpriteVariations Owner) {
    return bulletMap.get(bulletVar).get(Owner);
  }

  private static Map<SpriteVariations, Map<String, BufferedImage>> CharacterMap = new HashMap<>();
  private static Map<String, BufferedImage> CharacterImages = new HashMap<>();
  /**
   * loadCharacterImages loads the image of players, enemies and bosses.
   * images with movements depends on sprite velocity (only checking x-axis movement by strings "right", "left" or "still"
   * or for actions like super attacks by bosses using "super")
   */
  private static void loadCharacterImages() {
    AffineTransform tx;
    AffineTransformOp op;
    List<BufferedImage> flipImages = new ArrayList<>();
    // boss images
    BufferedImage boss4Right = Inf101Graphics.loadImageFromResources("/MoFBoss4Left.png");
    BufferedImage boss5Left = Inf101Graphics.loadImageFromResources("/MoFBoss5Right.png");
    flipImages.add(boss4Right);
    flipImages.add(boss5Left);
    // flip images horisontally
    for (int i = flipImages.size() - 1; i >= 0; i--) {
      BufferedImage image = flipImages.get(i);
      tx = AffineTransform.getScaleInstance(-1, 1);
      tx.translate(-image.getWidth(null), 0);
      op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      image = op.filter(image, null);
      flipImages.set(i, image);
    }
    /* character format (Sprite variation, velocity double) */
    // player1
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/player1.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/player1Left.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/player1Right.png"));
    CharacterMap.put(SpriteVariations.player1, CharacterImages);
    // player2
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/player2.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/player2Left.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/player2Right.png"));
    CharacterMap.put(SpriteVariations.player2, CharacterImages);
    // boss4
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/MoFBoss4.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/MoFBoss4left.png"));
    CharacterImages.put("right", flipImages.get(0));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/MoFBoss4super.png"));
    CharacterMap.put(SpriteVariations.boss4, CharacterImages);
    // boss5
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/MoFBoss5.png"));
    CharacterImages.put("left", flipImages.get(1));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/MoFBoss5Right.png"));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/MoFBoss5super.png"));
    CharacterMap.put(SpriteVariations.boss5, CharacterImages);
    // yokai1
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/Yokai1.png"));
    CharacterMap.put(SpriteVariations.yokai1, CharacterImages);
    // yokai2
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/Yokai2.png"));
    CharacterMap.put(SpriteVariations.yokai2, CharacterImages);
  }
 
  /**
   * getCharacterImage gets image for bullet using variation and ownership to determine 
   * the image.
   */
  private static BufferedImage getCharacterImage(SpriteVariations Character, String action) {
    return CharacterMap.get(Character).get(action);
  }

  private static Map<Integer, Map<String, BufferedImage>> FieldMap = new HashMap<>();
  private static Map<String, BufferedImage> FieldImages = new HashMap<>();
  /**
   * loadFieldImages loads the image of backgrounds and foregrounds.
   * int value is stage number and String determines stage background or boss background and foreground.
   */
  private static void loadFieldImages() {
    /* background format (stage int, boss/stage String) */
    // stage 1
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/MoFStage4background.PNG"));
    // stage boss 1
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/MoFBoss4Background.PNG"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/MoFBoss4Foreground.png"));
    FieldMap.put(1, FieldImages);
    // stage 2
    FieldImages = new HashMap<>();
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/MoFStage5background.PNG"));
    // stage boss 2
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/MoFBoss5Background.PNG"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/MoFBoss5Foreground.png"));
    FieldMap.put(2, FieldImages);
  }
 
  /**
   * getFieldImage gets image for backgrounds and foregrounds using stage (int) and type (stage or boss)
   */
  private static BufferedImage getFieldImage(int stage, String stageType) {
    return FieldMap.get(stage).get(stageType);
  }
  
  private void drawGame(Graphics2D Canvas) {
    // Field
    double xField = (double) Model.getDimension().getFieldX();
    double yField = (double) Model.getDimension().getFieldY();
    double widthField = (this.getWidth() - this.SCOREBOARDWIDTH) - 2*xField;
    double heightField = this.getHeight() - 2*yField;
    
    Rectangle2D fieldRect = new Rectangle2D.Double(xField, yField, widthField, heightField);

    // statistics
    double xStat = Model.getDimension().getFieldX();
    double yStat = Model.getDimension().getFieldY();
    double widthStat = this.SCOREBOARDWIDTH;
    double heightStat = this.SCOREBOARDHEIGHT;

    Rectangle2D statRect = new Rectangle2D.Double(xStat, yStat, widthStat, heightStat);

    // menu screen
    Rectangle2D drawScreenRect = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());

    drawMenuScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    if (!Model.getGameState().equals(GameState.GAME_MENU)) {
      // draw bullet field
      drawField(Canvas, fieldRect, this.scrollY, Model, setColor);
      // draw player on field
      drawPlayer(Canvas, Model.getPlayer(), setColor, false);
      // draw enemies on field
      drawEnemy(Canvas, Model.getEnemiesOnField(), setColor, false);
      // draw bosses on field
      if (Model.getBossEnemyOnField() != null) {
        drawBoss(Canvas, Model.getBossEnemyOnField(), Model.getBossAttackType(), setColor, false);
      }
      // draw bullets on field
      drawBulletsOnField(Canvas, Model.getBulletsOnField(), setColor, false);
      // draw field frame
      if (!(Model.getGameState().equals(GameState.PAUSE_GAME) || Model.getGameState().equals(GameState.GAME_OVER))) {
        // stop scrolling when game is pause or over.
        this.scrollY += 0.8; // scroll speed
        if (this.scrollY > fieldRect.getHeight()) {
          this.scrollY = 2*fieldRect.getY();
        }
      }
      drawFieldFrame(Canvas, fieldRect, setColor);
      // draw statistics like score or boss healthbar...
      drawStatistics(Canvas, statRect, setColor, Model);
      // draw pause screen
      drawPauseScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
      // draw game over screen when player has no lifes
      drawEndGameScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    }
  }
  
  private static void drawPlayer(Graphics2D Canvas, Player player, ColorTheme Color, boolean hasHitbox) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    Ellipse2D playerBall;
    BufferedImage playerImg;

    // get image for player
    String action = "still";
    if (player.getVelocity().x() > 0) {
      action = "right";
    }
    else if (player.getVelocity().x() < 0) {
      action = "left";
    }
    playerImg = getCharacterImage(player.getVariation(), action);
    
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
    
    // draw hitbox
    if (hasHitbox) {
      x -= player.getRadius();
      y -= player.getRadius();
      playerBall = new Ellipse2D.Double(x, y, diameter, diameter);
      Canvas.setColor(Color.getSpriteColor('r'));
      Canvas.fill(playerBall);
    }
    
  }

  private static void drawEnemy(Graphics2D Canvas, Iterable<Enemies> enemies, ColorTheme Color, boolean hasHitbox) {
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
      enemyImg = getCharacterImage(enemy.getVariation(), "still");
      
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
      if (hasHitbox) {
        x -= enemy.getRadius();
        y -= enemy.getRadius();
        enemyBall = new Ellipse2D.Double(x, y, diameter, diameter);
        Canvas.setColor(Color.getSpriteColor('b'));
        Canvas.fill(enemyBall);
      }

    }
  }

  private static void drawBoss(Graphics2D Canvas, Enemies boss, boolean activateSuper, ColorTheme Color, boolean hasHitbox) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    Ellipse2D enemyBall;
    BufferedImage bossImg;

    // get image for corrosponding boss variation 
    String action = "still";
    if (boss.getVelocity().x() > 0) {
      action = "right";
    }
    else if (boss.getVelocity().x() < 0) {
      action = "left";
    }
    // using super?
    if (activateSuper) {
      action = "super";
    }
    bossImg = getCharacterImage(boss.getVariation(), action);
    
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
    
    // draw boss
    Inf101Graphics.drawCenteredImage(Canvas, bossImg, x, y, scaleFactor);

    // draw hitbox
    if (hasHitbox) {
      x -= boss.getRadius();
      y -= boss.getRadius();
      enemyBall = new Ellipse2D.Double(x, y, diameter, diameter);
      Canvas.setColor(Color.getSpriteColor('r'));
      Canvas.fill(enemyBall);
    }
    
  }

  private static void drawBulletsOnField(Graphics2D Canvas, Iterable<Bullets> bulletList, ColorTheme Color, boolean hasHitbox) {
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
      bulletImg = getBulletImage(SpriteVariations.arrow, SpriteVariations.player1);
      if (bullet.getVariation() != null && bullet.getBulletOwner() != null) {
        bulletImg = getBulletImage(bullet.getVariation(), bullet.getBulletOwner());
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
      // get rotation angle from positive y-axis (y points down).
      // math: angle = arctan(det / dot)
      Vector yUnit = new Vector(0, -1, 1);
      double dotProd = bullet.getAimVector().x()*yUnit.x() + bullet.getAimVector().y()*yUnit.y();
      double determinant = bullet.getAimVector().y()*yUnit.x() - bullet.getAimVector().x()*yUnit.y();
      double angle = Math.atan2(determinant, dotProd);
      // draw bullet
      Inf101Graphics.drawCenteredImage(Canvas, bulletImg, x, y, scaleFactor, angle);

      // draw hitbox
      if (hasHitbox) {
        x -= bullet.getRadius();
        y -= bullet.getRadius();
        bulletHitbox = new Ellipse2D.Double(x, y, diameter, diameter);
        Canvas.setColor(Color.getSpriteColor('g'));
        Canvas.fill(bulletHitbox);
      }

    }

  }
  
  private static void drawField(Graphics2D Canvas, Rectangle2D fieldRect, double scrollY, ViewableDanmakuModel model, ColorTheme Color) {
    double x = (double) fieldRect.getX();
    double y = (double) fieldRect.getY();
    double imgBackHeight;
    double imgForeHeight = 1;
    double width = fieldRect.getWidth();
    double height = fieldRect.getHeight();
    double scaleFactor = 1;
    double foreScaleFactor = 1;
    BufferedImage fieldBackImg;
    BufferedImage fieldForeImg = null;

    Canvas.setColor(Color.getSpriteColor('r'));
    Canvas.fill(fieldRect);

    // update background
    fieldBackImg = getFieldImage(model.getCurrentStage(), "stage");
    if (model.getBossEnemyOnField() != null) {
      fieldBackImg = getFieldImage(model.getCurrentStage(), "back");
      fieldForeImg = getFieldImage(model.getCurrentStage(), "fore");

      imgForeHeight = fieldForeImg.getHeight();
    }
    imgBackHeight = fieldBackImg.getHeight();

    // scale image to field size
    scaleFactor = (imgBackHeight / width);
    if (height < imgBackHeight) {
      scaleFactor = (height / imgBackHeight);
    }
    foreScaleFactor = (imgBackHeight / imgForeHeight);
    if (imgBackHeight < imgForeHeight) {
      foreScaleFactor = (imgForeHeight / imgBackHeight);
    }
    // scrolling variable
    double scrollDown = scrollY - (imgBackHeight*scaleFactor);
    double scrollDown2 = scrollY;
    // draw stage background
    Inf101Graphics.drawImage(Canvas, fieldBackImg, x, scrollDown, scaleFactor);
    Inf101Graphics.drawImage(Canvas, fieldBackImg, x, scrollDown2, scaleFactor);
    // extra backgrounds (prevent bakcground scrolling off)
    Inf101Graphics.drawImage(Canvas, fieldBackImg, x, scrollDown2 - 2*(imgBackHeight*scaleFactor), scaleFactor);
    Inf101Graphics.drawImage(Canvas, fieldBackImg, x, scrollDown2 + (imgBackHeight*scaleFactor), scaleFactor);
    // if boss fight, stop scrolling
    if (model.getBossEnemyOnField() != null) {
      scrollDown = y;
      scrollDown2 = y;
      // draw boss background
      Inf101Graphics.drawImage(Canvas, fieldBackImg, x, scrollDown, scaleFactor);
      Inf101Graphics.drawImage(Canvas, fieldForeImg, x, scrollDown2, foreScaleFactor);
    }
    // draw line of collection
    Canvas.setColor(Color.getSpriteColor('r'));
    Line2D collectionLine = new Line2D.Double(x, y + (height*(0.28)), (double) fieldRect.getX() + width, y + (height*(0.28)));
    Canvas.draw(collectionLine);
    
  }

  private static void drawFieldFrame(Graphics2D Canvas, Rectangle2D fieldRect, ColorTheme color) {
    // left side
    double leftX = 0;
    double leftY = 0;
    double leftWidth = fieldRect.getX() + 1;
    double leftHeight = 2*fieldRect.getY() + fieldRect.getHeight();
    // right side
    double rightX = fieldRect.getX() - 1 + fieldRect.getWidth();
    double rightY = 0;
    double rightWidth = 7*leftWidth;
    double rightHeight = leftHeight;
    // top
    double topX = leftWidth;
    double topY = 0;
    double topWidth = fieldRect.getWidth();
    double topHeight = fieldRect.getY();
    // bottom
    double bottomX = topX;
    double bottomY = fieldRect.getY() + fieldRect.getHeight();
    double bottomWidth = topWidth;
    double bottomHeight = topHeight;

    Rectangle2D leftSide = new Rectangle2D.Double(leftX, leftY, leftWidth, leftHeight);
    Rectangle2D rightSide = new Rectangle2D.Double(rightX, rightY, rightWidth, rightHeight);
    Rectangle2D top = new Rectangle2D.Double(topX, topY, topWidth, topHeight);
    Rectangle2D bottom = new Rectangle2D.Double(bottomX, bottomY, bottomWidth, bottomHeight);
    Canvas.setColor(color.getFieldColor());
    Canvas.fill(leftSide);
    Canvas.fill(rightSide);
    Canvas.fill(top);
    Canvas.fill(bottom);
    
  }

  private static void drawStatistics(Graphics2D Canvas, Rectangle2D statRect, ColorTheme color, ViewableDanmakuModel model) {
    // fps counter
    double x = statRect.getX();
    double y = statRect.getY();
    double screenWidth = statRect.getWidth() - 1.5*x;
    double screenHeight = statRect.getHeight();
    
    Canvas.setColor(Color.RED);
    Canvas.setFont(new Font("Arial", Font.BOLD, 18));
    Inf101Graphics.drawCenteredString(Canvas, "fps: " + model.getFPSValue(), 2*screenWidth, screenHeight - 2*y, 50, 50);
    
    // current stage
    x = 4*model.getDimension().getFieldX() + model.getDimension().width();
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
