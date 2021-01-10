FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/lib/*.jar
COPY build/libs/*.jar *.jar
ENTRYPOINT ["java","-jar","*.jar"]
