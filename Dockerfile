FROM tomcat:8.0

RUN apt-get update --fix-missing && apt-get install -y git \
maven
