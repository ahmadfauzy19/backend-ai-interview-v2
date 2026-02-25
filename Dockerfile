# =========================
# STAGE 1 - Build
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy hanya pom.xml untuk cache dependency layer
COPY pom.xml .

# Download dependency yang diperlukan saja (lebih aman dari go-offline)
RUN mvn -B -q -e -C dependency:resolve

# Copy source
COPY src ./src

# Build jar
RUN mvn -B clean package -DskipTests

# =========================
# STAGE 2 - Runtime
# =========================
FROM eclipse-temurin:17-jdk-jammy

# Install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]