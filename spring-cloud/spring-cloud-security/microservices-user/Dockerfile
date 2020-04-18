FROM openjdk:8
VOLUME /tmp
ADD ./target/microservices-user-0.0.1-SNAPSHOT.jar microservices-user.jar
ENTRYPOINT ["java", "-jar", "microservices-user.jar"]
