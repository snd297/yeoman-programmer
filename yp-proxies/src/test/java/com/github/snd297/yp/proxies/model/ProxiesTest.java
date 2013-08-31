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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.yp.utils.hibernate.HibernateUtil;

public class ProxiesTest {

  private static Long squareId;
  private static Long rectangleId;

  @BeforeClass
  public static void classSetup() throws Exception {
    Session session = null;
    Transaction trx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      trx = session.beginTransaction();

      Rectangle rectangle = new Rectangle(2, 4);
      Square square = new Square(5);

      session.save(rectangle);
      session.save(square);
      rectangleId = rectangle.getId();
      squareId = square.getId();
      trx.commit();
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

  @Test
  public void test() throws Exception {
    Session session = null;
    Transaction trx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      trx = session.beginTransaction();

      
      
      trx.commit();
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

}
