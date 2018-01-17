# MIT License
#
# Copyright (c) 2017 Viktor Citaku
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

FROM oracle/glassfish:5.0

LABEL key="Viktor Çitaku"

ENV ADMIN_PASSWORD=admin \
    DEPLOYMENT_DIR=${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy \
    JDBC_LIB_DIR=${GLASSFISH_HOME}/glassfish/domains/domain1/lib

COPY ./jdbc/mysql-connector-java-5.1.45-bin.jar ${JDBC_LIB_DIR}
COPY ./jdbc/postgresql-42.1.4.jar ${JDBC_LIB_DIR}

RUN \
  echo "AS_ADMIN_NEWPASSWORD=${ADMIN_PASSWORD}" >> /tmp/glassfishpwd && \
  asadmin start-domain && \
  asadmin --user admin --passwordfile=/tmp/glassfishpwd create-jdbc-connection-pool \
  --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource \
  --restype javax.sql.ConnectionPoolDataSource \
  --property url="jdbc\:mysql\://modular-jee-mysql\:3306/simple_db?user\=root&password\=root" \
   ModularJeeMySQLPool && \
  asadmin --user admin --passwordfile=/tmp/glassfishpwd create-jdbc-resource \
  --connectionpoolid ModularJeeMySQLPool jdbc/ModularJeeMySQLDataSource && \
  asadmin --user admin --passwordfile=/tmp/glassfishpwd create-jdbc-connection-pool \
  --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
  --restype javax.sql.ConnectionPoolDataSource \
  --property url="jdbc\:postgresql\://modular-jee-postgres\:5432/simple_db:username=postgres:password=root" \
   ModularJeePostgreSQLPool && \
  asadmin --user admin --passwordfile=/tmp/glassfishpwd create-jdbc-resource \
  --connectionpoolid ModularJeePostgreSQLPool jdbc/ModularJeePostgreSQLDataSource

ENV WEB_ARCHIVE web-module.war
COPY ./web-module/target/${WEB_ARCHIVE} ${DEPLOYMENT_DIR}

