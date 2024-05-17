FROM openjdk:17-alpine

WORKDIR /usr/work

EXPOSE 8080

COPY ./build/libs/rs-project-0.0.1-SNAPSHOT.jar /usr/work
COPY ./scouter /usr/work

CMD ["java", "-javaagent:/scouter/scouter.agent.jar",\
"-Dscouter.config=/scouter/scouter.conf","-jar", "rs-project-0.0.1-SNAPSHOT.jar"]