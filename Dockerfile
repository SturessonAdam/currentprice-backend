# Byggbild
FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app
COPY . .

# Bygg projektet
RUN mvn clean package -DskipTests

# Kör-bild
FROM openjdk:11-jdk AS runtime

WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
