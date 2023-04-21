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
  assertEquals(Color.BLACK, colors.getFieldColor());
  assertEquals(Color.GREEN, colors.getSpriteColor('g'));
  assertEquals(Color.RED, colors.getSpriteColor('r'));
  assertThrows(IllegalArgumentException.class, () -> colors.getSpriteColor('\n'));
  assertEquals(Color.GREEN.brighter(), colors.getFieldBackgroundColor());
  assertEquals(Color.BLACK, colors.getFrameColor());

}

}
