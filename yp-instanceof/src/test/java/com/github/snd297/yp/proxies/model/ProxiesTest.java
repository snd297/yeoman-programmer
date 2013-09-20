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
import static org.junit.Assert.assertNotSame;
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

public class ProxiesTest {

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
      assertFalse(squareShape instanceof Square);

      Shape circleShape =
          (Shape) session.load(Shape.class, circleId);
      assertFalse(circleShape instanceof Circle);

      List<Shape> shapes = newArrayList(squareShape, circleShape);

      for (Shape shape : shapes) {
        if (Hibernate.getClass(shape).equals(Square.class)) {
          Square square = (Square) session.load(Square.class, shape.getId());
          assertEquals(shape, square);
          assertNotSame(shape, square);
          assertTrue(square instanceof HibernateProxy);
          assertTrue(square instanceof Shape);
        } else if (Hibernate.getClass(shape).equals(Circle.class)) {
          Circle circle = (Circle) session.load(Circle.class, shape.getId());
          assertEquals(shape, circle);
          assertNotSame(shape, circle);
          assertTrue(circle instanceof HibernateProxy);
          assertTrue(circle instanceof Shape);
        }
      }
      trx.commit();
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