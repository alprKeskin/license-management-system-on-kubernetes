FROM openjdk:17-alpine
WORKDIR /app
# for only this app image
# COPY ./target/*.jar app.jar
# for two app image
COPY ./license-management-system-customer/target/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]