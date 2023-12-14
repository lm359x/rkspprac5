FROM amazoncorretto:17-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar
COPY --from=builder /app/target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
