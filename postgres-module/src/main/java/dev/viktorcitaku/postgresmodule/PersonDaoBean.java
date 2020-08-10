/*
 * MIT License
 *
 * Copyright (c) 2017 Viktor Citaku
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.viktorcitaku.postgresmodule;

import dev.viktorcitaku.contract.Person;
import dev.viktorcitaku.contract.PersonDao;
import dev.viktorcitaku.contract.config.CommonException;
import dev.viktorcitaku.contract.config.DaoBean;
import dev.viktorcitaku.contract.config.PostgreSQLDataSource;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@DaoBean
public class PersonDaoBean implements PersonDao {

  @Inject @PostgreSQLDataSource EntityManager em;

  @Override
  public void create(Person person) {
    if (person == null) throw new CommonException("Person cannot be null!");
    em.persist(person);
  }

  @Override
  public List<Person> getPersons() {
    return em.createQuery("SELECT u FROM Person u", Person.class).getResultList();
  }

  @Override
  public Person update(Person person) {
    if (person == null) throw new CommonException("Person cannot be null!");
    return em.merge(person);
  }

  @Override
  public void delete(Person person) {
    if (person == null) throw new CommonException("Person cannot be null!");
    em.remove(person);
  }

  @Override
  public Person findById(Long id) {
    if (id == null || id <= 0)
      throw new CommonException("Person id cannot be null or 0 or negative number!");
    return em.find(Person.class, id);
  }
}
