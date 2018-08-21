# provider-node NL
Toop-node acting as TOOP provider for the Netherlands
TOOP Provider connects to the Dutch Chamber of Commerce Test API available here: https://api.kvk.nl/api/v2/testsearch/companies?q=kvk

# First of all #
This project depends on:
- JDK 9 (http://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase9-3934878.html)
- Apache Maven 3x (https://maven.apache.org/)
- Lombok (https://projectlombok.org)

All three need to be installed, configured and running properly.

# Country specific implementation #
The way forward would be to implement the eu.toop.node.provider.ProviderService interface which would need to use a country specific Chamber of Commerce service much like the eu.toop.node.service.ChamberOfCommerceProviderService 

# Check out #
Use GIT clone to get a local copy of this repo.

# Configuration #
Before running check server.port inside:
./src/main/resources/application.properties
Also outgoing-proxy can be configured here (proxy is ignored when no value is given)

The application.properties can be placed next to the jar and it will be picked up automatically.

# Building #
You will need to build the toopapi project first!
Build using:
mvn clean install

# Running #
Run the application inside ./target/
java -jar <jar-name>.jar

# OR #
mvn spring-boot:run

# Browse to # 
TOOP Provider URL:
http://localhost:<server.port>/toopnode/provider/provide?id=Grand%20Kontex%20B.V.

 