package no.uib.inf101.sem2.grid;


/**
 * A Sprite consists of its position and a bound value representing the 
 * sprites shape and its type.
 * @param pos the Gridposition
 * @param width the width of sprite boundary
 * @param height the height of sprite boundary
 * @param Type is the type of sprite (player, bullet, etc...)
 */
public record Sprite<E>(GridPosition pos, int Width, int Height, E Type) {
    
}
