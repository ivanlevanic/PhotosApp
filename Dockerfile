FROM eclipse-temurin:11-jdk-alpine
WORKDIR /home/app
COPY ./target/ivan-project.jar PhotosApp.jar
ENTRYPOINT ["java", "-jar", "/home/app/PhotosApp.jar"]