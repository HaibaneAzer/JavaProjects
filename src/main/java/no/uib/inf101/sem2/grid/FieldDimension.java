package no.uib.inf101.sem2.grid;

public interface FieldDimension {
  
  /** Number of pixels in a row on the field */
  int width();
  
  /** Number of pixels in a column on the field */
  int height();
  
  /** x coordinate from upper-left corner of window */
  int getFieldX();
  
  /** y coordinate from upper-left corner of window */
  int getFieldY();
}
