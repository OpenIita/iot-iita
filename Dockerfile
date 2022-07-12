FROM openjdk:11-jre-slim
WORKDIR /app
ADD iot-standalone/target/iot-standalone-0.3.2-SNAPSHOT.tar /app/
ADD /data /app/
EXPOSE 8086
ENTRYPOINT ["java", "-classpath", ".:lib/*","cc.iotkit.manager.Application"]