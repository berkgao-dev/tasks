FROM openjdk:21
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/task-api-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]