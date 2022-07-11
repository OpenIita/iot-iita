FROM openjdk:11-jre-slim
WORKDIR /app
ADD target/iot-standalone.tar /app
EXPOSE 8086
RUN touch .init
ENTRYPOINT ["java", "-classpath", ".:lib/*","cc.iotkit.manager.Application"]