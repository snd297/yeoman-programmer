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
import com.google.common.base.Optional;

public class PublicCollectionTest {

	private static Optional<Long> badBicycleId;
	private static Optional<Long> bicycleId;

	@BeforeClass
	public static void classSetup() throws Exception {
		Optional<Session> sess = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			BadBicycle badBicycle = new BadBicycle();
			new WheelInBadBicycle(badBicycle);
			new WheelInBadBicycle(badBicycle);

			sess.get().save(badBicycle);

			Bicycle bicycle = new Bicycle();
			new Wheel(bicycle);
			new Wheel(bicycle);

			trx.get().commit();

			badBicycleId = Optional.of(badBicycle.getId());
			bicycleId = Optional.of(bicycle.getId());

		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}

	@Test(expected = HibernateException.class)
	public void test() throws Exception {
		assertTrue(badBicycleId.isPresent());
		assertTrue(bicycleId.isPresent());
		Optional<Session> sess = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		Optional<Long> newWheel0Id =
				Optional.absent(), newWheel1Id = Optional.absent();
		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			BadBicycle bicycle =
					(BadBicycle) sess.get().get(
							BadBicycle.class,
							badBicycleId.get());
			
			Set<WheelInBadBicycle> wheels = bicycle.getWheels();
			assertEquals(2, wheels.size());

			WheelInBadBicycle newWheel0 = new WheelInBadBicycle(bicycle);
			WheelInBadBicycle newWheel1 = new WheelInBadBicycle(bicycle);

			sess.get().save(newWheel0);
			sess.get().save(newWheel1);
			assertTrue(wheels instanceof PersistentCollection);

			Set<WheelInBadBicycle> newWheels =
					newHashSet(newWheel0, newWheel1);

			bicycle.setWheels(newWheels);
			try {
				trx.get().commit();
			} catch (HibernateException he) {
				assertTrue(he
						.getMessage()
						.equals(
								"A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.github.snd297.hibernatecollections.model.BadBicycle.wheels"));
				throw he;
			}
			newWheel0Id = Optional.of(newWheel0.getId());
			newWheel1Id = Optional.of(newWheel1.getId());
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}

		assertTrue(newWheel0Id.isPresent());
		assertTrue(newWheel1Id.isPresent());

		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			BadBicycle bicycle =
					(BadBicycle) sess.get().get(BadBicycle.class, badBicycleId);
			Set<WheelInBadBicycle> wheels = bicycle.getWheels();
			assertEquals(2, wheels.size());
			assertNotNull(find(wheels,
					compose(equalTo(newWheel0Id.get()),
							IHasLongId.getId), null));
			assertNotNull(find(wheels,
					compose(equalTo(newWheel1Id.get()),
							IHasLongId.getId), null));
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}
}
