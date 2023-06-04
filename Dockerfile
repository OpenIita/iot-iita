FROM openjdk:11-jre-slim
WORKDIR /app
ADD iot-starter/target/iot-starter-0.4.3-SNAPSHOT.jar /app
ADD data/init /app/data/init
ADD data/components /app/data/components
ADD data/converters /app/data/converters
EXPOSE 8086
ENTRYPOINT ["java", "-classpath", ".:lib/*","cc.iotkit.Application"]