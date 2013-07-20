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

import com.github.snd297.yp.hibernatecollections.model.BadBicycle;
import com.github.snd297.yp.hibernatecollections.model.BandMember;
import com.github.snd297.yp.hibernatecollections.model.BandMemberWithForeignKey;
import com.github.snd297.yp.hibernatecollections.model.BandOwningSide;
import com.github.snd297.yp.hibernatecollections.model.BandWithForeignKey;
import com.github.snd297.yp.hibernatecollections.model.Bicycle;
import com.github.snd297.yp.hibernatecollections.model.Wheel;
import com.github.snd297.yp.hibernatecollections.model.WheelInBadBicycle;
import com.github.snd297.yp.utils.hibernate.HibernateUtil;
import com.github.snd297.yp.utils.hibernate.IHasLongId;

public class PublicCollectionTest {

  private static Long badBicycleId;
  private static Long bicycleId;
  private static Long bandOwningSideId;
  private static Long bandFkId;

  @BeforeClass
  public static void classSetup() throws Exception {
    Session sess = null;
    Transaction trx = null;
    try {
      sess = HibernateUtil.getSessionFactory().openSession();

      trx = sess.beginTransaction();
      {

        BadBicycle badBicycle = new BadBicycle();
        sess.save(badBicycle);
        badBicycleId = badBicycle.getId();
        sess.save(new WheelInBadBicycle(badBicycle));
        sess.save(new WheelInBadBicycle(badBicycle));
      }
      {
        Bicycle bicycle = new Bicycle();
        sess.save(bicycle);
        bicycleId = bicycle.getId();
        sess.save(new Wheel(bicycle));
        sess.save(new Wheel(bicycle));
      }
      {
        BandOwningSide bandOwningSide = new BandOwningSide();
        sess.save(bandOwningSide);
        bandOwningSideId = bandOwningSide.getId();
        BandMember member0 = new BandMember();
        sess.save(member0);
        BandMember member1 = new BandMember();
        sess.save(member1);
        bandOwningSide.getMembers().add(member0);
        bandOwningSide.getMembers().add(member1);
      }
      {
        BandWithForeignKey bandFk = new BandWithForeignKey();
        sess.save(bandFk);
        bandFkId = bandFk.getId();
        BandMemberWithForeignKey memberFk0 = new BandMemberWithForeignKey();
        sess.save(memberFk0);
        BandMemberWithForeignKey memberFk1 = new BandMemberWithForeignKey();
        sess.save(memberFk1);
        bandFk.getMembers().add(memberFk0);
        bandFk.getMembers().add(memberFk1);
      }

      trx.commit();

    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }

  @Test(expected = HibernateException.class)
  public void orphanedCollection() throws Exception {
    Session sess = null;
    Transaction trx = null;
    try {
      sess = HibernateUtil.getSessionFactory().openSession();

      trx = sess.beginTransaction();

      BadBicycle bicycle = (BadBicycle) sess
          .load(BadBicycle.class, badBicycleId);

      Set<WheelInBadBicycle> wheels = bicycle.getWheels();

      assertEquals(2, bicycle.getWheels().size());
      assertTrue(wheels instanceof PersistentCollection);

      WheelInBadBicycle newWheel0 = new WheelInBadBicycle(bicycle);
      WheelInBadBicycle newWheel1 = new WheelInBadBicycle(bicycle);

      sess.save(newWheel0);
      sess.save(newWheel1);

      Set<WheelInBadBicycle> newWheels = newHashSet(newWheel0, newWheel1);

      bicycle.setWheels(newWheels);
      try {
        trx.commit();
      } catch (HibernateException he) {
        assertTrue(he
            .getMessage()
            .equals(
                "A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.github.snd297.yp.hibernatecollections.model.BadBicycle.wheels"));
        throw he;
      }

    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }

  @Test
  public void fixedCollection() throws Exception {
    Session sess = null;
    Transaction trx = null;
    Long newWheel0Id = null;
    Long newWheel1Id = null;
    try {
      sess = HibernateUtil.getSessionFactory().openSession();

      trx = sess.beginTransaction();

      Bicycle bicycle = (Bicycle) sess.load(Bicycle.class, bicycleId);
      Set<Wheel> wheels = bicycle.getWheels();

      assertEquals(2, bicycle.getWheels().size());
      assertTrue(wheels instanceof PersistentCollection);

      Wheel newWheel0 = new Wheel(bicycle);
      Wheel newWheel1 = new Wheel(bicycle);

      sess.save(newWheel0);
      sess.save(newWheel1);

      Set<Wheel> newWheels = newHashSet(newWheel0, newWheel1);

      bicycle.getWheels().clear();
      bicycle.getWheels().addAll(newWheels);
      trx.commit();

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

      Bicycle bicycle = (Bicycle) sess.load(Bicycle.class, bicycleId);
      Set<Wheel> wheels = bicycle.getWheels();

      assertEquals(2, wheels.size());

      assertNotNull(find(wheels,
          compose(equalTo(newWheel0Id), IHasLongId.getId), null));

      assertNotNull(find(wheels,
          compose(equalTo(newWheel1Id), IHasLongId.getId), null));

      trx.commit();
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }

  @Test(expected = HibernateException.class)
  public void bandOwningSide() throws Exception {
    Session sess = null;
    Transaction trx = null;
    try {
      sess = HibernateUtil.getSessionFactory().openSession();

      trx = sess.beginTransaction();

      BandOwningSide band = (BandOwningSide)
          sess.load(BandOwningSide.class, bandOwningSideId);

      assertEquals(2, band.getMembers().size());
      assertTrue(band.getMembers() instanceof PersistentCollection);

      BandMember newMember0 = new BandMember();
      sess.save(newMember0);
      BandMember newMember1 = new BandMember();
      sess.save(newMember1);

      Set<BandMember> newMembers = newHashSet(newMember0, newMember1);

      band.setMembers(newMembers);
      try {
        trx.commit();
      } catch (HibernateException he) {
        assertTrue(he
            .getMessage()
            .equals(
                "A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.github.snd297.yp.hibernatecollections.model.BandOwningSide.members"));
        throw he;
      }
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }

  }

  @Test(expected = HibernateException.class)
  public void bandFk() throws Exception {
    Session sess = null;
    Transaction trx = null;
    try {
      sess = HibernateUtil.getSessionFactory().openSession();

      trx = sess.beginTransaction();

      BandWithForeignKey band =
          (BandWithForeignKey)
          sess.load(BandWithForeignKey.class, bandFkId);

      assertEquals(2, band.getMembers().size());
      assertTrue(band.getMembers() instanceof PersistentCollection);

      BandMemberWithForeignKey newMember0 = new BandMemberWithForeignKey();
      sess.save(newMember0);

      BandMemberWithForeignKey newMember1 = new BandMemberWithForeignKey();
      sess.save(newMember1);

      Set<BandMemberWithForeignKey> newMembers =
          newHashSet(newMember0, newMember1);

      band.setMembers(newMembers);
      try {
        trx.commit();
      } catch (HibernateException he) {
        assertTrue(he
            .getMessage()
            .equals(
                "A collection with cascade=\"all-delete-orphan\" was no longer referenced by the owning entity instance: com.github.snd297.yp.hibernatecollections.model.BandWithForeignKey.members"));
        throw he;
      }
    } catch (Exception e) {
      HibernateUtil.rollbackQuietly(trx);
      throw e;
    } finally {
      HibernateUtil.closeQuietly(sess);
    }
  }

}
