/*
 * Lifted from Joshua Bloch's Effective Java.
 */
package com.github.snd297.yp.abstractequals;

import javax.annotation.Nullable;

public class ColorPoint extends AbstractPoint {

  public enum Color {
    RED,
    BLUE,
    GREEN
  }

  private Color color;

  public ColorPoint(int x, int y, Color color) {
    super(x, y);
    this.color = color;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((color == null) ? 0 : color.hashCode());
    return result;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof ColorPoint)) {
      return false;
    }
    ColorPoint other = (ColorPoint) obj;
    if (color != other.color) {
      return false;
    }
    return true;
  }

}
