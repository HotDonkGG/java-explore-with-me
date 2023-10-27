FROM amazoncorretto:11
VOLUME /tmp
COPY build/libs/ewm-service.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#FROM amazoncorretto:11
#COPY ./target/ewm-service-0.0.1-SNAPSHOT.jar /app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
