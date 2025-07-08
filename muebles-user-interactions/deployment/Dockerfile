FROM eclipse-temurin:21-jdk-alpine
# Crear el directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la ruta relativa al contexto del build
COPY applications/app-service/build/libs/UserInteractions.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]