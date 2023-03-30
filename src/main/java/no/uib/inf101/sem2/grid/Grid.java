package no.uib.inf101.sem2.grid;


public class Grid implements IGrid {
    
    private final int x;
    private final int y;
    private int width;
    private int height;

    /**
     * constructor for Field
     * 
     */
    public Grid(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }


}
