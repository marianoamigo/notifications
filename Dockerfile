FROM openjdk:21-slim
ARG JAR_FILE=target/notifications-0.0.1.jar
COPY ${JAR_FILE} app_notifications.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_notifications.jar"]