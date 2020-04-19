FROM openjdk:8
VOLUME /tmp
ADD ./target/service-oauth-0.0.1-SNAPSHOT.jar service-oauth.jar
ENTRYPOINT ["java", "-jar", "service-oauth.jar"]
