FROM openjdk:11-jre-slim
WORKDIR /app
ADD iot-starter/target/iot-starter-0.4.5-SNAPSHOT.jar /app/app.jar
ADD data/init /app/data/init
ADD data/components /app/data/components
ADD data/converters /app/data/converters
