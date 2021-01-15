FROM openjdk:8-jre-slim
VOLUME /tmp
ADD target/api-manager.jar /app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]