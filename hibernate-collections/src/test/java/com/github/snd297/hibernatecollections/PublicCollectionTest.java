package com.github.snd297.hibernatecollections;

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
import com.github.snd297.hibernatecollections.model.WheelInBadBicycle;
import com.github.snd297.hibernatecollections.persistence.HibernateUtil;
import com.google.common.base.Optional;

public class PublicCollectionTest {

	private static Long badBicycleId;

	@BeforeClass
	public static void classSetup() throws Exception {
		Optional<Session> sess = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			BadBicycle badBicycle = new BadBicycle();

			sess.get().save(badBicycle);
			sess.get().save(new WheelInBadBicycle(badBicycle));
			sess.get().save(new WheelInBadBicycle(badBicycle));

			trx.get().commit();

			badBicycleId = badBicycle.getId();

		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}

	@Test(expected = HibernateException.class)
	public void test() throws Exception {
		assertNotNull(badBicycleId);
		Optional<Session> sess = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		Optional<Long> newWheel0Id = Optional.absent(), newWheel1Id = Optional
				.absent();
		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			BadBicycle bicycle =
					(BadBicycle) sess.get().get(BadBicycle.class, badBicycleId);

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
				he.printStackTrace();
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
		} catch (Exception e) {
			HibernateUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			HibernateUtil.closeQuietly(sess);
		}
	}
}
