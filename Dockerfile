FROM openjdk:17-alpine3.14
EXPOSE 8081
ADD target/CloudStorage-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "CloudStorage-0.0.1-SNAPSHOT.jar"]