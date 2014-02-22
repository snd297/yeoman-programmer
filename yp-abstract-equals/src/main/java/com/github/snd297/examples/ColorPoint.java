/*
 * Lifted from Joshua Bloch's Effective Java.
 */
package com.github.snd297.examples;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ColorPoint extends AbstractPoint {

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

  public Color getColor() {
    return color;
  }
}
