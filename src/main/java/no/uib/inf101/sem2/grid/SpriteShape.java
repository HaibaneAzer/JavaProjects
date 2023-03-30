package no.uib.inf101.sem2.grid;

public class SpriteShape<E> implements ISprite<E> {

    private int x;
    private int y;
    private int Width;
    private int Height;
    private int radius;
    private E Type;
    private Sprite<E> sp;

    /**
     * rectangular shape
     * 
     */
    public SpriteShape(int x, int y, int Width, int Height, E Type) {
        this.x = x;
        this.y = y;
        this.Width = Width;
        this.Height = Height;
        this.Type = Type;
        this.sp = new Sprite<E>(new GridPosition(x, y), this.Width, this.Height, this.Type);

    }
    /**
     * circular shape
     * 
     */
    public SpriteShape(int x, int y, int radius, E Type) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.Width = this.Height = 2*radius;
        this.Type = Type;
        this.sp = new Sprite<E>(new GridPosition(x, y), this.Width, this.Height, this.Type);

    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.Width;
    }

    @Override
    public int getHeight() {
        return this.Height;
    }

    @Override
    public E getType() {
        return this.Type;
    }

}
