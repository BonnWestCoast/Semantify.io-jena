
# README 

## Java and Tomcat

1. Install Tomcat 7, follow the instructions in the web page:
   `https://tomcat.apache.org/tomcat-7.0-doc/appdev/installation.html`
2. Install Java 6 and be sure that it will be added to the correspondant
   operating system: `www.oracle.com/technetwork/java/javase/downloads/index.html`
3. Add users and roles in the file `conf/tomcat-users.xml`

**Note:** It is possible to use any version of Apache and Java, the
recommendation is to use the most updated version and verify that all the
dependencies are working correctly

## Docker

1. Install docker from: https://www.docker.com/
2. Run in console: `docker run -itdp 8080:8080 --name jena akorovin/semantify.io-jena`. 
it will download docker image and build it on your host. And also it will build github project and run it on tomcat.
3. website should be accessible from `localhost:8080\rest`
