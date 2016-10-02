
1. Install Tomcat 7 (if not installed)
2. Add users and roles in the file `conf/tomcat-users.xml`
3. Go to root folder and build project: `mvn clean dependency:copy-dependencies package`
4. Import the `semantify.war` file in the webapps folder of Tomcat
5. Open `http://localhost:8080`
6. To test reasoning: `http://localhost:8080/semantify/rest/reasoner`
7. If inconsistent - returns false
8. Files can be found in resource folder

#Docker
1. Install docker from: https://www.docker.com/
2. docker run -itdp 8080:8080 --name jena akorovin/semantify.io-jena
3. website should be accessible from localhost:8080
