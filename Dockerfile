FROM openjdk:11
WORKDIR ./
COPY ./build/libs/paper-plane-0.0.1-SNAPSHOT.jar ./app
CMD java -jar app/paper-plane-0.0.1-SNAPSHOT.jar