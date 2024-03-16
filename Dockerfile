FROM maven:3-amazoncorretto-17 AS build

WORKDIR /app

COPY . /app

RUN mvn clean package

# multi-stage build
FROM openjdk:17.0.1-slim

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 3004

CMD ["java", "-jar", "app.jar"]