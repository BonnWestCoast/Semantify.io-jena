FROM tomcat:8.5

RUN apt-get update --fix-missing && apt-get install -y git \
maven \
vim \
openjdk-8-jdk

COPY . /usr/src/semantify/

RUN mvn -f /usr/src/semantify/pom.xml clean dependency:copy-dependencies package

RUN cp /usr/src/semantify/target/semantify.war /usr/local/tomcat/webapps/
