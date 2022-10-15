# oracle/jdk:11 https://github.com/oracle/docker-images/tree/master/OracleJava
# Netflix discovery image

FROM amazoncorretto:17
EXPOSE 8761
ARG JAR_FILE=/build/libs/discovery.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
