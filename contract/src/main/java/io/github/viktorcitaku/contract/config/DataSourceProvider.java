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

package io.github.viktorcitaku.contract.config;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class DataSourceProvider {

  // In Java 9 and above System.getLogger(...) will be used!
  // private static final System.Logger LOGGER = System.getLogger(Class.class.getName());
  private static final Logger LOGGER = Logger.getLogger(DataSourceProvider.class.getName());

  @Produces
  @MySQLDataSource
  public EntityManager getMySQLDataSource() {
    LOGGER.info("MySQL Data Source is picked up!");

    return Persistence.createEntityManagerFactory("mySQL-pu")
      .createEntityManager();
  }

  @Produces
  @PostgreSQLDataSource
  public EntityManager getPostgreSQLDataSource() {
    LOGGER.info("PostgreSQL Data Source is picked up!");
    return Persistence.createEntityManagerFactory("postgreSQL-pu")
      .createEntityManager();
  }
}