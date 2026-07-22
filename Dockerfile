# Use a lightweight JDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the build file and the source code
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
ENTRYPOINT ["java", "-jar", "target/cloud-fleet-microservices-0.0.1-SNAPSHOT.jar"]
