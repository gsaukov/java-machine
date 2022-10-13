# oracle/jdk:11 https://github.com/oracle/docker-images/tree/master/OracleJava

FROM amazoncorretto:17
EXPOSE 9092
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000
ARG JAR_FILE=/build/libs/potok-1.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
