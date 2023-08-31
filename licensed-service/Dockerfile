FROM openjdk:17-alpine
WORKDIR /app
COPY ./licensed-service/target/*.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]