FROM openjdk:17-jdk-slim

WORKDIR /app

COPY POM.XML .
COPY src ./src

RUN mvn clean package

# Expose port 8080 for the backend
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/NasCloudSimulation-0.0.1-SNAPSHOT.jar"]