/*
 * Copyright 2013 Sam Donnelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.snd297.yp.abstractequals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.snd297.yp.abstractequals.ColorPoint.Color;

/**
 * @author Sam Donnelly
 */
public class AbstractEqualsTest {

  @Test
  public void testEquals() {
    ColorPoint cp = new ColorPoint(2, 3, Color.RED);
    PointNoEquals pne = new PointNoEquals(2, 3);

    // breaks symmetry
    assertTrue(pne.equals(cp));
    assertFalse(cp.equals(pne));

    Point p1 = new Point(2, 3);
    Point p2 = new Point(2, 3);
    Point p3 = new Point(2, 3);

    Point p4 = new Point(0, 1);

    // symmetry fixed
    assertFalse(cp.equals(p1));
    assertFalse(p1.equals(cp));

    // reflexive
    assertTrue(p1.equals(p1));

    // symmetric
    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));
    assertFalse(p1.equals(p4));
    assertFalse(p4.equals(p1));

    // transitive
    assertTrue(p2.equals(p3));
    assertTrue(p1.equals(p3));

    // equals(null) is false
    assertFalse(p1.equals(null));

  }
}
