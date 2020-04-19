FROM openjdk:8
EXPOSE 8090
VOLUME /tmp
ADD ./target/zuul-api-gateway-0.0.1-SNAPSHOT.jar zuul-api-gateway.jar
ENTRYPOINT ["java", "-jar", "zuul-api-gateway.jar"]
