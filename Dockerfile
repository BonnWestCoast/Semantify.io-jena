FROM tomcat:8.0

ONBUILD apt-get update --fix-missing && apt-get install -y git /
                                                       maven
