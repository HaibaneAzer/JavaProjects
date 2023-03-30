package no.uib.inf101.sem2.grid;

public interface ISprite<E> {

    /**
     * getter for x-coordinate
     * 
     */
    int getX();

    /**
     * getter for y-coordinate
     * 
     */
    int getY();


    /**
     * getter for height of sprite
     * 
     */
    int getHeight();

    /**
     * getter for width of sprite
     * 
     */
    int getWidth();

    /**
     * getter for typo of sprite
     * 
     */
    E getType();

}
