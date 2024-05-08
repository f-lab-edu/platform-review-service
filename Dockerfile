FROM openjdk:17-alpine

WORKDIR /usr/work/

COPY ./build/libs/rs-project-0.0.1-SNAPSHOT.jar /usr/work

ENTRYPOINT ["java", "-jar", "rs-project-0.0.1-SNAPSHOT.jar"]