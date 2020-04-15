# Spring Cloud
     
* [Spring Cloud](#Spring-Cloud)
    * [Microservice](#Microservice)
    * [Creating Microservices](#Creating-Microservices)
        * [Spring Cloud Config Server](#Spring-Cloud-Config-Server)
            * [Create Config Server](#Create-Config-Server)
            * [Initialize Git Repository](#Initialize-Git-Repository)
            * [Create Properties](#Create-Properties)
            * [Connecting Microservices with Config Server](#Connecting-Microservices-with-Config-Server)
            * [Refresh Scope](#Refresh-Scope)
        * [Rest Calls](#Rest-Calls)    
            * [Rest Template](#Rest-Template)    
            * [FEING](#FEING)
        * [Client Side Load Balancing](#Client-Side-Load-Balancing)    
            * [Ribbon & Feign](#Ribbon-&-Feign)
            * [Ribbon & Rest Template](#Ribbon-&-Rest-Template)
        * [Naming Server](#Naming-Server)   
            * [How it works?](#How-it-works?)   
            * [Eureka Naming Service](#Eureka-Naming-Service) 
                * [Install Eureka Server](#Install-Eureka-Server) 
                * [Connect to Eureka Server](#Connect-to-Eureka-Server) 
        * [API Gateway](#API-Gateway)  
            * [Zuul-API-Gateway](#Zuul-API-Gateway)  
                * [Setup Zuul](#Setup-Zuul)
                * [Setup Microservices Paths](#Setup-Microservices-Paths)
                * [Filters](#Filters)  
                    * [Pre](#Pre)  
                    * [Post](#Post)  
                    * [Route](#Route)  
        * [Distributed Tracing](#Distributed-Tracing)
            * [Spring Cloud Sleuth](#Spring-Cloud-Sleuth)
            * [Zipkin Distributed Tracing](#Zipkin-Distributed-Tracing)
                * [Install Zipkins](#Install Zipkins)
                * [Add Zipkins Dependency](#Add-Zipkins-Dependency)
                * [Setup Sample](#Setup-Sample)
                * [Zipkins Tags](#Zipkins-Tags)
                * [Zipkins and RabbitMQ](#Zipkins and RabbitMQ)
        * [Fault Tolerance](#Fault-Tolerance)
            * [Hystrix](#Hystrix)
    
# Spring Cloud

# Microservice

Small autonomous services that work together.

1. Rest
2. Small well chosen deployable units
3. Cloud enabled

## Challenges with microservices

## Creating Microservices

Dependencies for the Microservices

1. Web
2. DevTools
3. Actuator
4. Config Client

# Spring Cloud Config Server

Spring Cloud Config Server is a tool in where we can have a central management for the configuration of all the microservices , with this server we can 
also have the properties for each environment like dev,qa,stage and prod.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/spring-cloud-config-server-diagram.JPG)

## Create Config Server

In https://start.spring.io/ create a project with the next dependencies

Dependencies:

1. Config Server

2. Enable Config Server

```java
@EnableConfigServer
@SpringBootApplication
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }

}
```

## Initialize Git Repository

In order to setup our Spring Config Server, we need to create a local/remote git repository in where we are going to 
store our configurations files.

1. Choose a path in where you want to create your local/remote repository.

2. In that path create a specific folder to store all the configurations.

```jshelllanguage
mkdir {folder.name}
```

Where {folder.name} is the name that you want for your folder

2. When you have your folder already create, move into the folder and init git (For this step you must have git installed)

```jshelllanguage
cd {folder.name}
git init
```

3. Setup Git directory in config server .properties.

In order to linked the previous local/remote repository created with our config server we need to add the next properties.

```properties
spring.application.name={app.name}
server.port={port}
spring.cloud.config.server.git.uri={git.folder.path}
```

Where:

{app.name} is the name that you want for your config server, Example : config-server
{port} is the port number in where you want that the application runs, Example : 8888
{git.folder.path} is the  route in where the folder was created, Example: file:///C:/Users/andres.montoya/Documents/config 

## Create Properties

Now we are able to create our properties files, these files must be created inside our git folder and these properties 
file could be per application and also per environment.

Let's say that we have a microservices that is called item-service, for that microservice we could create the next properties files.

item-service.properties

```properties
server.port=8005
configuration.text=Text for default environment
```

item-service-dev.properties

```properties
server.port=8007
configuration.text=Text for dev environment
author.name=Andres
author.lastname=Montoya
```

item-service-qa.properties

```properties
server.port=8009
configuration.text=Text for Qa environment
author.name=Felipe
author.lastname=Montoya
```

As you can see we can create properties files per environment, and all of the files must be in our git repository.

If you want to check the information in your config server you can call to the next enpoint.

{host}/{microservices-name}/{environment}, Example: http://localhost:8888/item-service/dev

## Connecting Microservices with Config Server

In the previous steps we setup our Spring Config Server and also the git local/remote repository, now is time to connect
each microservices with the config server, for that we can follow the next steps.

1. Add the Spring Config Starter dependency 

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

2. Create an bootstrap.properties

The bootstrap.properties file has a higher priority than application.properties, in order to use Spring config, we need to
setup our bootstrap.properties with the next information

```properties
spring.application.name=item-service            ## Name of the microservice
spring.profiles.active=dev                      ## Profile to use
spring.cloud.config.uri=http://localhost:8888   ## Url in where the config server is deployed
```

The spring.application.name property must match with the files created in the git local/remote repository.

3. Use properties in the microservices.      

```java
@RestController
public class ItemController {

    @Value("${configuration.text}")
    private String text;

    @Value("${author.name}")
    private String authorName;

    @Value("${author.lastname}")
    private String authorLastname;

    private final Environment environment;


    @GetMapping("/getConfig")
    public ResponseEntity<?> getConfig(@Value("${server.port}") String port){
        Map<String,String> jsonValue = new HashMap<>();
        jsonValue.put("text", text);
        jsonValue.put("port", port);
        jsonValue.put("name", authorName);
        jsonValue.put("lastname", authorLastname);
        Arrays.asList(environment.getActiveProfiles()).stream()
                .filter(env -> env.equalsIgnoreCase("dev"))
                .forEach(x -> jsonValue.put("environment", "We are in dev"));
        return new ResponseEntity<Map<String, String>>(jsonValue, HttpStatus.OK);
    }
}
```

As you can see in the above code, in our microservice now we are able to use the properties defined in the spring-config-server
using the @Value annotation and the name of the property.

## Refresh Scope

There is still one more problem, even that our microservices is using the config server,  we must restart the application in order
to have the last version of our properties, in order to avoid those restart we can use the Refresh Scope annotation, 
basically means that if we updated some properties we can refresh the scope and those properties
are going to be updated in the microservices too. 

1. Add Actuator dependency to our microservices.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. Mark the classes that we want to refresh with the @RefreshScope Annotation

3. Add the next property to the bootstrap.properties file.

```properties
management.endpoints.web.exposure.include=*
```

4. Call the refresh actuator endpoint in order to refresh.

After every update of our properties, we must call the next endpoint, in order to refresh

POST {host:port}/actuator/refresh, Example : POST http://localhost:8005/actuator/refresh

Be aware to use POST Http method.

# Rest Calls        

As you may notices when we are talking about microservices, we need to call several API services in order to achieve 
the expected results. For this reason is very likely that in some of those microservices we need to call other microservices.

## Rest Template

Rest template is a tool that let us call other REST service.

```java
when we want to change some properties in our config server, in order to avoid those restart we can use something that is called
Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8001/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class,
                uriVariables);

CurrencyConversion response = responseEntity.getBody();
```
                

In the previous example we had the next steps.

1. Create a new instance of RestTemplate.
2. Use the method getForEntity in order to map the response to a specific entity.
3. Set the URL or end point.
4. Set the entity in which you want to map the response
5. Set the variables for the call.
6. Create an object ResponseEntity<Entity> in where you are going to store the answer.
7. Return the entity with the method getBody.

## Feing

Feing is a tool very similar to restTemplate that let us call REST endpoints.

1. Add dependency in pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```

2. Enable Feign Client

```java
@EnableFeignClients("com.microservices.currencyconversionservice")
```

Where the "com.microservices.currencyconversionservice" is the package that we need to scan      

3. Create Interface with @FeignClient

```java
@FeignClient(name="currency-exchange-service", url="localhost:8001")
public interface CurrencyExchangeServiceProxy {

@GetMapping("/currency-exchange/from/{from}/to/{to}")
public CurrencyConversion retrieveExchangeValue(@RequestParam("from") String from, @RequestParam("to")String to);

}
```

In this interface you are going to put abstract methods in which you put the URL, parameters and also return type. One important thing to highlight is that you must use @RequesParam("parameter_name").

4. Use the proxy.

```java
@Autowired
CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

CurrencyConversion response = currencyExchangeServiceProxy.retrieveExchangeValue(from, to);
```

# Client Side Load Balancing

## Ribbon & Feign

Ribbon can help us to distribute the calls among different instances of the same service.

All the next changes need to be applied in the caller microservices(Client Side).

1. Dependency.

        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>

2. Put @RibbonClient(name="currency-exchange-service") in our Proxy Interface

```java
@FeignClient(name="currency-exchange-service")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

@GetMapping("/currency-exchange/from/{from}/to/{to}")
public CurrencyConversion retrieveExchangeValue(@RequestParam("from") String from, @RequestParam("to")String to);

}
```

* First we must add the anotation @RibbonClient in our Proxy Feign Class. 

* We must delete the url parameter in our @FeignClient anotation, as you can notice the Ribbon anotation is going 
to deal with the url of the service, because the main purpose of Ribbon is to distribute among several instances. 

3. Config Properties.

```properties
currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001
```

In our properties files we must to set up what are the posible host or instances in which the application or microservices is runnig.        

## Ribbon & Rest Template

1. Dependency.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```

2. Config rest template bean.

```java
@Configuration
public class AppConfig {

        @Bean("restClient")
        @LoadBalanced
        public RestTemplate registerRestTemplate() {
                return new RestTemplate();
        }
}
```

3. Call service

```java
private final RestTemplate restTemplate;

public ItemServiceImpl(RestTemplate restTemplate) {
this.restTemplate = restTemplate;
}

@Override
public List<Item> findAll() {
        List<Product> products = Arrays.asList(restTemplate.getForObject("http://products-service/products", Product[].class));
        return products.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());
}
```
                
4. Config Properties.

```properties
currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001
```

# Naming Server

In the previous configuration we setup a pool of two instances, but what happend if we create another instances. Is the application able to know about the new instance? 
 
Because the instances are harcoded the application is not going to be able to see the new instances and in order to solve
this we have two options:

1. Turn-down our application and add the new instance url in our properties.
2. Setup an Naming Server(Eureka) that can work with Ribbon an update the pool of connection automatically.


```properties
currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001,http://localhost:8002
```

The previous approach means that every single time that we create a new instance we must to change the properties file, 
nevertheless this is not the ideal situation, the best approach is that the client side detects the changes in the 
number of instances dinamically for this reason the Naming Server was create, and in this case we are going to use Eureka Naming Server.

## How it works?

Whenever an instance or a microservices comes up it will register itself in the Eureka Naming Server, this is call Service Registration and whenever when a service want to talk with another service it must talk with the naming server and ask about  what are the instances availables for the desire service and this is call service discovery.

## Eureka Naming Service

## Install Eureka Server

1. Create Component Naming Server

First you should go to https://start.spring.io/ and create a project with the next dependencies:

* Eureka Server
* Config Client (Optional -> Only if you want to store properties in the Config Server)
* Spring Boot Actuator (Optional)
* Spring Boot DevTools (Optional)

2. Mark as @EnableEurekaServer

In the main class of the project that we just created we must mark the class with the annotation @EnableEurekaServer.

3. Properties Files.

In order to launch a basic Eureka Server we need to set up some configuration in our properties file.

```properties
spring.application.name=netflix-eureka-naming-server
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

## Connect to Eureka Server

1. First in the microservices or project that we want to use the eureka services we must add the next dependency.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. Add in the microservices main class the @EnableDiscoveryClient anotation 

3. Finally configure the properties file in order to let the application know where is eureka.

```properties
eureka.client.service-url.default-zone=http//:localhost:8761/eureka
```

4. Eureka Server with Ribbon.

Did you remember what was the problem that Naming Server fixs? Do not hardcoded information in our properties files, 
so basically if we want to connect Eureka with Ribbon the only thing that we have to do is delete the hardcoded configuration 
in our properties files, that is because we already install eureka client and ribbon, so in background they understand
each other withouth the neeeded of configuration properties.

```properties
## Delete or comment the following line
currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001,http://localhost:8002
```

## Setup Port Dynamically

With Eureka and Ribbon configurations we don't need to harcoded the instances in our application properties,
eureka is always going to know what instances are up, but so far we are starting up the instances with harcoded ports, in
order to set those port dynamically we can follow the next steps:

1. In our application properties set de port properties as random

```properties
server.port=${PORT:0}
```

2. In the application properties set instnace id property as random too

```properties
eureka.instance.instance-id=${spring.application.name}:${eureka.instance.instance_id:${random.value}}
```

With these two properties every time that we start-up a new instances, these instances are going to have a new random 
available port and also are going to be registry in eureka.

# API-Gateway

An API Gateway is a server that is the single entry point into the system. It is similar to the Facade pattern from object‑oriented design. 

The API Gateway encapsulates the internal system architecture and provides an API that is tailored to each client.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/api-gateway.PNG) 

It might have other responsibilities such as :

1. Authentication.

2. Monitoring. 

3. Load balancing.
 
4. Caching.

## Zuul API Gateway 

Zuul Server is an API Gateway application. It handles all the requests and performs the dynamic routing of microservice 
applications. It works as a front door for all the requests

## Setup Zuul

1. First you should go to https://start.spring.io/ and create a project with the next dependencies:

* Zuul api-gateway server
* Eureka Discovery Cliente
* Spring Boot Actuator (Optional)
* Spring Boot DevTools (Optional)

2. Config the main class with @EnableZuulProxy and @EnableDiscoveryClient

```java
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class NetflixZuulApiGatewayServerApplication {

        public static void main(String[] args) {
                SpringApplication.run(NetflixZuulApiGatewayServerApplication.class, args);
        }

}
```

3. Config de properties file with require info

```properties
spring.application.name=netflix-zuul-api-gateway-server
server.port=8765
eureka.client.service-url.default-zone=http//:localhost:8761/eureka
```

## Setup Microservices Paths

Zuul is the entry point for all the request, for this reason we need to setup the routes of the inner microservices or API with the
url that zuul are going to expose.

Let's say that we have two microservices, products and items and with the following features:

1. Each microservices has its own controller.

2. Each microservices has the application name already setup.

Items -> application properties

```properties
spring.application.name=item-service
server.port=${PORT:0}
eureka.instance.instance-id=${spring.application.name}:${eureka.instance.instance_id:${random.value}}
eureka.client.service-url.default-zone=http//:localhost:8761/eureka
```

Products -> application properties

```properties
spring.application.name=products-service
server.port=${PORT:0}
eureka.instance.instance-id=${spring.application.name}:${eureka.instance.instance_id:${random.value}}
eureka.client.service-url.default-zone=http//:localhost:8761/eureka
```

With the previous configuration we are able to setup the microservices' paths in zuul.

Basically we just need to add the next configuration in our properties.

```properties
zuul.routes.products.service-id=products-service
zuul.routes.products.path=/api/products/**

zuul.routes.items.service-id=item-service
zuul.routes.items.path=/api/items/**
```

With the above code zuul is going to check in eureka what are the instances for the application with ids
"products-service" and "items-service" for those application Zuul is going to route all the request that cames from
"/api/products/**" and "/api/items/**" respectively.

For instance:

* GET Http Method, url: host/api/products/findAll. This request is going to search for an endpoint created in the products application 
un which the url is findAll


## Sending Request
 
At this point zuul should be working as expected, but there is something else that we have to hightlight, if we want to see the zuul filtering, we have to use a different URL for each microservices, let's see an example:

Original URL for two microservices:

* http://localhost:8000/currency-exchange/from/USD/to/INR
* http://localhost:8100/currency-converter-feign/from/USD/to/INR/quantity/200

If we execute the previous url zuul is not going to be executed.

In order to execute the zuul filtering whe must use a new kind of urls that are going to be build in base of our zuul server and our microservices.

Final Url Structure:

* {zuul-url}/{app-name}/{service-url}

Example

* zuul-url -> localhost:8765
* app-name -> currency-exchange-service
* service-url -> We could take this parameter from the Original url -> currency-exchange/from/USD/to/INR

Final Url

* http://localhost:8765/currency-exchange-service/currency-exchange/from/USD/to/INR

## Filter

When we are using Zuul one of the main features are the filters, in where the Api-gateweay can process the request in different
stages, there are three kinds of filter: Pre, Post and Route

In order to create a custom filter we need to keep in mind the next rules: 

1. The Filter class must extend the Abstract Class ZuulFilter.

2. The ZuulFilter Abstract class has four abstract methods, filterType, filterOrder, shouldFilter, run

3. filterType could be pre -> means to execute the filter just when the request come.

4. filterType could be post -> means to execute the filter just when the request finish.

5. filterType could be error -> means to execute the filter just when the request is an error.

6. filterOrder gives a priority among the different ZuulFilter implementations, an order in which all the filter are going to be executed.}

7. shouldFilter is a flag in which we set up if the filter is going to be applied.

8. run is the main method in which all the filtering is define.

### Pre

The "Pre" filter is going to be execute just before to route the request(Usually is used to check the request).

```java
@Component
public class ZuulLoggingFilter extends ZuulFilter {

        private Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public String filterType() {
                return "pre";
        }

        @Override
        public int filterOrder() {
                return 1;
        }

        @Override
        public boolean shouldFilter() {
                return true;
        }

        @Override
        public Object run() throws ZuulException {
                HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
                logger.info("request -> {} request uri -> {}", request, request.getRequestURI());
                return null;
        }
}
```

### Post

The "Post" filter is going to be execute just after to route the request(Usually is used to modified the response).

```java
@Component
public class ZuulLoggingFilter extends ZuulFilter {

        private Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public String filterType() {
                return "post";
        }

        @Override
        public int filterOrder() {
                return 1;
        }

        @Override
        public boolean shouldFilter() {
                return true;
        }

        @Override
        public Object run() throws ZuulException {
                HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
                logger.info("request -> {} request uri -> {}", request, request.getRequestURI());
                return null;
        }
}
```

### Route

The "Route" filter is going to be execute to route the request(Comunication with microservices).
      

# Distributed Tracing

As we saw in previous stages, calling a lot of microservices becomes complex because there are several call chain. Let's say there is a small defect, one service is not really working fine and we dont want to debug it, where can I find the problem?.

One important thing to keep in mind when we are working with a microservices architecture is to implemtent a distributed tracing system, basically is one place in where we can go and find out what is happening with a specific request.


## Spring Cloud Sleuth

If we want to achieve a Distributed Tracing System we neeed to assing a unique id to each request in the application, that is the main purposes of the Spring Cloud Sleuth.

1. Add the dependency in the microservices that are require (all of those microservices where is at least one call.

```xml
<dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

When you finish the previous step you can run all the application with the change and you are going to note that for all the new 
request of those microservices a new an unique request id will be create it with the purpose of trace each call.

## Zipkin Distributed Tracing

In this point another challenges arises, as we could see in the previous step we can create a unique id per request, but know the question is,
Where are we going to put the logs? Remember that we have a lot of microservices that means a lot of logs, one of the solution of this question
is something that is called centralized log in where all the microservices are going to write their information. 

In order to use zipkins, we need two steps. the first step is add the dependency in our pom.xml

### Install Zipkins

To install Zipkin server we need to follow the next steps:

1. Go to https://zipkin.io/pages/quickstart

2. Download the lastest release of the zipkin server (this is going to be a jar file)

3. Run Zipkins using the next command

```jshelllanguage
java -jar {zipkins.jar.file}
```

Where an example of the zipkins jar file could be zipkin-server-2.16.1-exec.jar

4. Open http://localhost:9411/zipkin/

With the previous step we are going to have our zipkins server running in the port 9411.

### Add Zipkins Dependency

In every project that we want to log information into our zipkin server we need to add the next dependency.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

### Setup Sample

In every project that we want to log information into our zipkin server you must add a sampler as a @Bean

```java
@Bean
public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
}
```

### Zipkins Tags

Sometimes we want to add extra information in our zipkins server, in order to do that we can use tags, with this tool
we can create custom message with the information that we want in the place that we require it.

In order to use the Tags we need to use the Tracer class from brave library and inject this bean wherever we want to put the tag.

Let's say that we have an autehntication service and we want to log in zipkins if the authentication was sucess or fail.    


```java
@org.springframework.stereotype.Service
public class AuthenticationService {
    
    @org.springframework.beans.factory.annotation.Autowired
    private brave.Tracer tracer;
    
    public boolean userCanAccess(User user) {
        if (user.haveAccess()) {
            tracer.currentSpan().tag("Authentication Sucess", "the user has access  to the appliaction");
        } else {
            tracer.currentSpan().tag("Authentication Fails", "the user has not access to the appliaction");
        }
    }
}
```

### Zipkins and RabbitMQ

Connect microserves to rabbitmq with the zipkin config.

1. the first step is install and run RabbitMq (We are not going to explain the installion here.)

2. Add the dependency in our microservices

```xml
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>
```

3. Start zipkin server with rabbit setup

Now you have to install the zipkin server but you have to set the rabbit_addresses property in order to let zipkins know
where is going to be listening our broker.

```cmd
@echo off
set RABBIT_ADDRESSES=localhost:5672
java -jar zipkin-server-2.16.1-exec.jar
```

# Fault Tolerance

If something among our microservices fails, with fault tolerance we can setup default responses in order to don't break
the entire system.

For instance we can deal with the next scenarios:

1. Systems or microservices fails.
2. Latency problems
3. Timeouts problems

## Hystrix

With hystrix we can implement the fault tolarence in spring cloud, in order to setup Hystrix we can follow the next steps:

1. Add dependency

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

2. Enable Hystrix

Mark the main class with the next annotation

```java
@EnableHystrix
```

3. Set up controllers

Examples:

### Exceptions

If in any microservices there is an expcetion we can handle that event with Hystrix.

```java

// Setup the Hystrix Command 
@GetMapping("/fault-tolerance-example")
@HystrixCommand(fallbackMethod = "fallbackFaultToleranceExample")
public CurrencyConversion faultToleranceExample() {
        throw new RuntimeException("Not Available");
}

// Create the fallback method when something happen.                
public CurrencyConversion fallbackFaultToleranceExample() {
        return new CurrencyConversion(1l, "USD", "IDR", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1 );
}
```

### Timeouts

If one endpoint is taking a lot of time, we can handling a timeout with hystrix.

By default the timeout time is set around one second, nevertheless we can modified that value.

```properties
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=9000
ribbon.ConnectTimeout=2000
ribbon.ReadTimeout=5000
```

One thing to keep in mind is that Hystrix is like a wrappe for Ribbon, for this reason the timeout for Hystrix should 
be equals or greater than the sum of the ribbon properties (ConnectTimeout and ReadTimeout))

```java
@GetMapping("/items/timeOut")
@HystrixCommand(fallbackMethod = "timeOutMethod")
public Item findByIdTimeout() throws Exception {
    Thread.sleep(100000);
    return null;
}

public Item timeOutMethod() {
    Item item = new Item();
    item.setAmount(1);
    Product product = new Product();
    product.setId(100L);
    product.setName("Default product - Time out");
    item.setProduct(product);
    return item;
}
```

With the above code, every time that a timeout happens the timeOutMethod is going to be called.

