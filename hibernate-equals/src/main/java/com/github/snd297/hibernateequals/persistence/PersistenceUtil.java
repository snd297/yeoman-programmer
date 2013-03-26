package com.github.snd297.hibernateequals.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.common.base.Optional;

public class PersistenceUtil {
	public static void closeQuietly(Optional<Session> sess) {
		try {
			if (sess.isPresent() && sess.get().isOpen()) {
				sess.get().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rollbackQuietly(Optional<Transaction> trx) {
		try {
			if (trx.isPresent() && trx.get().isActive()) {
				trx.get().rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
