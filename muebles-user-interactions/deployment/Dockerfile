# Etapa 1: Build del proyecto con Gradle

FROM gradle:8.7-jdk21-alpine AS builder

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar todo el proyecto (solo lo necesario para compilar)
COPY . .

# Ejecutar build con Gradle (sin tests si quieres más rápido)
RUN gradle clean build -x test


# Etapa 2: Imagen ligera para ejecución

FROM eclipse-temurin:21-jdk-alpine

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el .jar compilado desde tu máquina al contenedor
COPY /app/muebles-user-interactions/applications/app-service/build/libs/UserInteractions.jar app.jar

# Puerto expuesto (si aplica, cambia si tu app usa otro)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]