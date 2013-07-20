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

package com.github.snd297.yp.utils.hibernate;

import java.util.Properties;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.Cache;

@ThreadSafe
public class HibernateUtil {

  private final static Supplier<SessionFactories> sessFacsSupplier =
      Suppliers.memoize(
          new Supplier<SessionFactories>() {
            @Override
            public SessionFactories get() {
              return new SessionFactories();
            }
          });

  private static final Logger logger =
      LoggerFactory.getLogger(HibernateUtil.class);

  public static void closeQuietly(@Nullable Session sess) {
    try {
      if (sess != null && sess.isOpen()) {
        sess.close();
      }
    } catch (Exception e) {
      logger.error("caught exception closing session", e);
    }
  }

  public static Configuration getConfiguration() {
    return sessFacsSupplier.get().getConfiguration();
  }

  public static Configuration getConfiguration(String key) {
    Preconditions.checkNotNull(key);
    return sessFacsSupplier.get().getConfiguration(key);
  }

  @VisibleForTesting
  static Cache<String, SessionFactory> getSessFacs() {
    return sessFacsSupplier.get().getSessFacs();
  }

  public static SessionFactory getSessionFactory() {
    return sessFacsSupplier.get().getSessionFactory();
  }

  public static SessionFactory getSessionFactory(String sessFacKey) {
    return sessFacsSupplier.get().getSessionFactory(sessFacKey);
  }

  public static void putConfigProps(Properties props) {
    sessFacsSupplier.get().putConfigProps(props);
  }

  public static void putConfigProps(String key, Properties props) {
    sessFacsSupplier.get().putConfigProps(key, props);
  }

  public static void rollbackQuietly(@Nullable Transaction trx) {
    try {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
    } catch (Exception e) {
      logger.error("caught exception rolling back transaction", e);
    }
  }

  public static void shutdown() {
    sessFacsSupplier.get().shutdown();
  }

  private HibernateUtil() {
    throw new AssertionError();
  }

}