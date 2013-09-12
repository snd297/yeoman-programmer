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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

      if (Hibernate.getClass(squareShape).equals(Square.class)) {
        Square square = (Square) session.load(Square.class, squareId);
        assertTrue(square instanceof Square);
      } else if (Hibernate.getClass(squareShape).equals(Circle.class)) {
        Circle circle = (Circle) session.load(Circle.class, circleId);
        assertTrue(circle instanceof Circle);
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
  public void isASquare() {
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
  public void isNotASquare() {
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
