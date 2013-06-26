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

import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

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

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Maps;

@ThreadSafe
public class HibernateUtil {

  public static final String DEFAULT_SESS_FAC_KEY = "hibernate";

  private static final Logger logger =
      LoggerFactory.getLogger(HibernateUtil.class);

  private static class SessionFactoryRemovalListener
      implements RemovalListener<String, SessionFactory> {

    @Override
    public void onRemoval(
        RemovalNotification<String, SessionFactory> notification) {
      notification.getValue().close();
    }
  }

  private static LoadingCache<String, SessionFactory> sessFacs = CacheBuilder
      .newBuilder()
      .removalListener(new SessionFactoryRemovalListener())
      .build(
          new CacheLoader<String, SessionFactory>() {
            @Override
            public SessionFactory load(String sessFacKey) {
              Configuration config = new Configuration();
              config.setNamingStrategy(new ImprovedNamingStrategy());
              if (sessFacKey.equals(DEFAULT_SESS_FAC_KEY)) {
                config.configure();
              } else {
                config.configure(sessFacKey + "-hibernate.cfg.xml");
              }
              Optional<Properties> props =
                  Optional.fromNullable(configProps.get(sessFacKey));
              if (props.isPresent()) {
                config.setProperties(props.get());
              }
              ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                  .applySettings(config.getProperties())
                  .buildServiceRegistry();
              SessionFactory sessionFactory = config
                  .buildSessionFactory(serviceRegistry);
              return sessionFactory;
            }
          });

  private static ConcurrentMap<String, Properties> configProps =
      Maps.newConcurrentMap();

  public static ConcurrentMap<String, Properties> getConfigProps() {
    return configProps;
  }

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
    return getSessionFactory(DEFAULT_SESS_FAC_KEY);
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
    sessFacs.invalidateAll();
  }

  private HibernateUtil() {
    throw new AssertionError();
  }

}
