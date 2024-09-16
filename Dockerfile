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