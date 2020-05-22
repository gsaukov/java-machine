# oracle/jdk:11 https://github.com/oracle/docker-images/tree/master/OracleJava
# Spring Oauth2 server Justitia image

FROM oracle/jdk:14
EXPOSE 8002
ARG JAR_FILE=/build/libs/justitia.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
