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
package com.github.snd297.yp.proxies.model;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.yp.utils.hibernate.HibernateUtil;

public class InstanceofTest {

  private static Long squareId;
  private static Long circleId;

  @BeforeClass
  public static void classSetup() {
    Session session = null;
    Transaction trx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      trx = session.beginTransaction();

      Circle circle = new Circle(2);
      Square square = new Square(5);

      session.save(circle);
      session.save(square);

      circleId = circle.getId();
      squareId = square.getId();

      trx.commit();
    } catch (RuntimeException e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

  @Test
  public void whatToDo() {
    Session session = null;
    Transaction trx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      trx = session.beginTransaction();

      Shape squareShape =
          (Shape) session.load(Shape.class, squareId);
      assertTrue(squareShape instanceof HibernateProxy);
      Hibernate.initialize(squareShape);

      Shape circleShape =
          (Shape) session.load(Shape.class, circleId);
      assertTrue(circleShape instanceof HibernateProxy);
      Hibernate.initialize(circleShape);

      List<Shape> shapes = newArrayList(squareShape, circleShape);
      boolean foundRectangle = false, foundCircle = false;
      for (Shape shape : shapes) {
        if (Rectangle.class.isAssignableFrom(Hibernate.getClass(shape))) {
          Rectangle squareRectangle = (Rectangle) session.load(Rectangle.class,
              shape.getId());
          assertTrue(squareRectangle instanceof HibernateProxy);
          Hibernate.initialize(squareRectangle);

          // This is something to be aware of, this is why we get the warning:
          // HHH000179: Narrowing proxy to class
          // com.github.snd297.yp.proxies.model.Square - this operation breaks
          // ==
          assertNotEquals(shape, squareRectangle);
          assertEquals(shape.getId(), squareRectangle.getId());

          foundRectangle = true;
        } else if (Circle.class.isAssignableFrom(Hibernate.getClass(shape))) {
          Circle circle = (Circle) session.load(Circle.class, shape.getId());
          assertTrue(circle instanceof HibernateProxy);
          Hibernate.initialize(circle);

          // This is something to be aware of, this is why we get the warning:
          // HHH000179: Narrowing proxy to class
          // com.github.snd297.yp.proxies.model.Circle - this operation breaks
          // ==
          assertNotEquals(shape, circle);
          assertEquals(shape.getId(), circle.getId());

          foundCircle = true;
        }
      }
      trx.commit();
      assertTrue(foundRectangle);
      assertTrue(foundCircle);
    } catch (RuntimeException e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

  @Test
  public void instanceofASquare() {
    Session session = null;
    Transaction trx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      trx = session.beginTransaction();

      Shape gotSquare =
          (Shape) session.get(Shape.class, squareId);
      assertTrue(gotSquare instanceof Square);
      assertFalse(gotSquare instanceof HibernateProxy);

      Shape loadedSquare =
          (Shape) session.load(Shape.class, squareId);
      assertSame(loadedSquare, gotSquare);

      trx.commit();
    } catch (RuntimeException e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

  @Test
  public void notInstanceofASquare() {
    Session session = null;
    Transaction trx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      trx = session.beginTransaction();

      Shape loadedSquare =
          (Shape) session.load(Shape.class, squareId);
      assertFalse(loadedSquare instanceof Square);
      assertTrue(loadedSquare instanceof HibernateProxy);

      Shape gotSquare =
          (Shape) session.get(Shape.class, squareId);
      assertSame(loadedSquare, gotSquare);

      trx.commit();
    } catch (RuntimeException e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

}
