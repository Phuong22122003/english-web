# Build

# create image
FROM amazoncorretto:21.0.4

WORKDIR /app
COPY target/learning-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]