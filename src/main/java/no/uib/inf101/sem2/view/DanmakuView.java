package no.uib.inf101.sem2.view;

import java.awt.Dimension;
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
        int x = this.Model.getDimension().getX();
        int y = this.Model.getDimension().getY();
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

    }

    private void drawSprites(Graphics2D Canvas, Player player, ColorTheme Color) {
        double x;
        double y;
        double width;
        double height;
        Ellipse2D playerBall;
        Rectangle2D playerRect;
       
        x = player.getPos().x() - player.getRadius();
        y = player.getPos().y() - player.getRadius();
        width = player.getWidth();
        height = player.getHeight();

        if (player.getType().equals("P1c") || player.getType().equals("P2c")) {
            
            x = player.getPos().x() - player.getRadius();
            y = player.getPos().y() - player.getRadius();
            width = player.getWidth();
            height = player.getHeight();
            
            playerBall = new Ellipse2D.Double(x, y, width, height);

            Canvas.setColor(Color.getSpriteColor('r'));
            Canvas.fill(playerBall);
           
        }
        else {
            x = player.getPos().x();
            y = player.getPos().y();
            width = player.getWidth();
            height = player.getHeight();

            playerRect = new Rectangle2D.Double(x, y, width, height);

            Canvas.setColor(Color.getSpriteColor('r'));
            Canvas.fill(playerRect);
        }
        


    }
        

        

    private void drawField(Graphics2D Canvas, FieldDimension Field, ColorTheme Color) {
        double x = (double) Field.getX();
        double y = (double) Field.getY();
        double width = this.getWidth() - 2*x;
        double height = this.getHeight() - 2*y;
        
        Rectangle2D newRect = new Rectangle2D.Double(x, y, width, height);
        Canvas.setColor(Color.getFieldColor());
        Canvas.fill(newRect);

    }



}
