# SpringBoot Rest API Boilerplate


## Development

To start your application in the dev profile, run:

```
./gradlew
```


## Building for production

### Packaging as jar

To build the final jar and optimize the basicSample application for production, run:

```
./gradlew -Pprod clean bootJar
```

To ensure everything worked, run:

```
java -jar build/libs/*.jar
```


### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./gradlew -Pprod -Pwar clean bootWar
```

## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```


## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker-compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./gradlew bootJar -Pprod jibDockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

## Motivation
- With this skeleton, we can easily start a server application using Java Spring Boot.
- Rather than spending time on the project setup, get on with the important stuff right away.

Take it for a test drive. We'd love to hear any feedback you have or if you've thought of a new feature.


## Project Structure
| Name | Description |
| ------------------------ | --------------------------------------------------------------------------------------------- |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/.github** | Contains GitHub settings and configurations, including the GitHub Actions workflows  |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/blob/main/build.gradle** | Contains all your gradle dependencies |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/blob/main/sonar-project.properties**| Contains sonar properties |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest**| This package contains all the sub packages |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/aop**| This package contains AOP Logging (Aspect-Oriented Programming) |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/config**| This package contains all the configuration files |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/domain**| This package contains DTOs, Ports & Service implementations |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/domain/dto**| This package contains Data Transfer Objects |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/domain/port**| This package contains Application Programming Interface (API) & Service Provider Interface (SPI) |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/domain/port/api**| This package contains the service ports for services |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/domain/port/spi**| This package contains the persistence ports for JPA Adapters |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/domain/service**| This package contains the service implementations for service ports |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/infrastructure**| This package contains JPA Adaptors, Entities, Repositories |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/infrastructure/adaptor**| This package contains JPA Adapters which implements Persistence Ports |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/infrastructure/entity**| This package contains the entities and their database mappings|
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/infrastructure/repository**| This package contains Repositories which are extended by JpaRepository |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/mapper**| This package contains mappers for converting Entities to DTOs |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/rest**| This package contains Controllers, Custom Exceptions & VM|
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/java/com/springboot/rest/security**| This package contains  JWT & Security Utils|
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/blob/main/src/main/java/com/springboot/rest/BasicSampleApp.java**| This is a sample Spring Boot Application  |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/resources/config/liquibase**| This package contains Liquibase database change logs |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/main/resources/config**| This package contains Configuration files application.yml (-dev , -prob, -tls) |
| **https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/tree/main/src/test/java/com/springboot/rest**| This package contains integration tests |


## Request Response Workflow
![Project Flow](https://user-images.githubusercontent.com/52003038/138070423-cc830e96-cbef-445e-9143-a7a006d65c8b.png)
### Advantages
1. Immediate implementation
2. Focus on the domain of the application
3. Possibility of changes
4. Expected test
5. Optimal result


## Modules
* Logger
* Request Response
* Database
* Swagger/Open API


## Wiki
- [Project Set Up Guide](https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/wiki)
- [Clean Architecture](https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/wiki/Clean-Architecture)
- [JSON Web Tokens](https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/wiki/JSON-Web-Tokens)
- [Security](https://github.com/NeoSOFT-Technologies/rest-java-spring-boot/wiki/Security)
