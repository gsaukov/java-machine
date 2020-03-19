# oracle/jdk:11 https://github.com/oracle/docker-images/tree/master/OracleJava
# Netflix proxy seervise Zuul image

FROM oracle/jdk:11
EXPOSE 8090
ARG JAR_FILE=/build/libs/zuul.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]