package com.github.snd297.hibernatecollections;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;

import com.github.snd297.hibernatecollections.persistence.HibernateUtil;
import com.github.snd297.hibernatecollections.persistence.PersistenceUtil;
import com.google.common.base.Optional;

public class PublicCollectionTest {
	@BeforeClass
	public static void classSetup() throws Exception {
		Optional<Session> sess = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sess =
					Optional.of(HibernateUtil.getSessionFactory().openSession());

			trx = Optional.of(sess.get().beginTransaction());

			trx.get().commit();

		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sess);
		}
	}
}
