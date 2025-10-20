# Étape 1 : Build Maven
FROM eclipse-temurin:17-jdk-alpine AS build

# Définir le dossier de travail
WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY src ./src

# Installer Maven et construire le projet
RUN apk add --no-cache maven \
    && mvn clean package -DskipTests

# Étape 2 : Image finale
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copier le JAR final
COPY --from=build /app/target/DashBot-1.0.jar app.jar

# Exposer le port HTTP utilisé par DashBot
EXPOSE 8080

# Variables d’environnement (à adapter si besoin)
ENV BOT_TOKEN="" \
    WEBHOOK_PORT=8080

# Lancer l’application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]