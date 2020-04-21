# Spring Cloud Example with Security

In this folder you're going to find a microservices project built with Java/Spring cloud.

In order to run this project you must need maven and docker and follow the next steps.

1. Build all the jar using the next command in the root of each project:

```jshelllanguage}
mvn clean install -DskipTests
```

2. After build all the jars, we need to create all the docker using the next command in the root of each project:

```jshelllanguage}
docker build -t tag-name:version .
```

3. When the previous two step are ready, we must go to the folder docker-compose and run the next command:

```jshelllanguage}
docker-compose up
```

With this steps, our application should be ready and running.!