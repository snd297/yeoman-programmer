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
package com.github.snd297.hibernatecollections;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.collection.spi.PersistentCollection;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.hibernatecollections.model.BadBicycle;
import com.github.snd297.hibernatecollections.model.Bicycle;
import com.github.snd297.hibernatecollections.model.IHasLongId;
import com.github.snd297.hibernatecollections.model.Wheel;
import com.github.snd297.hibernatecollections.model.WheelInBadBicycle;
import com.github.snd297.hibernatecollections.persistence.HibernateUtil;

public class PublicCollectionTest {

	private static Long badBicycleId;
	private static Long bicycleId;

	@BeforeClass
	public static void classSetup() throws Exception {
		Session sess = null;
		Transaction trx = null;
		try {
			sess = HibernateUtil.getSessionFactory().openSession();

			trx = sess.beginTransaction();

			BadBicycle badBicycle = new BadBicycle();
			new WheelInBadBicycle(badBicycle);
			new WheelInBadBicycle(badBicycle);

			sess.save(badBicycle);

			Bicycle bicycle = new Bicycle();
			new Wheel(bicycle);
			new Wheel(bicycle);

			trx.commit();

			badBicycleId = badBicycle.getId();
			bicycleId = bicycle.getId();

		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}

	@Test(expected = HibernateException.class)
	public void test() throws Exception {
		Session sess = null;
		Transaction trx = null;
		Long newWheel0Id, newWheel1Id;
		try {
			sess = HibernateUtil.getSessionFactory().openSession();

			trx = sess.beginTransaction();

			BadBicycle bicycle =
					(BadBicycle) sess.get(
							BadBicycle.class,
							badBicycleId);

			Set<WheelInBadBicycle> wheels = bicycle.getWheels();
			assertEquals(2, wheels.size());

			WheelInBadBicycle newWheel0 = new WheelInBadBicycle(bicycle);
			WheelInBadBicycle newWheel1 = new WheelInBadBicycle(bicycle);

			sess.save(newWheel0);
			sess.save(newWheel1);
			assertTrue(wheels instanceof PersistentCollection);

			Set<WheelInBadBicycle> newWheels =
					newHashSet(newWheel0, newWheel1);

			bicycle.setWheels(newWheels);
			try {
				trx.commit();
			} catch (HibernateException he) {
				assertTrue(he
						.getMessage()
						.equals(
								"A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.github.snd297.hibernatecollections.model.BadBicycle.wheels"));
				throw he;
			}
			newWheel0Id = newWheel0.getId();
			newWheel1Id = newWheel1.getId();
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}

		try {
			sess = HibernateUtil.getSessionFactory().openSession();

			trx = sess.beginTransaction();

			BadBicycle bicycle =
					(BadBicycle) sess.get(BadBicycle.class, badBicycleId);
			Set<WheelInBadBicycle> wheels = bicycle.getWheels();
			assertEquals(2, wheels.size());
			assertNotNull(find(wheels,
					compose(equalTo(newWheel0Id),
							IHasLongId.getId), null));
			assertNotNull(find(wheels,
					compose(equalTo(newWheel1Id),
							IHasLongId.getId), null));
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}
}
