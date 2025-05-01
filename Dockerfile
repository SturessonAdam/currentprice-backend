# Byggbild med Maven och Java 17
FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

# Bygg projektet
RUN mvn clean package -DskipTests

# Kör-bild med Java 17
FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]

