FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests


FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -g 1000 bcuser && adduser -u 1000 -G bcuser -S bcuser

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN chown -R bcuser:bcuser /app

USER bcuser

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
