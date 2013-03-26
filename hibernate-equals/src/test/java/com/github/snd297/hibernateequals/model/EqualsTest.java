package com.github.snd297.hibernateequals.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.hibernateequals.persistence.HibernateUtil;
import com.github.snd297.hibernateequals.persistence.PersistenceUtil;
import com.google.common.base.Optional;

public class EqualsTest {

	private static String brokenEqualsCarVin;
	private static String carVin;

	@BeforeClass
	public static void classSetup() throws Exception {
		Optional<Session> sessOpt = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sessOpt = Optional.of(HibernateUtil.getSessionFactory()
					.openSession());
			Session sess = sessOpt.get();

			trx = Optional.of(sess.beginTransaction());

			BrokenEqualsCar brokenEqualsCar =
					new BrokenEqualsCar("J398D8305HKDHG");

			sess.save(brokenEqualsCar);

			Car car = new Car("KH08934U508YTUSZ0IDYGOAIH");
			sess.save(car);

			trx.get().commit();

			brokenEqualsCarVin = brokenEqualsCar.getVin();
			carVin = car.getVin();

		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sessOpt);
		}
	}

	@Test
	public void brokenEquals() throws Exception {
		Optional<Session> sessOpt = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sessOpt = Optional.of(HibernateUtil.getSessionFactory()
					.openSession());
			Session sess = sessOpt.get();
			trx = Optional.of(sess.beginTransaction());

			BrokenEqualsCar brokenEqualsCar0 =
					(BrokenEqualsCar)
					sess
							.bySimpleNaturalId(BrokenEqualsCar.class)
							.getReference(brokenEqualsCarVin);

			BrokenEqualsCar brokenEqualsCar1 =
					new BrokenEqualsCar(brokenEqualsCarVin);

			assertFalse(brokenEqualsCar1.equals(brokenEqualsCar0));

			trx.get().commit();
		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sessOpt);
		}
	}

	@Test
	public void fixedEquals() throws Exception {
		Optional<Session> sessOpt = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sessOpt = Optional.of(
					HibernateUtil.getSessionFactory().openSession());
			Session sess = sessOpt.get();
			trx = Optional.of(sess.beginTransaction());

			Car car0 =
					(Car)
					sess
							.bySimpleNaturalId(Car.class)
							.getReference(carVin);

			Car car1 = new Car(carVin);

			assertTrue(car1.equals(car0));

			trx.get().commit();
		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sessOpt);
		}
	}
}
