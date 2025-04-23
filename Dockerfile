FROM openjdk:17.0.1-jdk-slim

COPY --from=build /target/NasCloudSimulation-0.0.1-SNAPSHOT.jar NasCloudSimulation-0.0.1-SNAPSHOT.jar

RUN mvn clean package

# Expose port 8080 for the backend
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "NasCloudSimulation-0.0.1-SNAPSHOT.jar"]