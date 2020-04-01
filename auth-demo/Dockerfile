# oracle/jdk:11 https://github.com/oracle/docker-images/tree/master/OracleJava
# Auth-demo image

FROM oracle/jdk:11
EXPOSE 8097 9093
ARG JAR_FILE=/build/libs/auth-demo.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]