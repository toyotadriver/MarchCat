FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar marchcat.jar

#COPY marchcat-0.0.3-SNAPSHOT.jar /app/marchcat-0.0.3-SNAPSHOT.jar

#RUN mvn build clean install

#FROM openjdk:21-jdk

CMD ["java", "-jar", "marchcat.jar"]
