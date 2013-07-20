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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.concurrent.ThreadSafe;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.snd297.yp.utils.io.Closeables;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Maps;

@ThreadSafe
public final class SessionFactories {
  private class SessionFactoryRemovalListener
      implements RemovalListener<String, SessionFactory> {

    @Override
    public void onRemoval(
        RemovalNotification<String, SessionFactory> notification) {
      notification.getValue().close();
      configs.remove(notification.getKey());
    }
  }

  public static final String DEFAULT_SESS_FAC_KEY = "default-session-factory";

  private static final Logger logger =
      LoggerFactory.getLogger(SessionFactories.class);

  private LoadingCache<String, SessionFactory> sessFacs = CacheBuilder
      .newBuilder()
      .removalListener(new SessionFactoryRemovalListener())
      .build(
          new CacheLoader<String, SessionFactory>() {
            @Override
            public SessionFactory load(String sessFacKey) {
              checkNotNull(sessFacKey);
              String m = "load(String)";
              logger.debug(
                  "{}: building new SessionFactory for sessFacKey [{}]",
                  m, sessFacKey);
              Configuration config = new Configuration();
              config.setNamingStrategy(new ImprovedNamingStrategy());
              Optional<Properties> propsOpt =
                  Optional.fromNullable(configProps
                      .get(sessFacKey));
              if (propsOpt.isPresent()) {
                config.setProperties(propsOpt.get());
              } else {
                if (sessFacKey.equals(DEFAULT_SESS_FAC_KEY)) {

                } else {
                  Properties props = new Properties();
                  InputStream is = null;
                  try {
                    String fileName =
                        sessFacKey
                            + "-hibernate.properties";
                    is = HibernateUtil.class
                        .getClassLoader()
                        .getResourceAsStream(
                            sessFacKey
                                + "-hibernate.properties");
                    Preconditions.checkState(is != null,
                        "couldn't load hibernate properties file ["
                            + fileName + "]");
                    props.load(is);
                    config.setProperties(props);
                  } catch (IOException e) {
                    throw Throwables.propagate(e);
                  } finally {
                    Closeables.closeQuietly(is);
                  }
                }
              }
              if (sessFacKey.equals(DEFAULT_SESS_FAC_KEY)) {
                config.configure();
              } else {
                config.configure(sessFacKey
                    + "-hibernate.cfg.xml");
              }

              ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                  .applySettings(config.getProperties())
                  .buildServiceRegistry();
              SessionFactory sessionFactory = config
                  .buildSessionFactory(serviceRegistry);
              configs.put(sessFacKey, config);
              configProps.remove(sessFacKey);
              return sessionFactory;
            }
          });

  private ConcurrentMap<String, Configuration> configs =
      Maps.newConcurrentMap();

  private ConcurrentMap<String, Properties> configProps =
      Maps.newConcurrentMap();

  public Configuration getConfiguration() {
    return getConfiguration(DEFAULT_SESS_FAC_KEY);
  }

  public Configuration getConfiguration(String key) {
    Preconditions.checkNotNull(key);
    return configs.get(key);
  }

  @VisibleForTesting
  Cache<String, SessionFactory> getSessFacs() {
    return sessFacs;
  }

  public SessionFactory getSessionFactory() {
    return getSessionFactory(DEFAULT_SESS_FAC_KEY);
  }

  public SessionFactory getSessionFactory(String sessFacKey) {
    return sessFacs.getUnchecked(sessFacKey);
  }

  public void putConfigProps(Properties props) {
    configProps.put(DEFAULT_SESS_FAC_KEY, props);
  }

  public void putConfigProps(String key, Properties props) {
    configProps.put(key, props);
  }

  public void shutdown() {
    sessFacs.invalidateAll();
  }

}