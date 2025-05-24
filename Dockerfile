FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "app.jar"]
