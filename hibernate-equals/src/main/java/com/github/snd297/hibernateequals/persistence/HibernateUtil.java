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
package com.github.snd297.hibernateequals.persistence;

import javax.annotation.Nullable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;

@SuppressWarnings("deprecation")
public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		try {
			Configuration config = new Configuration();
			config.setNamingStrategy(new ImprovedNamingStrategy());
			config.configure();
			sessionFactory = config.buildSessionFactory();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new ExceptionInInitializerError(t);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private HibernateUtil() {
		throw new AssertionError();
	}

	public static void closeQuietly(@Nullable Session sess) {
		try {
			if (sess != null && sess.isOpen()) {
				sess.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rollbackQuietly(@Nullable Transaction trx) {
		try {
			if (trx != null && trx.isActive()) {
				trx.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
