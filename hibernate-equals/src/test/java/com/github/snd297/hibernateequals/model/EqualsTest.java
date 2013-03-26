package com.github.snd297.hibernateequals.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.hibernateequals.persistence.HibernateUtil;
import com.github.snd297.hibernateequals.persistence.PersistenceUtil;
import com.google.common.base.Optional;

public class EqualsTest {

	private static Long brokenEqualsCar0;
	private static Long brokenEqualsCar1;

	@BeforeClass
	public static void classSetup() throws Exception {
		Optional<Session> sessOpt = Optional.absent();
		Optional<Transaction> trxOpt = Optional.absent();
		try {
			sessOpt = Optional.of(HibernateUtil.getSessionFactory()
					.openSession());
			Session sess = sessOpt.get();

			trxOpt = Optional.of(sess.beginTransaction());

			BrokenEqualsCar brokenEqualsCar0 =
					new BrokenEqualsCar("J398D8305HKDHG");
			BrokenEqualsCar brokenEqualsCar1 =
					new BrokenEqualsCar("J398D8305HKDHG");

			sess.save(brokenEqualsCar0);
			sess.save(brokenEqualsCar1);

			Car car = new Car("KH08934U508YTUSZ0IDYGOAIH");
			sess.save(car);

			trxOpt.get().commit();
		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trxOpt);
		} finally {
			PersistenceUtil.closeQuietly(sessOpt);
		}
	}

	@Test
	public void brokenEquals() {

	}
}
