# 1) 빌드 스테이지
FROM gradle:8-jdk21 AS builder
WORKDIR /app

# Python 및 docxtpl + requests + Pillow 설치
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    pip3 install docxtpl requests Pillow --break-system-packages

COPY . .
RUN gradle clean bootJar --no-daemon    # build/libs/*.jar 생성

# 2) 런타임 스테이지
FROM openjdk:21-jdk-slim
WORKDIR /app

# Python 및 필요한 패키지 재설치
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    pip3 install docxtpl requests Pillow --break-system-packages && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 앱 및 리포트 스크립트 복사
COPY .env .env
COPY --from=builder /app/build/libs/*.jar app.jar
COPY python-report/ python-report/

ENTRYPOINT ["java", "-jar", "app.jar"]
