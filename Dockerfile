# 1) 빌드 스테이지
FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon    # build/libs/*.jar 생성

# 2) 런타임 스테이지
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY .env .env
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]