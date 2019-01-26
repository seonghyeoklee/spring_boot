FROM openjdk:8-jdk-alpine

ADD . /application
WORKDIR /application
RUN ./gradlew clean build
WORKDIR /application/build/libs
USER nobody
CMD [ "java", "-jar", "boot1-0.0.1-SNAPSHOT.jar" ]
EXPOSE 9090