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

import org.hibernate.SessionFactory;
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

}
