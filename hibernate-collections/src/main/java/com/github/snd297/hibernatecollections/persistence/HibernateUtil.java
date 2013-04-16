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
package com.github.snd297.hibernatecollections.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.google.common.base.Optional;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		try {
			Configuration config = new Configuration();
			config.setNamingStrategy(new ImprovedNamingStrategy());
			config.configure();
			ServiceRegistry serviceRegistry =
					new ServiceRegistryBuilder()
							.applySettings(config.getProperties())
							.buildServiceRegistry();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new ExceptionInInitializerError(t);
		}
	}

	public static void closeQuietly(Optional<Session> sess) {
		try {
			if (sess.isPresent() && sess.get().isOpen()) {
				sess.get().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void rollbackQuietly(Optional<Transaction> trx) {
		try {
			if (trx.isPresent() && trx.get().isActive()) {
				trx.get().rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HibernateUtil() {
		throw new AssertionError();
	}

}