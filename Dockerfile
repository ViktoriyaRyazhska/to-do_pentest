FROM eclipse-temurin:17-jdk-alpine
COPY ${JAR_FILE} to-do.jar
ENTRYPOINT ["java","-jar","/app.jar"]