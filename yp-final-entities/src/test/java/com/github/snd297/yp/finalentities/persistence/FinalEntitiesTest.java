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
package com.github.snd297.yp.finalentities.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.yp.finalentities.persistence.FinalBicycle;
import com.github.snd297.yp.finalentities.persistence.FinalWheel;
import com.github.snd297.yp.finalentities.persistence.Spoke;
import com.github.snd297.yp.finalentities.persistence.SpokeInFinalWheel;
import com.github.snd297.yp.finalentities.persistence.Wheel;
import com.github.snd297.yp.utils.hibernate.HibernateUtil;

public class FinalEntitiesTest {

  private static Long finalSpokeId;
  private static Long spokeId;

  @BeforeClass
  public static void classSetup() throws Exception {
    SessionFactory sessFac = HibernateUtil.getSessionFactory();
    Session sess = null;
    Transaction trx = null;
    try {
      sess = sessFac.openSession();
      trx = sess.beginTransaction();

      FinalBicycle bicycle = new FinalBicycle();
      FinalWheel finalWheel = new FinalWheel(bicycle);
      SpokeInFinalWheel finalSpoke = new SpokeInFinalWheel(finalWheel);
      sess.save(bicycle);

      Wheel wheel = new Wheel();
      Spoke spoke = new Spoke(wheel);
      sess.save(wheel);

      trx.commit();

      finalSpokeId = finalSpoke.getId();
      spokeId = spoke.getId();
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }

  @Test
  public void finalNoProxy() throws Exception {
    SessionFactory sessFac = HibernateUtil.getSessionFactory();
    Session sess = null;
    Transaction trx = null;
    try {
      sess = sessFac.openSession();
      trx = sess.beginTransaction();

      SpokeInFinalWheel spoke = (SpokeInFinalWheel)
          sess.load(SpokeInFinalWheel.class, finalSpokeId);

      FinalWheel wheel = spoke.getWheel();

      assertFalse((Object) wheel instanceof HibernateProxy);

      trx.commit();
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }

  @Test
  public void proxy() throws Exception {
    SessionFactory sessFac = HibernateUtil.getSessionFactory();
    Session sess = null;
    Transaction trx = null;
    try {
      sess = sessFac.openSession();
      trx = sess.beginTransaction();

      Spoke spoke =
          (Spoke) sess.load(Spoke.class, spokeId);

      Wheel wheel = spoke.getWheel();

      assertTrue(wheel instanceof HibernateProxy);
      assertFalse(Hibernate.isInitialized(wheel));

      trx.commit();
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }
}
