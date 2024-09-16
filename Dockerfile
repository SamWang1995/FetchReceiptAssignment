# Stage 1: Build the application using Maven
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application (this will download dependencies and build the JAR)
RUN mvn clean package

# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file into the container (ensure the name matches the actual JAR file)
COPY target/receipts-0.0.1-SNAPSHOT.jar /app/receipt-application.jar

# Expose the port the app runs on
EXPOSE 8080

# Set the command to run the jar file
ENTRYPOINT ["java", "-jar", "receipt-application.jar"]