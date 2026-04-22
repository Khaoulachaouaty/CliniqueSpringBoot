# Multi-stage build pour image optimisée
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier pom.xml d'abord (pour optimiser le cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source et compiler
COPY src ./src
RUN mvn clean package -DskipTests

# Image d'exécution (légère)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Sécurité : utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copier le JAR depuis l'étape build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]