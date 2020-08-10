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

FROM payara/server-full

LABEL key="Viktor Çitaku"

ENV WEB_ARCHIVE web-module-1.0.0-SNAPSHOT.war
ENV JDBC_LIB_DIR=${PAYARA_DIR}/glassfish/domains/production/lib

# Copy over the JDBC drivers for MySQL and Postgres
COPY --chown=payara ./jdbc/mysql-connector-java-5.1.48.jar ${JDBC_LIB_DIR}
COPY --chown=payara ./jdbc/postgresql-42.2.14.jar ${JDBC_LIB_DIR}

# Copy over the post-boot-command
COPY --chown=payara ./payara/config/post-boot-commands.asadmin ${POSTBOOT_COMMANDS}

# Copy over the WAR to deployment directory
COPY --chown=payara ./web-module/target/${WEB_ARCHIVE} ${DEPLOY_DIR}
