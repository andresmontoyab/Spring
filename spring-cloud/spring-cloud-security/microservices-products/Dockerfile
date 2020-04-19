FROM openjdk:8
VOLUME /tmp
ADD ./target/microservices-products-0.0.1-SNAPSHOT.jar microservices-products.jar
ENTRYPOINT ["java", "-jar", "microservices-products.jar"]
