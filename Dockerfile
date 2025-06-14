FROM openjdk:21-slim
COPY target/app_notifications.jar app_notifications.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_notifications.jar"]