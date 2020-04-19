FROM openjdk:8
VOLUME /tmp
ADD ./target/microservices-items-0.0.1-SNAPSHOT.jar microservices-items.jar
ENTRYPOINT ["java", "-jar", "microservices-items.jar"]
