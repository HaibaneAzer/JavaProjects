package no.uib.inf101.sem2.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestGrid {
  
  @Test
  public void gridTestGetCoordinates() {
    IGrid grid = new Grid(3, 4, 9, 16);
    assertEquals(3, grid.getFieldX());
    assertEquals(4, grid.getFieldY());
    assertEquals(9, grid.width());
    assertEquals(16, grid.height());

  }

}
