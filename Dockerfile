FROM openjdk:11-jre-slim
WORKDIR /app
ADD iot-standalone/target/iot-standalone-0.4.0-SNAPSHOT.tar /app
ADD data/init /app/data/init
ADD data/components /app/data/components
ADD data/converters /app/data/converters
EXPOSE 8086
ENTRYPOINT ["java", "-classpath", ".:lib/*","cc.iotkit.Application"]