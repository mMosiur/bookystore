# Build stage
FROM maven:3.8.4-jdk-11-slim AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn verify --fail-never
COPY src .
RUN mvn clean package

# Package stage
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/bookystore-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
