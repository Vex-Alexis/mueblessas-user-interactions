# Etapa 1: Construcción
FROM gradle:8.5-jdk21-alpine as build
COPY . /app
WORKDIR /app
RUN gradle clean build -x test

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/applications/app-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]