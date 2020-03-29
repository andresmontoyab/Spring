# Spring Cloud
     
* [Spring Cloud](#Spring-Cloud)
    * [Microservice](#Microservice)
    * [Creating Microservices](#Creating-Microservices)
        * [Spring Cloud Config Server](#Spring-Cloud-Config-Server)
            * [Create Config Server](#Create-Config-Server)
            * [Git and Config Server](#Git-and-Config-Server)
            * [Overrinding properties](#Overrinding-properties)
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
        * [Distributed Tracing](#Distributed-Tracing)
            * [Spring Cloud Sleuth](#Spring-Cloud-Sleuth)

    
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

Spring Cloud Config server is a tool that let us save all the properties for the microservices, with this server we can also have the properties for each environment like dev,qa,stage and prod.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/spring-cloud-config-server-diagram.JPG)

## Create Config Server

In https://start.spring.io/ create a project with the next dependencies

Dependencies:

1. Config Server

## Git and Config Server

1. Create folder 

        mkdir git-config-info

2. Init git

        git init

3. Create a Linked Source

A linked source is a module in our project that is pointing to a specific folder

https://stackoverflow.com/questions/23058448/linked-files-and-folder-in-intellij

4. Properties file.

After all of this we must create the properties file which are going to have the informacion for the specific microservices, in this example is called limits-service:

        limits-service.minimum=8
        limits-service.maximum=8888

5. Enable config server 

```java
@EnableConfigServer
@SpringBootApplication
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }

}
```

6. Run the application

At this point our configuration server should be working and returning the informacion in the url http://localhost:8888/limits-service/default

## Overrinding properties

As we said previously spring config server can help us saving properties for different environment, to achieve this we need to create more files with the following structure.


        limits-service.properties -> default info, url -> http://localhost:8888/limits-service/default
        limits-service-dev.properties -> dev info, url -> http://localhost:8888/limits-service/dev
        limits-service-qa.properties -> qa info, url -> http://localhost:8888/limits-service/qa

so forth.

## Connecting Microservices with Config Server

1. Chaging the name of application.properties by bootstrap.properties

2. We need to start up our config server in the port 8888 or whatever you want and also put the next line in the bootstrap.properties of the microservices

        spring.application.name=limits-service
        spring.cloud.config.uri=http://localhost:8888

Where spring.application.name must match with the name of the properties files in the config server and the uri must match with the url of the config server.      

## Configuring profiles

In the bootstrap.properties of the microservies

        spring.profiles.active=dev

# Rest Calls        

As you may notices when we are talking about microservices, we need to call several in services in order to achieve the expected result, for this reason is very likely that in some of those microservices we need to call other microservices.

## Rest Template

Rest template is a tool that let us call other REST service.
```java
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

## FEING

1. Add dependency in pom

        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-feign</artifactId>
		</dependency>

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

First we must add the anotation @RibbonClient in our Proxy Feign Class, and also we must delete the url parameter in our @FeignClient anotation, as you can notice the Ribbon anotation is going to deal with the url of the service, because the main purpose of Ribbon is to distribute among several instances.

3. Config Properties.

                currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001

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

In the previous configuration we add a pool of two host, but what happend if we create another instances in http://localhost:8002, Is our client side able to see this new instance?The answer is No, if we want that our client side see the new instance we must to change the properties file and added the new instance.

```properties
currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001,http://localhost:8002
```

The previous approach means that every single time that we create a new instance we must to change the properties file, nevertheless this is not the ideal situation, the best approach is that the client side detects the changes in the number of instances dinamically for this reason the Naming Server was create, and in this case we are going to use Eureka Naming Server.

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

Did you remember what was the problem that Naming Server fixs? Do not hardcoded information in our properties files, so basically if we want to connect Eureka with Ribbon the only thing that we have to do is delete the hardcoded configuration in our properties files, that is because we already install eureka client and ribbon, so in background they understand each other withouth the neeeded of configuration properties.

```properties
## Delete or comment the following line
currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001,http://localhost:8002
```

# API-Gateway

Are different function that are going to be applied in the middle of the communication of each microservices as:

1. Authentication, authorization
2. Tracing.
3. Fault Tolerance.

Often this function need to be intercepted to be proceced!. 

## Zuul API Gateway 

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

4. Create FilterClass

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
                
In the previous code we can notice the next things.

* The Filter class must extend the Abstract Class ZuulFilter.
* The ZuulFilter Abstract class has four abstract methods, filterType, filterOrder, shouldFilter, run
* filterType could be pre -> means to execute the filter just when the request come.
* filterType could be post -> means to execute the filter just when the request finish.
* filterType could be error -> means to execute the filter just when the request is an error.
* filterOrder gives a priority among the different ZuulFilter implementations, an order in which all the filter are going to be executed.}
* shouldFilter is a flag in which we set up if the filter is going to be applied.
* run is the main method in which all the filtering is define.

5. Executing Request with Zuul

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

# Distributed Tracing

As we saw in previous stages, calling a lot of microservices becomes complex because there are several call chain. Let's say there is a small defect, one service is not really working fine and we dont want to debug it, where can I find the problem?.

One important thing to keep in mind when we are working with a microservices architecture is to implemtent a distributed tracing system, basically is one place in where i can go and find what is happening with a specific request.


## Spring Cloud Sleuth

If we want to achieve a Distributed Tracing System we neeed to assing a unique id to each request in the application, that is the main purposes of the Spring Cloud Sleuth.

1. Add the dependency in the microservices that are require (all of those microservices where is at least one call.

```xml
<dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

2. Create a Sampler 

In our main class of the same microservices in where you put the previous dependency we need to create the next bean.

```java
@Bean
public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
}
```

When you finish the previous two steps you can run all the application with the change and you are going to note that for all the new request of those microservices a new an unique request id will be create it with the purpose of trace each call.

## Zipkin Distributed Tracing

In this point another challenges arises, as we could see in the previous step we can create a unique id per request, but know the question is, Where are we going to put the logs? Remember that we have a lot of microservices that means a lot of logs, one of the solution of this question is something that is called centralized log in where all the microservices are going to write their information. 

## RabbitMQ

Connect microserves to rabbitmq with the zipkin config.

First we need to add the dependency of sleuth-zipkin, this is require to log the information in the format that zipkin is expecting

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

Also is required the dependency of RabbitMQ in order to send the information to the zipkin server.

```
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>
```

To install Zipkin server we need to follow the next steps:

1. Go to https://zipkin.io/pages/quickstart

2. Download the lastest release of the zipkin server

3. In the folder that you put your jar use the next command in the CLI

                1. SET RABBIT_URI=amqp://localhost
                2. java -jar zipkin-server-2.16.1-exec.jar

3. Open http://localhost:9411/zipkin/


# Faul Tolerance

## Hystrix

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

Set up the controller that depend of other services.

```java
@GetMapping("/fault-tolerance-example")
        @HystrixCommand(fallbackMethod = "fallbackFaultToleranceExample")
        public CurrencyConversion faultToleranceExample() {
                throw new RuntimeException("Not Available");
        }
```

4. Create the fallback method when something happen.

```java
public CurrencyConversion fallbackFaultToleranceExample() {
        return new CurrencyConversion(1l, "USD", "IDR", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, 1 );
}
```