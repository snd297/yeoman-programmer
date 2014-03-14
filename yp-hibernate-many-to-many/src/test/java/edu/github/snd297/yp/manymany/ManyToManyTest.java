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
package edu.github.snd297.yp.manymany;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.github.snd297.yp.manymany.persistence.Employee;
import com.github.snd297.yp.manymany.persistence.Project;
import com.github.snd297.yp.utils.hibernate.HibernateUtil;

public class ManyToManyTest {

  @Test
  public void manyToMany() throws Exception {
    Long projectId;
    {
      Session sess = null;
      Transaction trx = null;
      try {
        sess = HibernateUtil.getSessionFactory().openSession();

        trx = sess.beginTransaction();

        Employee employee0 = new Employee();
        sess.save(employee0);

        Employee employee1 = new Employee();
        sess.save(employee1);

        Project project = new Project();
        sess.save(project);
        project.getEmployees().add(employee0);
        project.getEmployees().add(employee1);

        trx.commit();

        projectId = project.getId();

      } catch (Exception e) {
        HibernateUtil.rollbackQuietly(trx);
        throw e;
      } finally {
        HibernateUtil.closeQuietly(sess);
      }
    }
    {
      Session sess = null;
      Transaction trx = null;
      try {
        sess = HibernateUtil.getSessionFactory().openSession();
        trx = sess.beginTransaction();
        Project project = (Project) sess.get(Project.class, projectId);

        Employee newEmployee = new Employee();
        project.setEmployees(newHashSet(newEmployee));
        sess.save(newEmployee);

        trx.commit();

      } catch (Exception e) {
        HibernateUtil.rollbackQuietly(trx);
        throw e;
      } finally {
        HibernateUtil.closeQuietly(sess);
      }
    }
    {
      Session sess = null;
      Transaction trx = null;
      try {
        sess = HibernateUtil.getSessionFactory().openSession();
        trx = sess.beginTransaction();
        Project project = (Project) sess.load(Project.class, projectId);
        project.getEmployees();
        assertEquals(1, project.getEmployees().size());
        trx.commit();
      } catch (Exception e) {
        HibernateUtil.rollbackQuietly(trx);
        throw e;
      } finally {
        HibernateUtil.closeQuietly(sess);
      }
    }
  }
}
