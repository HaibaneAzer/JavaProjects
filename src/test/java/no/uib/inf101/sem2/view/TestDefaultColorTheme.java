package no.uib.inf101.sem2.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.awt.Color;

public class TestDefaultColorTheme {

@Test
public void sanityTestDefaultColorTheme() {
  ColorTheme colors = new DefaultColorTheme();
  assertEquals(null, colors.getBackgroundColor());
  assertEquals(Color.WHITE, colors.getFieldColor());
  assertEquals(Color.GREEN, colors.getSpriteColor('g'));
  assertEquals(Color.RED, colors.getSpriteColor('r'));
  assertThrows(IllegalArgumentException.class, () -> colors.getSpriteColor('\n'));
  assertEquals(Color.GRAY.brighter(), colors.getFieldBackgroundColor());
  assertEquals(Color.DARK_GRAY.darker(), colors.getFrameColor());
  assertEquals(Color.decode("#4F0001").brighter().brighter().brighter(), colors.getGameOverColor("gameover"));
  assertEquals(Color.WHITE, colors.getMenuScreenColor("title"));
  assertEquals(Color.WHITE.darker(), colors.getPauseScreenColor("paused"));
  assertEquals(Color.CYAN.darker(), colors.getStatisticsColor("lives"));
  assertEquals(Color.DARK_GRAY.darker(), colors.getFrameColor());

}

}
