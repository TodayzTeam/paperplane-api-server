FROM openjdk:11
WORKDIR ./
COPY ./build/libs/paper-plane-0.0.1-SNAPSHOT.jar ./
CMD java -jar paper-plane-0.0.1-SNAPSHOT.jar