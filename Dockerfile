FROM openjdk:17-alpine

WORKDIR /usr/work

EXPOSE 8080

COPY ./build/libs/rs-project-0.0.1-SNAPSHOT.jar /usr/work

CMD ["java", "-jar", "rs-project-0.0.1-SNAPSHOT.jar"]