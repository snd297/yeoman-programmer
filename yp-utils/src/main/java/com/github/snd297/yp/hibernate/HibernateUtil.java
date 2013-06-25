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
package com.github.snd297.yp.hibernate;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@ThreadSafe
public class HibernateUtil {

  private static final Logger logger =
      LoggerFactory.getLogger(HibernateUtil.class);

  private static class SessionFactorySupplier implements
      Supplier<SessionFactory> {

    @Override
    public SessionFactory get() {
      Configuration config = new Configuration();
      config.setNamingStrategy(new ImprovedNamingStrategy());
      config.configure();
      ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
          .applySettings(config.getProperties())
          .buildServiceRegistry();
      SessionFactory sessionFactory = config
          .buildSessionFactory(serviceRegistry);
      return sessionFactory;
    }
  }

  private static LoadingCache<String, SessionFactory> sessFacs = CacheBuilder
      .newBuilder()
      .build(CacheLoader.from(new SessionFactorySupplier()));

  public static void closeQuietly(@Nullable Session sess) {
    try {
      if (sess != null && sess.isOpen()) {
        sess.close();
      }
    } catch (Exception e) {
      logger.error("caught exception closing session", e);
    }
  }

  public static SessionFactory getSessionFactory(String sessFacKey) {
    return sessFacs.getUnchecked(sessFacKey);
  }

  public static SessionFactory getSessionFactory() {
    return getSessionFactory("hibernate");
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

  private HibernateUtil() {
    throw new AssertionError();
  }

}
