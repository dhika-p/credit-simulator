# Stage 1: build jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/credit-simulator.jar target/credit-simulator.jar
COPY credit_simulator file_inputs.txt ./
RUN chmod +x credit_simulator
ENTRYPOINT ["./credit_simulator"]
