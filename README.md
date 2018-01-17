 # Modular Java EE 8

 ---

 Simple Java EE 8 Modular Architecture application! The intention was to test modular architecture
 by providing two implementation for different datasource one for MySQL and the other for PostgreSQL.
 
 To run the application execute the following commands:
 
 1. build project
``mvn clean install``
 2. start the containers
 ``docker-compose up``
 3. Enjoy!
  
 The development environment was:
 1. JDK9 (The project it self is target to run with Java 8)
 2. Docker 17.12.0 CE

 Architecture overview:
 
![alt Architecture Overview](https://github.com/viktorcitaku/modular-jee/blob/master/architecture/ModularJavaEE.png)

- **web-module.war** can run also without one of the **mysql-module.jar** and **postgres-module**.
- - When one of the modules is removed _beans.xml_ should be modified in **web-module.war** where the following line of code should be changes: ``  <alternatives>
    <class>io.github.viktorcitaku.mysqlmodule.UserDaoBean</class>
  </alternatives> ``.
- **contract.jar** module contains entity classes, interfaces, qualifiers, cdi stereotypes, JPA Producers for either MySQL or PostgresSQL.
- - The inteface called ``UserDao`` has two implementation which live on one of the modules **mysql-module.jar** and **postgres-module**.
- - The entity class defined in contract is necessary to be included in the entity classes in the ``persistence.xml`` since JPA seems to scan just the war and not the JARs too.
- The purple boxes represent the main technologies used among many other provided by Java EE 8.
