FROM openjdk:8-jdk-alpine
# FROM java:8
VOLUME /tmp
ADD imjcker-backend.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
