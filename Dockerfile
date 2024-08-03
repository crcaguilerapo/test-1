FROM openjdk:21-jdk-slim

COPY build/libs/franquicia-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]