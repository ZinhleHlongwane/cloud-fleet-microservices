FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/drone-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
