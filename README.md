# Semantify IO


## Install the system

The instructions are mainly for Linux, but this guide will be useful for any operating system

## Old style installation

1. [Download](https://tomcat.apache.org/whichversion.html) and install the last version of Tomcat (we used [Tomcat 7.0.72](https://tomcat.apache.org/download-70.cgi)).
2. [Download](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and install the last version of Java (we use [Java 1.8.0_102](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)).
3. Add Java and Tomcat to the path of the operating system (We assume that we installed things in the folder `/usr/local/`). In Linux, do the following:
    1. Add the next lines to the file `.bashrc`:

            export JAVA_HOME="/usr/local/jdk1.8.0_102"
            export JRE_HOME="/usr/local/jdk1.8.0_102/jre"
            export CATALINA_HOME="/usr/local/tomcat7"

    2. Load this configurations: `source ~/.bashrc`
    3. Be sure that all is ok: `echo $JAVA_HOME`
4. Install Maven.
    1. Debian/Ubuntu: `sudo apt-get install maven`
5. If we want to have access to the admin GUI we add users and roles in the file `conf/tomcat-users.xml` which is in the
   Tomcat folder. Be sure the next lines are in the file:

        <tomcat-users>
            <role rolename="manager-gui"/>
            <user username="tomcat" password="****" roles="manager-gui"/>
        </tomcat-users>

6. Go to the root foolder and build the project: `mvn clean dependency:copy-dependencies package`.
7. Copy `semantify.war` file into the Tomcat `webapps` folder.
    1. In Linux: `sudo cp target/semantify.war $tomcat7/webapps/rest.war`
5. Startup Tomcat: `$CATALINA_HOME/bin/startup.sh`
8. Website should be accessible from http://localhost:8080/rest

**Note:** It is possible to use any version of Apache and Java, the
recommendation is to use the most updated version and verify that all the
dependencies are working correctly.

## Using Docker

1. Download and install docker from: https://www.docker.com/
2. Run in console: `docker run -itdp 8080:8080 --name jena akorovin/semantify.io-jena`.
it will download Docker image and build it on your host. And also it will build Github project and run it on Tomcat.
3. Website should be accessible from http://localhost:8080/rest
