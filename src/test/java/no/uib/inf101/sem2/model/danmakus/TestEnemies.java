package no.uib.inf101.sem2.model.danmakus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestEnemies {

  @Test
  public void testEnemyTakingDmg() {
    
    // check if dmg subtracts normally
    int dmg = 50;
    Enemies e1 = Enemies.newEnemy(SpriteVariations.fairy);
    Enemies e2 = Enemies.newEnemy(SpriteVariations.MoFboss1);

    int oldHealth1 = e1.getMaxhealth();
    int oldHealth2 = e2.getMaxhealth();
    int oldbars = e2.getHealthBars();
    int curHealth = oldHealth1 - dmg;

    assertEquals(curHealth, e1.attackEnemy(dmg).getHealthPoints());
    
    // losing all health should kill the enemy
    e1 = e1.attackEnemy(oldHealth1);

    assertTrue(!e1.isAlive());

    // depleting all healthpoints doesn't kill if enemy has healthbars
    e2 = e2.attackEnemy(oldHealth2);

    assertTrue(e2.isAlive());

    // depleting health should only remove 1 healthbar
    int curBars = oldbars - 1;
    assertEquals(curBars, e2.getHealthBars());

  }

  @Test
  public void testFireDelayAndDirectionState() {

    Enemies e1 = Enemies.newEnemy(SpriteVariations.fairy);
    Enemies e2 = Enemies.newEnemy(SpriteVariations.highFairy);

    // check fireTimer setter (default zero)
    int newTimer = 45;

    assertNotEquals(e1.getFireTimer(), e1.setFireTimer(newTimer).getFireTimer());

    // check different enemies with different delays
    assertTrue(e1.getFireDelay() != e2.getFireDelay());

    // default direction is "aim"
    SpriteState defaultState = e2.directionState;

    assertEquals(SpriteState.aim, defaultState);

  }

}
