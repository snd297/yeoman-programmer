/*
 * Copyright 2014 Sam Donnelly
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
package org.yp.hibernate.collvers.persistence;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.yp.utils.hibernate.HibernateUtil;

public class MyTest {

  private static SessionFactory sessFac = HibernateUtil.getSessionFactory();

  private static Long parentId;

  @BeforeClass
  public static void classSetUp() {
    Session session = null;
    Transaction trx = null;
    try {
      session = sessFac.openSession();
      trx = session.beginTransaction();
      Parent parent = new Parent();
      session.save(parent);

      EmbeddableChild embChild0 = new EmbeddableChild();
      parent.getEmbeddableChildren().add(embChild0);
      EmbeddableChild embChild1 = new EmbeddableChild();
      parent.getEmbeddableChildren().add(embChild1);

      EntityChild entityChild0 = new EntityChild(parent);
      parent.getEntityChildren().add(entityChild0);
      EntityChild entityChild1 = new EntityChild(parent);
      parent.getEntityChildren().add(entityChild1);

      trx.commit();
      parentId = checkNotNull(parent.getId());
    } catch (RuntimeException rte) {
      HibernateUtil.rollbackQuietly(trx);
      throw rte;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

  @Test
  public void embeddableChildren() {
    Session session = null;
    Transaction trx = null;
    try {
      session = sessFac.openSession();
      trx = session.beginTransaction();
      Parent parent = (Parent)
          session.load(Parent.class, parentId);
      Integer parentVersion = parent.getVersion();
      assertNotNull(parentVersion);

      EmbeddableChild child = new EmbeddableChild();
      parent.getEmbeddableChildren().add(child);
      session.flush();
      assertEquals(parentVersion + 1, parent.getVersion().intValue());

      parent.getEmbeddableChildren().remove(child);
      session.flush();

      assertEquals(parentVersion + 2, parent.getVersion().intValue());

      trx.commit();
    } catch (RuntimeException rte) {
      HibernateUtil.rollbackQuietly(trx);
      throw rte;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }

  @Test
  public void entityChildren() {
    Session session = null;
    Transaction trx = null;
    try {
      session = sessFac.openSession();
      trx = session.beginTransaction();
      Parent parent = (Parent)
          session.load(Parent.class, parentId);
      Integer parentVersion = parent.getVersion();
      assertNotNull(parentVersion);

      EntityChild child = new EntityChild(parent);
      parent.getEntityChildren().add(child);
      session.flush();
      assertEquals(parentVersion + 1, parent.getVersion().intValue());

      parent.getEntityChildren().remove(child);
      session.flush();

      assertEquals(parentVersion + 2, parent.getVersion().intValue());

      trx.commit();
    } catch (RuntimeException rte) {
      HibernateUtil.rollbackQuietly(trx);
      throw rte;
    } finally {
      HibernateUtil.closeQuietly(session);
    }
  }
}
