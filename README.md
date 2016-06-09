1. Install Tomcat 7 (if not installed)
2. Go to root folder and build project:
mvn clean dependency:copy-dependencies package
3. Open localhost:8080
4. To test reasoning: localhost:8080/rest/reasoner
5. If inconsistent - returns false
6. Files can be found in resource folder
