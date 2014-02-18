/*
 * Lifted from Joshua Bloch's Effective Java.
 */
package com.github.snd297.yp.abstractequals;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public class ColorPoint extends AbstractPoint {

  public enum Color {
    RED,
    BLUE,
    GREEN
  }

  private final Color color;

  public ColorPoint(int x, int y, Color color) {
    super(x, y);
    this.color = checkNotNull(color);
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

  public Color getColor() {
    return color;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((color == null) ? 0 : color.hashCode());
    return result;
  }

}
