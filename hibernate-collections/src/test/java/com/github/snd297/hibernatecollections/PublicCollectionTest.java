package com.github.snd297.hibernatecollections;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.hibernatecollections.model.BadBicycle;
import com.github.snd297.hibernatecollections.model.WheelInBadBicycle;
import com.github.snd297.hibernatecollections.persistence.HibernateUtil;
import com.github.snd297.hibernatecollections.persistence.PersistenceUtil;
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
			new WheelInBadBicycle(badBicycle);
			new WheelInBadBicycle(badBicycle);
			sess.get().save(badBicycle);

			trx.get().commit();

		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sess);
		}
	}

	@Test
	public void test() throws Exception {
		Optional<Session> sess = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			BadBicycle badBicycle =
					(BadBicycle) sess.get().get(BadBicycle.class, badBicycleId);

			trx.get().commit();

		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sess);
		}
	}
}
