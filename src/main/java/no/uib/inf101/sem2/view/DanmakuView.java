package no.uib.inf101.sem2.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import no.uib.inf101.sem2.grid.Vector;
import no.uib.inf101.sem2.model.GameState;
import no.uib.inf101.sem2.model.danmakus.Bullets;
import no.uib.inf101.sem2.model.danmakus.Consumables;
import no.uib.inf101.sem2.model.danmakus.Enemies;
import no.uib.inf101.sem2.model.danmakus.Player;
import no.uib.inf101.sem2.model.danmakus.SpriteType;
import no.uib.inf101.sem2.model.danmakus.SpriteVariations;

public class DanmakuView extends JPanel{
  
  private final ViewableDanmakuModel Model;
  private final ColorTheme setColor;
  private double SCOREBOARDWIDTH;
  private static final double WFactor = 0.7;
  private double SCOREBOARDHEIGHT;
  private double scrollY;
  private double rotateHitbox;
  // number incrementer
  private static final Pattern NUMBER = Pattern.compile("\\d+");
  
  /**
   * DanmakuView is the main contructor for handling all data regarding graphics. 
   */
  public DanmakuView(ViewableDanmakuModel Model) {
    this.setFocusable(true);
    this.Model = Model;
    
    // prefered field dimensions
    int x = this.Model.getDimension().getFieldX();
    int y = this.Model.getDimension().getFieldY();
    int Width = this.Model.getDimension().width();
    int Height = this.Model.getDimension().height();
    this.scrollY = 0;
    this.rotateHitbox = 0;
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
    loadCollectibleImages();
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
  private Map<SpriteVariations, Map<SpriteVariations, BufferedImage>> bulletMap = new HashMap<>();
  private Map<SpriteVariations, BufferedImage> bulletImages = new HashMap<>();

  /**
   * loadBulletImages loads the image of a certain bullet,
   * depending on bullet variation and bullets owner.
   * Bullets format (bullet variation, sprite owner).
   */
  private void loadBulletImages() {
    // arrow
    bulletImages.put(SpriteVariations.player1, 
    Inf101Graphics.loadImageFromResources("/arrowMagenta.png"));
    bulletMap.put(SpriteVariations.arrow, bulletImages);
    // ofuda
    bulletImages.put(SpriteVariations.player2, 
    Inf101Graphics.loadImageFromResources("/ofudaP2.png"));
    bulletMap.put(SpriteVariations.ofuda, bulletImages);
    // ofudaHoming
    bulletImages.put(SpriteVariations.player2, 
    Inf101Graphics.loadImageFromResources("/ofudaHoming.png"));
    bulletMap.put(SpriteVariations.ofudaHoming, bulletImages);
    // circleSmall
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.fairy, 
    Inf101Graphics.loadImageFromResources("/circleSmallBlue.png"));
    bulletImages.put(SpriteVariations.MoFboss1, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletImages.put(SpriteVariations.MoFboss2, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletImages.put(SpriteVariations.SubAnimBoss3, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletImages.put(SpriteVariations.SubAnimBoss4, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletImages.put(SpriteVariations.SubAnimBoss5, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletImages.put(SpriteVariations.MoFExtraBoss, 
    Inf101Graphics.loadImageFromResources("/circleSmallRed.png"));
    bulletMap.put(SpriteVariations.circleSmall, bulletImages);
    // ellipseLarge
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.highFairy, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeRed.png"));
    bulletImages.put(SpriteVariations.MoFboss1, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png"));
    bulletImages.put(SpriteVariations.MoFboss2, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png"));
    bulletImages.put(SpriteVariations.SubAnimBoss3, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png"));
    bulletImages.put(SpriteVariations.SubAnimBoss4, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeMagenta.png"));
    bulletImages.put(SpriteVariations.SubAnimBoss5, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeCyan.png"));
    bulletImages.put(SpriteVariations.MoFExtraBoss, 
    Inf101Graphics.loadImageFromResources("/ellipseLargeMagenta.png"));
    bulletMap.put(SpriteVariations.ellipseLarge, bulletImages);
    // star 
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.player1, 
    Inf101Graphics.loadImageFromResources("/starMagenta.png"));
    bulletMap.put(SpriteVariations.star, bulletImages);
    // ballLarge
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.yokai, 
    Inf101Graphics.loadImageFromResources("/ballLargeRed.png"));
    bulletImages.put(SpriteVariations.Trancendent, 
    Inf101Graphics.loadImageFromResources("/ballLargeGreen.png"));
    bulletMap.put(SpriteVariations.ballLarge, bulletImages);
    // heart
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.SubAnimBoss5, 
    Inf101Graphics.loadImageFromResources("/heartGreen.png"));
    bulletImages.put(SpriteVariations.guardianFairy, 
    Inf101Graphics.loadImageFromResources("/heartPink.png"));
    bulletMap.put(SpriteVariations.heart, bulletImages);
    // knife
    bulletImages = new HashMap<>();
    bulletImages.put(SpriteVariations.cursedFairy, 
    Inf101Graphics.loadImageFromResources("/knifeBlue.png"));
    bulletMap.put(SpriteVariations.knife, bulletImages);
    // pellet

    // note

    // specials: 

  }

  /**
   * getBulletImage gets image for bullet using variation and ownership to determine 
   * the image.
   */
  private BufferedImage getBulletImage(SpriteVariations bulletVar, SpriteVariations Owner) {
    return bulletMap.get(bulletVar).get(Owner);
  }

  private Map<SpriteType, Map<SpriteVariations, BufferedImage>> collectibleMap = new HashMap<>();
  private Map<SpriteVariations, BufferedImage> itemImages = new HashMap<>();

  /**
   * loadBulletImages loads the image of a certain bullet,
   * depending on bullet variation and bullets owner.
   * Bullets format (bullet variation, sprite owner).
   */
  private void loadCollectibleImages() {
    // power
    itemImages.put(SpriteVariations.powerSmall, 
    Inf101Graphics.loadImageFromResources("/powerSmall.png"));
    itemImages.put(SpriteVariations.powerMedium, 
    Inf101Graphics.loadImageFromResources("/powerMedium.PNG"));
    itemImages.put(SpriteVariations.powerLarge, 
    Inf101Graphics.loadImageFromResources("/PowerLarge.PNG"));
    collectibleMap.put(SpriteType.Power, itemImages);
    // faith
    itemImages = new HashMap<>();
    itemImages.put(SpriteVariations.faithSmall, 
    Inf101Graphics.loadImageFromResources("/scoreSmall.png"));
    itemImages.put(SpriteVariations.faithMedium, 
    Inf101Graphics.loadImageFromResources("/scoreMedium.png"));
    itemImages.put(SpriteVariations.faithLarge, 
    Inf101Graphics.loadImageFromResources("/scoreLarge.png"));
    collectibleMap.put(SpriteType.Faith, itemImages);
    // extend
    itemImages = new HashMap<>();
    itemImages.put(SpriteVariations.extend, 
    Inf101Graphics.loadImageFromResources("/extraLife.PNG"));
    collectibleMap.put(SpriteType.Extend, itemImages);

  }

  /**
   * getBulletImage gets image for bullet using variation and ownership to determine 
   * the image.
   */
  private BufferedImage getCollectibleImage(SpriteType type, SpriteVariations variation) {
    return collectibleMap.get(type).get(variation);
  }

  private Map<SpriteVariations, Map<String, BufferedImage>> CharacterMap = new HashMap<>();
  private Map<String, BufferedImage> CharacterImages = new HashMap<>();

  /**
   * loadCharacterImages loads the image of players, enemies and bosses.
   * images with movements depends on sprite velocity (only checking x-axis movement by strings "right", "left" or "still"
   * or for actions like super attacks by bosses using "super").
   * Character format (Sprite variation, velocity String).
   */
  private void loadCharacterImages() {
    // boss images
    BufferedImage boss1Right = Inf101Graphics.loadImageFromResources("/MoFBoss1Left.png");
    BufferedImage boss2Left = Inf101Graphics.loadImageFromResources("/MoFBoss2Right.png");
    BufferedImage SubAnimBoss3Right = Inf101Graphics.loadImageFromResources("/SubAnimBoss3Left.png");
    BufferedImage SubAnimBoss4Right = Inf101Graphics.loadImageFromResources("/SubAnimBoss4Left.png");
    BufferedImage bossExtraLeft = Inf101Graphics.loadImageFromResources("/MoFExtraBossRight.png");
    
    // player1
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/player1.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/player1Left.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/player1Right.png"));
    CharacterImages.put("shift", Inf101Graphics.loadImageFromResources("/playerHitbox.png"));
      // select screen image
    CharacterImages.put("select", Inf101Graphics.loadImageFromResources("/Th175Marisa.png"));
    CharacterMap.put(SpriteVariations.player1, CharacterImages);
    // player2
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/player2.PNG"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/player2Left.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/player2Right.png"));
    CharacterImages.put("shift", Inf101Graphics.loadImageFromResources("/playerHitbox.png"));
      // select screen image
    CharacterImages.put("select", Inf101Graphics.loadImageFromResources("/Th175Reimu.png"));
    CharacterMap.put(SpriteVariations.player2, CharacterImages);
    // boss stage 1
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/MoFBoss1.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/MoFBoss1Left.png"));
    CharacterImages.put("right", imageFliper(boss1Right));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/MoFBoss1Super.png"));
    CharacterMap.put(SpriteVariations.MoFboss1, CharacterImages);
    // boss stage 2
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/MoFBoss2.png"));
    CharacterImages.put("left", imageFliper(boss2Left));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/MoFBoss2Right.png"));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/MoFBoss2Super.png"));
    CharacterMap.put(SpriteVariations.MoFboss2, CharacterImages);
    // boss stage 3
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/SubAnimBoss3.png"));
    CharacterImages.put("spawning", Inf101Graphics.loadImageFromResources("/SubAnimBoss3Cat.png"));
    CharacterImages.put("preBattle", Inf101Graphics.loadImageFromResources("/SubAnimBoss3CatRight.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/SubAnimBoss3Left.png"));
    CharacterImages.put("right", imageFliper(SubAnimBoss3Right));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/SubAnimBoss3.png"));
    CharacterMap.put(SpriteVariations.SubAnimBoss3, CharacterImages);
    // boss stage 4
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/SubAnimBoss4.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/SubAnimBoss4Left.png"));
    CharacterImages.put("right", imageFliper(SubAnimBoss4Right));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/SubAnimBoss4Super.png"));
    CharacterMap.put(SpriteVariations.SubAnimBoss4, CharacterImages);
    // sub boss stage 4
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/EoSDsubBoss4.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/EoSDsubBoss4Left.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/EoSDsubBoss4Right.png"));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/EoSDsubBoss4Super.png"));
    CharacterMap.put(SpriteVariations.EoSDsubBoss4, CharacterImages);
    // boss stage 5
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/SubAnimBoss5.png"));
    CharacterImages.put("left", Inf101Graphics.loadImageFromResources("/SubAnimBoss5Left.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/SubAnimBoss5Right.png"));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/SubAnimBoss5Super.png"));
    CharacterMap.put(SpriteVariations.SubAnimBoss5, CharacterImages);
    // boss stage 6
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/MoFExtraBoss.png"));
    CharacterImages.put("left", imageFliper(bossExtraLeft));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/MoFExtraBossRight.png"));
    CharacterImages.put("super", Inf101Graphics.loadImageFromResources("/MoFExtraBossSuper.png"));
    CharacterMap.put(SpriteVariations.MoFExtraBoss, CharacterImages);
    // fairy
    BufferedImage fairyleft1 = Inf101Graphics.loadImageFromResources("/fairyBlueRight.png");
    BufferedImage fairyleft2 = Inf101Graphics.loadImageFromResources("/fairyRedRight.png");
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/fairyBlue.png"));
    CharacterImages.put("still2", Inf101Graphics.loadImageFromResources("/fairyRed.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/fairyBlueRight.png"));
    CharacterImages.put("right2", Inf101Graphics.loadImageFromResources("/fairyRedRight.png"));
    CharacterImages.put("left", imageFliper(fairyleft1));
    CharacterImages.put("left2", imageFliper(fairyleft2));
    CharacterMap.put(SpriteVariations.fairy, CharacterImages);
    // high fairy
    BufferedImage highFairyleft = Inf101Graphics.loadImageFromResources("/highFairyRight.png");
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/highFairy.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/highFairyRight.png"));
    CharacterImages.put("left", imageFliper(highFairyleft));
    CharacterMap.put(SpriteVariations.highFairy, CharacterImages);
    // guardian fairy
    BufferedImage gFairyleft = Inf101Graphics.loadImageFromResources("/guardianFairyGreenRight.png");
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/guardianFairyGreen.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/guardianFairyGreenRight.png"));
    CharacterImages.put("left", imageFliper(gFairyleft));
    CharacterMap.put(SpriteVariations.guardianFairy, CharacterImages);
    // seasonal fairy
    BufferedImage sFairyleft = Inf101Graphics.loadImageFromResources("/seasonalFairyRight.png");
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/seasonalFairy.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/seasonalFairyRight.png"));
    CharacterImages.put("left", imageFliper(sFairyleft));
    CharacterMap.put(SpriteVariations.seasonalFairy, CharacterImages);
    // cursed fairy
    BufferedImage cFairyleft = Inf101Graphics.loadImageFromResources("/cursedFairyRight.png");
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/cursedFairy.png"));
    CharacterImages.put("right", Inf101Graphics.loadImageFromResources("/cursedFairyRight.png"));
    CharacterImages.put("left", imageFliper(cFairyleft));
    CharacterMap.put(SpriteVariations.cursedFairy, CharacterImages);
    // yokai
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/yokaiRed.png"));
    CharacterImages.put("still2", Inf101Graphics.loadImageFromResources("/yokaiGreen.png"));
    CharacterMap.put(SpriteVariations.yokai, CharacterImages);
    // Trancendent
    CharacterImages = new HashMap<>();
    CharacterImages.put("still", Inf101Graphics.loadImageFromResources("/Trancendent.png"));
    CharacterMap.put(SpriteVariations.Trancendent, CharacterImages);
  }

  private BufferedImage imageFliper(BufferedImage image) {
    AffineTransform tx;
    AffineTransformOp op;
    // flip images horisontally
    tx = AffineTransform.getScaleInstance(-1, 1);
    tx.translate(-image.getWidth(null), 0);
    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    image = op.filter(image, null);
    return image;
  }
 
  /**
   * getCharacterImage gets image for bullet using variation and ownership to determine 
   * the image.
   */
  private BufferedImage getCharacterImage(SpriteVariations Character, String action) {
    return CharacterMap.get(Character).get(action);
  }

  private Map<Integer, Map<String, BufferedImage>> FieldMap = new HashMap<>();
  private Map<String, BufferedImage> FieldImages = new HashMap<>();

  /**
   * loadFieldImages loads the image of backgrounds and foregrounds.
   * int value is stage number and String determines stage background or boss background and foreground.
   * background format (stage int, boss/stage String)
   */
  private void loadFieldImages() {
    // stage 1
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/MoFStage4Background.png"));
    // stage boss 1
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/MoFBoss4Background.png"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/MoFBoss5Foreground.png"));
    FieldMap.put(1, FieldImages);
    // stage 2
    FieldImages = new HashMap<>();
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/MoFStage5background.PNG"));
    // stage boss 2
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/MoFBoss5Background.PNG"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/MoFBoss5Foreground.png"));
    FieldMap.put(2, FieldImages);
    // stage 3
    FieldImages = new HashMap<>();
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/WBWCStage3Background.png"));
    // stage boss 3
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/SubAnimBoss3Background.png"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/SubAnimBoss3Foreground.png"));
    FieldMap.put(3, FieldImages);
    // stage 4
    FieldImages = new HashMap<>();
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/UFOStage4Background.png"));
    // stage boss 4
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/SubAnimBoss4Background.png"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/SubAnimBoss4Foreground.png"));
    FieldMap.put(4, FieldImages);
    // stage 5
    FieldImages = new HashMap<>();
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/UFOStage3Background.png"));
    // stage boss 5
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/impNightBoss5Background.png"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/MoFBoss5Foreground.png"));
    FieldMap.put(5, FieldImages);
    // stage 6
    FieldImages = new HashMap<>();
    FieldImages.put("stage", Inf101Graphics.loadImageFromResources("/impNightStage4Background.png"));
    // stage boss 6
    FieldImages.put("back", Inf101Graphics.loadImageFromResources("/MoFBoss6Background.png"));
    FieldImages.put("fore", Inf101Graphics.loadImageFromResources("/MoFBoss5Foreground.png"));
    FieldMap.put(6, FieldImages);
  }
 
  /**
   * getFieldImage gets image for backgrounds and foregrounds using stage (int) and type (stage or boss)
   */
  private BufferedImage getFieldImage(int stage, String stageType) {
    return FieldMap.get(stage).get(stageType);
  }
  
  private void drawGame(Graphics2D Canvas) {
    // Field
    double xField = (double) Model.getDimension().getFieldX();
    double yField = (double) Model.getDimension().getFieldY();
    double widthField = (this.getWidth() - this.SCOREBOARDWIDTH) - 2*xField;
    double heightField = this.getHeight() - 2*yField;
    
    Rectangle2D fieldRect = new Rectangle2D.Double(xField, yField, widthField, heightField);
    
    // backgroundSize
    double xBackground = 0.0;
    double yBackground = 0.0;
    double widthBackground = (this.getWidth() - this.SCOREBOARDWIDTH);
    double heightBackground = this.getHeight();

    Rectangle2D backgroundRect = new Rectangle2D.Double(xBackground, yBackground, widthBackground, heightBackground);

    // statistics
    double xStat = Model.getDimension().getFieldX();
    double yStat = Model.getDimension().getFieldY();
    double widthStat = this.SCOREBOARDWIDTH;
    double heightStat = this.SCOREBOARDHEIGHT;

    Rectangle2D statRect = new Rectangle2D.Double(xStat, yStat, widthStat, heightStat);

    // menu screen
    Rectangle2D drawScreenRect = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());

    drawMenuScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    drawPlayerSelectionScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    if (!(Model.getGameState().equals(GameState.GAME_MENU) || Model.getGameState().equals(GameState.SELECT_SCREEN))) {
      // draw colored background
      drawBackground(Canvas, drawScreenRect, setColor);
      // draw bullet field
      drawField(Canvas, fieldRect, backgroundRect, this.scrollY, Model, setColor);
      // draw player on field
      drawPlayer(Canvas, Model.getPlayer(), this.rotateHitbox, Model.getIFrames(), setColor, true);
      // draw enemies on field
      drawEnemy(Canvas, Model.getEnemiesOnField(), setColor, false);
      // draw bosses on field
      if (Model.getBossEnemyOnField() != null) {
        drawBoss(Canvas, Model.getBossEnemyOnField(), Model.getBossAttackType(), setColor, false);
        // draw boss health
        drawBossHealthbar(Canvas, Model.getBossEnemyOnField(), fieldRect, setColor);
      }
      // draw bullets on field
      drawBulletsOnField(Canvas, Model.getBulletsOnField(), setColor, false);
      // draw collectibles on field
      drawCollectiblesOnField(Canvas, Model.getCollectiblesOnField(), setColor, true);
      // draw field frame
      if (!(Model.getGameState().equals(GameState.PAUSE_GAME) || Model.getGameState().equals(GameState.GAME_OVER))) {
        // stop scrolling when game is pause or over.
        this.scrollY += 0.8; // scroll speed
        this.rotateHitbox += (0.01)*Math.PI;
        if (this.scrollY > fieldRect.getHeight() + 2*fieldRect.getY()) {
          this.scrollY = 0;
        }
        if (this.rotateHitbox >= 2*Math.PI) {
          this.rotateHitbox = 0;
        }
      }
      drawFieldFrame(Canvas, fieldRect, setColor);
      // draw statistics like score or boss healthbar...
      drawStatistics(Canvas, statRect, setColor, Model);
      // draw pause screen
      drawPauseScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
      // draw game over screen when player has no lifes
      drawEndGameScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
      // draw game won screen
      drawGameWonScreen(Canvas, drawScreenRect, setColor, Model.getGameState());
    }
  }
  
  private void drawPlayer(Graphics2D Canvas, Player player, double angleInc, boolean iFramesActive, ColorTheme Color, boolean hasHitbox) {
    double x;
    double imgWidth;
    double y;
    double imgHeight;
    double diameter;
    double scaleFactor;
    BufferedImage playerImg;
    BufferedImage playerBox;
    Ellipse2D outer;
    Ellipse2D inner;
    Area hCircle;

    // get image for player
    String action = "still";
    if (player.getVelocity().x() > 0) {
      action = "right";
    }
    if (player.getVelocity().x() < 0) {
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
    scaleFactor = 2.5*(diameter/imgHeight);
    if (imgWidth < imgHeight) {
      scaleFactor = 2.5*(diameter/imgWidth);
    }

    // draw immunity ring
    if (iFramesActive) {
      outer = new Ellipse2D.Double(
        x - 3*player.getRadius(), 
        y - 3*player.getRadius(), 
        3*diameter, 
        3*diameter);
      inner = new Ellipse2D.Double(
        x - 3*player.getRadius() + 2, 
        y - 3*player.getRadius() + 2, 
        3*diameter - 4, 
        3*diameter - 4);
      hCircle = new Area(outer);
      hCircle.subtract(new Area(inner));
      Canvas.fill((Shape) hCircle);
      Canvas.setColor(Color.getSpriteColor('r'));
      Canvas.draw((Shape) hCircle);
    }
    // draw player
    Inf101Graphics.drawCenteredImage(Canvas, playerImg, x, y, scaleFactor);
    
    // draw hitbox
    if (hasHitbox) {
      playerBox = getCharacterImage(player.getVariation(), "shift");
      Inf101Graphics.drawCenteredImage(Canvas, playerBox, x, y, 1.5*scaleFactor, angleInc);
      /* x -= player.getRadius();
      y -= player.getRadius();
      playerBall = new Ellipse2D.Double(x, y, diameter, diameter);
      Canvas.setColor(Color.getSpriteColor('b'));
      Canvas.fill(playerBall); */
    }
    
  }

  private void drawEnemy(Graphics2D Canvas, Iterable<Enemies> enemies, ColorTheme Color, boolean hasHitbox) {
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

  private void drawBoss(Graphics2D Canvas, Enemies boss, boolean activateSuper, ColorTheme Color, boolean hasHitbox) {
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

  private void drawBossHealthbar(Graphics2D Canvas, Enemies boss, Rectangle2D fieldRect, ColorTheme color) {
    double x;
    double width;
    double y;
    double height;
    double curHealth;
    double maxHealth;
    int healthBars;

    maxHealth = boss.getMaxhealth();
    curHealth = boss.getHealthPoints();
    healthBars = boss.getHealthBars();
    x = 2*fieldRect.getX();
    y = 2*fieldRect.getY();
    
    // maxHealth
    width = (0.9)*fieldRect.getWidth();
    height = 0.5*fieldRect.getY();

    Rectangle2D maxHPLength = new Rectangle2D.Double(x, y, width, height);
    Canvas.setColor(color.getSpriteColor('r'));
    Canvas.fill(maxHPLength);

    // healthpoints decrease length
    width = (0.9)*fieldRect.getWidth()*(curHealth/maxHealth);

    Rectangle2D HPLength = new Rectangle2D.Double(x, y, width, height);
    Canvas.setColor(color.getSpriteColor('g'));
    Canvas.fill(HPLength);

    // number of healthbars
    x = 1.3*fieldRect.getX();
    y = 2.5*fieldRect.getY();

    Canvas.setColor(color.getStatisticsColor("stage"));
    Canvas.setFont(new Font("Arial", Font.BOLD, 25));
    Canvas.drawString(String.format("%s", healthBars), Math.round(x), Math.round(y));

  }

  private void drawBulletsOnField(Graphics2D Canvas, Iterable<Bullets> bulletList, ColorTheme Color, boolean hasHitbox) {
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

  private void drawCollectiblesOnField(Graphics2D Canvas, Iterable<Consumables> itemList, ColorTheme color, boolean hasHitbox) {
    double x;
    double imgWidth;
    double y;
    double diameter;
    double scaleFactor;
    Ellipse2D itemHitbox;
    BufferedImage itemImg;
    
    for (Consumables item : itemList) {
      // get image for corrosponding bullet variation.
      // also depends on bullet owners variation to determine color.
      itemImg = getCollectibleImage(SpriteType.Extend, SpriteVariations.extend);
      if (item.getType() != null && item.getVariation() != null) {
        itemImg = getCollectibleImage(item.getType(), item.getVariation());
      }

      // initialize variables
      imgWidth = itemImg.getWidth();
      x = item.getPosition().x();
      y = item.getPosition().y();
      diameter = 2*item.getRadius();
      /* System.out.println("x: " + x);
      System.out.println("y: " + y);
      System.out.println("size: " + diameter); */
      // scale by smallest image length.
      scaleFactor = 5*(diameter/imgWidth);
      if (imgWidth < diameter) {
        scaleFactor = 5*(imgWidth/diameter);
      }
      
      // draw bullet
      Inf101Graphics.drawCenteredImage(Canvas, itemImg, x, y, scaleFactor);

      // draw hitbox
      if (hasHitbox) {
        x -= item.getRadius();
        y -= item.getRadius();
        itemHitbox = new Ellipse2D.Double(x, y, diameter, diameter);
        Canvas.setColor(color.getSpriteColor('b'));
        Canvas.fill(itemHitbox);
      }

    }

  }
  
  private void drawField(Graphics2D Canvas, Rectangle2D fieldRect, Rectangle2D backgroundRect, double scrollY, ViewableDanmakuModel model, ColorTheme Color) {
    double x = (double) fieldRect.getX();
    double y = (double) fieldRect.getY();
    double centerX = (double) fieldRect.getCenterX();
    double backImgX;
    double foreImgX = 0;
    double bgHeight = backgroundRect.getHeight();
    double bgWidth = backgroundRect.getWidth();
    double imgBackHeight;
    double imgForeWidth = 1;
    double width = fieldRect.getWidth();
    double height = fieldRect.getHeight();
    double scaleFactor = 1;
    double foreScaleFactor = 1;
    BufferedImage fieldBackImg;
    BufferedImage fieldForeImg = null;

    Canvas.setColor(Color.getFieldColor());
    Canvas.fill(fieldRect);
    // only draw available stages.
    if (model.getCurrentStage() < 7) {
      // update background
      fieldBackImg = getFieldImage(model.getCurrentStage(), "stage");
      if (model.getBossEnemyOnField() != null) {
        fieldBackImg = getFieldImage(model.getCurrentStage(), "back");
        fieldForeImg = getFieldImage(model.getCurrentStage(), "fore");

        imgForeWidth = fieldForeImg.getWidth();
      }
      imgBackHeight = fieldBackImg.getHeight();

      // scale background to screen size
      scaleFactor = (bgHeight / imgBackHeight);
      if (bgHeight < imgBackHeight) {
        scaleFactor = (imgBackHeight / bgHeight);
      }
      // scale foreground to screen
      foreScaleFactor = (bgWidth / imgForeWidth);
      if (bgWidth < imgForeWidth) {
        foreScaleFactor = (imgForeWidth / bgWidth);
      }

      backImgX = centerX - (fieldBackImg.getWidth()*scaleFactor/2);
      if (model.getBossEnemyOnField() != null) {
        foreImgX = centerX - (fieldForeImg.getWidth()*foreScaleFactor/2);
      }
      // scrolling variable
      double scrollDown = scrollY - (imgBackHeight*scaleFactor) + 3;
      double scrollDown2 = scrollY;
      // draw stage background
      Inf101Graphics.drawImage(Canvas, fieldBackImg, backImgX, scrollDown, scaleFactor);
      Inf101Graphics.drawImage(Canvas, fieldBackImg, backImgX, scrollDown2, scaleFactor);
      // if boss fight, stop scrolling
      if (model.getBossEnemyOnField() != null) {
        scrollDown = y;
        scrollDown2 = y;
        // draw boss background
        Inf101Graphics.drawImage(Canvas, fieldBackImg, backImgX, scrollDown, scaleFactor);
        Inf101Graphics.drawImage(Canvas, fieldForeImg, foreImgX, scrollDown2, foreScaleFactor);
      }
    }
    // draw line of collection
    Canvas.setColor(Color.getSpriteColor('r'));
    Line2D collectionLine = new Line2D.Double(x, y + (height*(0.28)), x + width, y + (height*(0.28)));
    Canvas.draw(collectionLine);
  
  }

  private void drawBackground(Graphics2D Canvas, Rectangle2D backroundRect, ColorTheme color) {

    Canvas.setColor(color.getFrameColor());
    Canvas.fill(backroundRect);
  }

  private void drawFieldFrame(Graphics2D Canvas, Rectangle2D fieldRect, ColorTheme color) {
    // left side
    double leftX = 0;
    double leftY = 0;
    double leftWidth = fieldRect.getX() + 1;
    double leftHeight = 2*fieldRect.getY() + fieldRect.getHeight();
    // right side
    double rightX = fieldRect.getX() - 1 + fieldRect.getWidth();
    double rightY = 0;
    double rightWidth = 14*leftWidth;
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
    Canvas.setColor(color.getFrameColor());
    Canvas.fill(leftSide);
    Canvas.fill(rightSide);
    Canvas.fill(top);
    Canvas.fill(bottom);
    
  }

  private void drawStatistics(Graphics2D Canvas, Rectangle2D statRect, ColorTheme color, ViewableDanmakuModel model) {
    // fps counter
    double x = statRect.getX();
    double y = statRect.getY();
    double screenWidth = statRect.getWidth() + 2*x;
    double screenHeight = statRect.getHeight();
    
    Canvas.setColor(color.getStatisticsColor("fps"));
    Canvas.setFont(new Font("Arial", Font.BOLD, 18));
    Inf101Graphics.drawCenteredString(Canvas, "fps: " + model.getFPSValue(), 2*screenWidth, screenHeight - 2*y, 50, 50);
    
    // current stage
    x = model.getDimension().getFieldX() + model.getDimension().width();
    y = model.getDimension().getFieldY();
    double Width = statRect.getWidth();
    double HeightStageBoard = statRect.getHeight()*0.2;

    Canvas.setColor(color.getStatisticsColor("stage"));
    Canvas.setFont(new Font("Arial", Font.BOLD, 45));
    Inf101Graphics.drawCenteredString(Canvas, "Stage: " + model.getCurrentStage(), x, y, Width, HeightStageBoard);

    // current score
    x = 2*model.getDimension().getFieldX() + model.getDimension().width();
    y = 6*model.getDimension().getFieldY();

    Canvas.setColor(color.getStatisticsColor("curscore"));
    Canvas.setFont(new Font("Arial", Font.BOLD, 35));
    Canvas.drawString("Score  " + ScoreToLeadingZeroString("00000000", model.getCurrentScore()), Math.round(x), Math.round(y));

    // player lives
    y = 8*model.getDimension().getFieldY();

    Canvas.setColor(color.getStatisticsColor("lives"));
    Canvas.setFont(new Font("Arial Unicode MS", Font.BOLD, 35));
    String lifes = new String(new char[model.getPlayer().getLives()]).replace("\0", "\u22C6 "); // star for lives.
    Canvas.drawString("Player  " + lifes, Math.round(x), Math.round(y));

    // player power
    y = 10*model.getDimension().getFieldY();

    Canvas.setColor(color.getStatisticsColor("lives"));
    Canvas.setFont(new Font("Arial", Font.BOLD, 35));
    Canvas.drawString(String.format("Power  %.2f", model.getPlayer().getPower()), Math.round(x), Math.round(y));

    
  }

  // source: thanks to user4910279 on stackoverflow.
  // link: https://stackoverflow.com/questions/68978147/java-how-to-increment-a-string-of-an-integer-with-leading-zeros.
  private String ScoreToLeadingZeroString(String input, int score) {
    return NUMBER.matcher(input).replaceFirst(
      s -> String.format("%0" + s.group().length() + "d", 
      Integer.parseInt(s.group()) + score));
  }

  private void drawMenuScreen(Graphics2D Canvas, Rectangle2D MenuBackground, ColorTheme color, GameState gameStatus) {
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

  private void drawPauseScreen(Graphics2D Canvas, Rectangle2D MenuBackground, ColorTheme color, GameState gameStatus) {
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

  private void drawEndGameScreen(Graphics2D Canvas, Rectangle2D endGameBackground, ColorTheme color, GameState gameStatus) {
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
  
  private void drawGameWonScreen(Graphics2D Canvas, Rectangle2D endGameBackground, ColorTheme color, GameState status) {
    Rectangle2D pressMenu = new Rectangle2D.Double(0, 0.7*endGameBackground.getHeight(), endGameBackground.getWidth(), 30);
    if (status.equals(GameState.GAME_WON)) {

      Canvas.setColor(color.getGameOverColor("back"));
      Canvas.fill(endGameBackground);
      Canvas.setColor(color.getGameOverColor("gameover"));
      Canvas.setFont(new Font("Times New Roman", Font.PLAIN, 69));

      Inf101Graphics.drawCenteredString(Canvas, "GAME WON", endGameBackground);

      Canvas.setColor(color.getGameOverColor("key"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 20));
      Inf101Graphics.drawCenteredString(Canvas, "Go back to menu? (Enter)", pressMenu);
    }


  }

  private void drawPlayerSelectionScreen(Graphics2D Canvas, Rectangle2D MenuBackground, ColorTheme color, GameState gameStatus) {
    Rectangle2D player1 = new Rectangle2D.Double(0, 0.1*MenuBackground.getHeight(), 0.5*MenuBackground.getWidth(), 80);
    Rectangle2D pressKey1 = new Rectangle2D.Double(0, 0.9*MenuBackground.getHeight(), 0.5*MenuBackground.getWidth(), 60);
    Rectangle2D player2 = new Rectangle2D.Double(0.5*MenuBackground.getWidth(), 0.1*MenuBackground.getHeight(), 0.5*MenuBackground.getWidth(), 80);
    Rectangle2D pressKey2 = new Rectangle2D.Double(0.5*MenuBackground.getWidth(), 0.9*MenuBackground.getHeight(), 0.5*MenuBackground.getWidth(), 60);
    double scaleFactor;
    BufferedImage marisa = getCharacterImage(SpriteVariations.player1, "select");
    BufferedImage reimu = getCharacterImage(SpriteVariations.player2, "select");
    if (gameStatus.equals(GameState.SELECT_SCREEN)) {
      Canvas.setColor(color.getBackgroundColor());
      Canvas.fill(MenuBackground);
      // draw name
      Canvas.setColor(color.getMenuScreenColor("title"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 80));
      Inf101Graphics.drawCenteredString(Canvas, "player1", player1);
      // draw image
      scaleFactor = (marisa.getWidth() / ((0.5)*MenuBackground.getWidth()));
      if (marisa.getWidth() > (0.5)*MenuBackground.getWidth()) {
        scaleFactor = (((0.5)*MenuBackground.getWidth()) / marisa.getWidth());
      }
      Inf101Graphics.drawCenteredImage(Canvas, marisa, 0.25*MenuBackground.getWidth(), 0.55*MenuBackground.getHeight(), scaleFactor);
      // draw button message
      Canvas.setColor(color.getMenuScreenColor("key"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 30));
      Inf101Graphics.drawCenteredString(Canvas, "Press 1 for Stronger bullets", pressKey1);

      Canvas.setColor(color.getMenuScreenColor("title"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 80));
      Inf101Graphics.drawCenteredString(Canvas, "player2", player2);

      scaleFactor = (reimu.getWidth() / ((0.5)*MenuBackground.getWidth()));
      if (reimu.getWidth() > (0.5)*MenuBackground.getWidth()) {
        scaleFactor = (((0.5)*MenuBackground.getWidth()) / reimu.getWidth());
      }
      Inf101Graphics.drawCenteredImage(Canvas, reimu, 0.75*MenuBackground.getWidth(), 0.575*MenuBackground.getHeight(), scaleFactor);

      Canvas.setColor(color.getMenuScreenColor("key"));
      Canvas.setFont(new Font("Arial", Font.BOLD, 30));
      Inf101Graphics.drawCenteredString(Canvas, "Press 2 for Homing bullets", pressKey2);
    }
  }
  
}