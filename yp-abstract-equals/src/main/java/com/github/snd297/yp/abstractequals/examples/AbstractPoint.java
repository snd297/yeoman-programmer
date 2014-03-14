/*
 * Lifted from Joshua Bloch's Effective Java.
 */
package com.github.snd297.yp.abstractequals.examples;

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
}
