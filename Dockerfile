# Stage 1: Build
FROM maven:3.9.9-openjdk-17-slim AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/NasCloudSimulation-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the backend
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]