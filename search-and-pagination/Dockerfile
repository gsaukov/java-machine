# Netflix proxy seervise Zuul image

FROM amazoncorretto:17
EXPOSE 8081
#ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000
ARG JAR_FILE=/build/libs/search-and-pagination-1.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
