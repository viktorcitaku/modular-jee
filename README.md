 # Modular Jakarta EE 8

 ---

 Simple Jakarta EE 8 Modular Architecture. The intention is to test a simple modular architecture by providing two implementations for different data sources for example one for MySQL DB and the other one for PostgreSQL DB.
 
 To run the application execute the following commands:
 
 1. build project
``mvn clean install``
 2. start the containers
 ``docker-compose up``
 3. For admin console go to: https://localhost:4848/ user: admin & password = admin
 4. Rest endpoint url: http://localhost:8080/web-module/api/persons
  
 The development environment was:
 1. OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.8+10) (The project itself targets to run under Java 8)
 2. Docker 19.03.12 CE

 Architecture overview:
 
![alt Architecture Overview](https://github.com/viktorcitaku/modular-jee/blob/master/architecture/ModularJavaEE.png)

* **web-module** can run also without one of the Jars, either **mysql-module** or **postgres-module**.
    - When one of the Jars is removed _beans.xml_ should be modified in **web-module** by changing: ``  <alternatives>
    <class>io.github.viktorcitaku.mysqlmodule.UserDaoBean</class>
  </alternatives> `` accordingly. 
* **contract** module contains entity classes, interfaces, qualifiers, cdi stereotypes, JPA Producers and so on.
    - The interface called ``UserDao`` has two implementation which live on one of the modules **mysql-module.jar** and **postgres-module**.
    - The entity class under the **contract** module, should be included in the ``persistence.xml``, because JPA scans just the War and not the Jars.
* The purple boxes represent the main specs supported by Jakarta EE 8 but not limited to. The reference implementation is Payara Server (Full) Community Edition.
