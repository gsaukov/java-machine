# oracle/jdk:11 https://github.com/oracle/docker-images/tree/master/OracleJava
# Netflix proxy seervise Zuul image

FROM oracle/jdk:11
EXPOSE 8090
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000
ARG JAR_FILE=/build/libs/zuul.jar
ARG TRUSTSTORE=/build/resources/main/truststore.jks
ADD ${TRUSTSTORE} truststore.jks
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]