FROM openjdk:8-jre
MAINTAINER Alan Turing <helloalanturing@icloud.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/imjcker/spring-cloud-service-elastic.jar"]

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
# ADD target/lib           /usr/share/myservice/lib
# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/imjcker/spring-cloud-service-elastic.jar
