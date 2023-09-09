FROM eclipse-temurin:17-jdk
WORKDIR /home/app
COPY ./target/ivan-project-0.0.1-SNAPSHOT.jar PhotosApp.jar
ENTRYPOINT ["java", "-jar", "/home/app/PhotosApp.jar"]