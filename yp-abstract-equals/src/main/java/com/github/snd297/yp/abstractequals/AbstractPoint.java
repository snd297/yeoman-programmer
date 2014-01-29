/*
 * Lifted from Joshua Bloch's Effective Java.
 */
package com.github.snd297.yp.abstractequals;

public abstract class AbstractPoint {

  private final int x;
  private final int y;

  public AbstractPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public final int getX() {
    return x;
  }

  public final int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  /**
   * If two {@code AbstractPoint}s are equal, they have the same x and y
   * coordinates.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof AbstractPoint)) {
      return false;
    }
    AbstractPoint other = (AbstractPoint) obj;
    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return true;
  }
}
