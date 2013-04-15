package com.github.snd297.hibernateequals.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.snd297.hibernateequals.persistence.HibernateUtil;
import com.github.snd297.hibernateequals.persistence.PersistenceUtil;
import com.google.common.base.Optional;

public class EqualsTest {

	private static String getClassCarVin;
	private static String brokenEqualsCarVin;
	private static String carVin;

	@BeforeClass
	public static void classSetup() throws Exception {
		Optional<Session> sessOpt = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sessOpt =
					Optional.of(HibernateUtil.getSessionFactory().openSession());
			Session sess = sessOpt.get();

			trx = Optional.of(sess.beginTransaction());

			GetClassCar getClassCar =
					new GetClassCar("H94H878YUIOHFGOH");
			getClassCar.setSomeField("value");
			sess.save(getClassCar);

			BrokenEqualsCar brokenEqualsCar =
					new BrokenEqualsCar("J398D8305HKDHG");

			sess.save(brokenEqualsCar);

			Car car = new Car("KH08934U508YTUSZ0IDYGOAIH");
			car.setSomeField("someField");
			sess.save(car);

			trx.get().commit();

			getClassCarVin = getClassCar.getVin();
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
	public void getClassEquals() throws Exception {
		Optional<Session> sessOpt = Optional.absent();
		Optional<Transaction> trx = Optional.absent();
		try {
			sessOpt =
					Optional.of(HibernateUtil.getSessionFactory().openSession());
			Session sess = sessOpt.get();
			trx = Optional.of(sess.beginTransaction());

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

			trx.get().commit();
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
			sessOpt =
					Optional.of(HibernateUtil.getSessionFactory().openSession());
			Session sess = sessOpt.get();
			trx = Optional.of(sess.beginTransaction());

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
			sessOpt =
					Optional.of(HibernateUtil.getSessionFactory().openSession());
			Session sess = sessOpt.get();
			trx = Optional.of(sess.beginTransaction());

			Car car0 =
					(Car)
					sess
							.bySimpleNaturalId(Car.class)
							.getReference(carVin);

			Car car1 = new Car(carVin);

			assertTrue(car0 instanceof HibernateProxy);
			assertTrue(car1.equals(car0));
			assertTrue(car0.equals(car1));
			String aField = car0.getSomeField();

			trx.get().commit();
		} catch (Exception e) {
			PersistenceUtil.rollbackQuietly(trx);
			throw e;
		} finally {
			PersistenceUtil.closeQuietly(sessOpt);
		}
	}
}
