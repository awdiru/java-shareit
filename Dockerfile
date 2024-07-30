FROM amazoncorretto:11
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
ENV SHAREIT_IMAGE_DIRECTORY "shareit-image"