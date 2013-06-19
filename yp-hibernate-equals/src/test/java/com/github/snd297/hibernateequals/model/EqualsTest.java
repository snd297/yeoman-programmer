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
package com.github.snd297.hibernateequals.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.hibernateequals.persistence.HibernateUtil;

public class EqualsTest {

	private static String getClassCarVin;
	private static String brokenEqualsCarVin;
	private static String carVin;

	@BeforeClass
	public static void classSetup() throws Exception {
		Session sess = null;
		Transaction trx = null;
		try {

			sess = HibernateUtil.getSessionFactory().openSession();
			trx = sess.beginTransaction();

			GetClassCar getClassCar =
					new GetClassCar("H94H878YUIOHFGOH");
			getClassCar.setSomeField("value");
			sess.save(getClassCar);

			BrokenEqualsCar brokenEqualsCar =
					new BrokenEqualsCar("J398D8305HKDHG");

			sess.save(brokenEqualsCar);

			Car car = new Car("KH08934U508YTUSZ0IDYGOAIH");
			sess.save(car);

			trx.commit();

			getClassCarVin = getClassCar.getVin();
			brokenEqualsCarVin = brokenEqualsCar.getVin();
			carVin = car.getVin();

		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}

	@Test
	public void getClassEquals() throws Exception {
		Session sess = null;
		Transaction trx = null;
		try {
			sess = HibernateUtil.getSessionFactory().openSession();
			trx = sess.beginTransaction();

			GetClassCar getClassCar0 =
					(GetClassCar)
					sess
							.bySimpleNaturalId(GetClassCar.class)
							.getReference(getClassCarVin);
			GetClassCar getClassCar1 =
					new GetClassCar(getClassCarVin);

			assertTrue(getClassCar0 instanceof HibernateProxy);
			assertFalse(getClassCar1.equals(getClassCar0));
			assertTrue(getClassCar0.equals(getClassCar1));

			trx.commit();
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}

	@Test
	public void brokenEquals() throws Exception {
		Session sess = null;
		Transaction trx = null;
		try {
			sess = HibernateUtil.getSessionFactory().openSession();
			trx = sess.beginTransaction();

			BrokenEqualsCar brokenEqualsCar0 =
					(BrokenEqualsCar)
					sess
							.bySimpleNaturalId(BrokenEqualsCar.class)
							.getReference(brokenEqualsCarVin);

			BrokenEqualsCar brokenEqualsCar1 =
					new BrokenEqualsCar(brokenEqualsCarVin);

			assertTrue(brokenEqualsCar0 instanceof HibernateProxy);
			assertFalse(brokenEqualsCar1.equals(brokenEqualsCar0));
			assertTrue(brokenEqualsCar0.equals(brokenEqualsCar1));

			trx.commit();
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}

	@Test
	public void fixedEquals() throws Exception {
		Session sess = null;
		Transaction trx = null;
		try {
			sess = HibernateUtil.getSessionFactory().openSession();
			trx = sess.beginTransaction();

			Car car0 =
					(Car)
					sess
							.bySimpleNaturalId(Car.class)
							.getReference(carVin);

			Car car1 = new Car(carVin);

			assertTrue(car0 instanceof HibernateProxy);
			assertTrue(car1.equals(car0));
			assertTrue(car0.equals(car1));

			trx.commit();
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}
}
