# Security
     
* [Security](#Security)
    * [Authentication](#Authentication)
    * [Authorization](#Authorization)
    * [JWT](#JWT)
        * [Headers](#Headers)
        * [Payload](#Payload)
        * [Signature](#Signature)
    * [OAuth 2.0](#OAuth-2.0)    
        * [Roles](#Roles)    
        * [Protocol Endpoints](#Protocol-Endpoints)    
        * [Scopes](#Scopes)    
        * [Grant Types](#Grant-Types)    
            * [Authorization Code](#Authorization-Code)
            * [Authorization Code with PKCE](#Authorization-Code-with-PKCE)
            * [Client Credentials](#Client-Credentials)
            * [Implicit](#Implicit)
            * [Password](#Password)
            * [Refresh](#Refresh)    
    * [Example](#Example)    
		* [Creating API](#Creating-API)    
		* [Create User Application](#Create-User-Application)    
		* [Create OAuthServer](#Create-OAuthServer)    
			* [Setup Feign Client](#Setup-Feign-Client)    
			* [Define Authentication Type](#Define-Authentication-Type)    
			* [Create Authentication Service](#Create-Authentication-Service)    
			* [Authorization Server Config](#Authorization-Server-Config)    
		* [Testing OAuthServer](#Testing-OAuthServer)
		* [Protecting API](#Protecting-API)   
		    * [Add OAuth2.0 Dependency](#Add-OAuth2.0-Dependency)   
		    * [ResourceServer Class](#ResourceServer-Class)   
		        * [ResourceServerSecurityConfigurer](#ResourceServerSecurityConfigurer)   
		        * [HttpSecurity](#HttpSecurity)   
		        * [HttpSecurity with CORS](#HttpSecurity-with-CORS)
        * [Testing Protected API](#Testing-Protected-API)
            * [Unprotected resources](#Unprotected-resources)
            * [Protected resources](#Protected-resources)
		  

# Security

When we are creating applications one important question that arises is How are we going to protect our application?,
How are we going to ensure that only the allow users can access to the apps?

In this section we are going to talk about Authorization, Authentication and some tools as JWT or Oauth2.0 that let us secure our
application in a proper way.

## Authentication

When we are talking about authentication basically we are answering the question, Who are you? In every single application
the users need an identity in order to do specific tasks, for this reason the authentication is a key concept in terms of security.

## Authorization

After authentication there is the authorization and this one is about, What can I do in the application?, As many of you may know
in several application there are different roles like admin, user so forth, and these roles are allowed to execute very specific tasks.

So authorization point to What action or which resource I am allowed to execute/use

## JWT

JSON Web Token (JWT) is an open standard that defines a compact and self-contained way for securely 
transmitting information between parties as a JSON object

The JWT Structure is composed by :

* Header
* Payload
* Signature

Therefore, a JWT typically looks like the following.

xxxxx.yyyyy.zzzzz

### Headers

The header typically consists of two parts: the type of the token, which is JWT, and the signing algorithm being used, such as HMAC SHA256 or RSA.

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### Payload

The second part of the token is the payload, which contains the claims. Claims are statements about an entity (typically, the user) and additional data.

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```

### Signature

To create the signature part you have to take the encoded header, the encoded payload, a secret, the algorithm specified in the header, and sign that.

```json
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```

The signature is used to verify the message wasn't changed along the way

![](https://github.com/andresmontoyab/Spring/blob/master/resources/jwt-encoded.PNG)


## OAuth 2.0

Is an Autohrization framework specifically built for HTTP APis, It allows to user to securely delegate scopes API access to
applications. 

### Roles

In order to sucesfully implement the OAuth 2.0 framework we have to take into account the kind of roles. 

1. Protected Resources: Basically is the API

2. Client: The application that want to access to the protected resources.

3. Resource Owner: Is the user that protected resources.

4. Authorization Server: This server is responsible for handling authorization request.

### Protocol Endpoints

1. Authorization Endpoint : Handle all user interaction via the user agent, typically the browser

2. Token Endpoint: Is meant for machines only, interacting away from the browser via a secure API call.

### Scopes

A permission to do something within a protected resource on behalf of the resource owner.

For instance we can define the next scopes in an application

1. READ_ONLY: Means that the client only can read information

2. ADMIN: Means that the user can create, update, delete or read information.

### Grant Types

### Authorization Code

### Authorization Code with PKCE

### Client Credentials

### Implicit

### Password

### Refresh 

## Example

Now with this information in mind, we are going to apply the concepts that we saw in the previous sections:

![](https://github.com/andresmontoyab/Spring/blob/master/resources/basic-security-spring.png)

As we see in the above image we are going to make a real example using Spring framework with OAuth 2.0

1. Client: Is the application who want to use our API(In this case could be a front-end or event postmant)
2. OAuth Server: Is our authorization server that is going to be created with spring-cloud-security and OAuth 2.0
3. API: In this example our API or protected resource is the item-microservices.
4. user-microservices is an application that is going to deal with user and roles.

Let's Start.

## Creating API

In this point we are going to create a very simple API, in this API we are going to deal with the "Item" resource, so let's
create it.!

Go to https://start.spring.io/ and add the next dependencies:

1. Spring Web.

2. H2 Database

3. Spring Data JPA

4. Rest Repositories

And Download the application!

Let's start with the configuration in our new application.

### Create Domain

In our application, we have to create a new package call it "domain" (Or as you wish) and inside of this folder we are
going to create the entity Item(our protected resource.)

Should look like:

```java
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String description;

    private Integer amountAvailable;
    
    // setter, getter and constructors
    
}
``` 

### Create Repository and Controller

In our application, we have to create a new package call it "domain" (Or as you wish) and inside of this folder we are
going to create the repositoy/controller using our previous domian entity.

```java
@RepositoryRestResource(path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {
}
``` 

And that's all that we need, at this point we are able to run our application!.

Nevertheless if we create a GET request pointing to the endpoint is going to work, in other words the application
is not safe.

## Create User Application

This application is going to deal with the user information an their roles, this is require in order to know which kind
of permission every user has.

Go to https://start.spring.io/ and add the next dependencies:

1. Spring Web.

2. H2 Database

3. Spring Data JPA

4. Rest Repositories

And Download the application!

Let's start with the configuration in our new application.

### Create Domain

As you remember this application is going to deal with user and their roles, for this reason we have two entities, the first
one is User and the second one is role, let's go to create those entities.

```java

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String username;

    @Column(length = 60)
    private String password;

    private Boolean enabled;

    private String name;

    private String lastname;

    @Column(unique = true, length = 50)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles;
    
    // setter, getter and constructors
    
}


@Entity
@Table(name = "ROLES")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 30)
    private String name;

}
``` 

At this point we already created the entities for our user-microservices, now let's insert some default data using the import.sql
file in our resources folder.

```sql
INSERT INTO USERS (USERNAME,PASSWORD,ENABLED,NAME,LASTNAME,EMAIL) VALUES ('Admin', '$2a$10$7cCHdorngQIAX3QvnV95zO.1vsb0bN9/Cxv7dnAdBeUMAGhqtL86u', 1, 'Andres', 'Montoya', 'andres@correo.com');
INSERT INTO USERS (USERNAME,PASSWORD,ENABLED,NAME,LASTNAME,EMAIL) VALUES ('Andres', '$2a$10$7cCHdorngQIAX3QvnV95zO.1vsb0bN9/Cxv7dnAdBeUMAGhqtL86u', 1, 'Felipe', 'Montoya', 'felipe@correo.com');

INSERT INTO ROLES (NAME) VALUES ('ROLE_USER');
INSERT INTO ROLES (NAME) VALUES ('ROLE_ADMIN');

INSERT INTO USERS_ROLES  (USER_ID, ROLES_ID) VALUES (1,1);
INSERT INTO USERS_ROLES  (USER_ID, ROLES_ID) VALUES (1,2);
INSERT INTO USERS_ROLES  (USER_ID, ROLES_ID) VALUES (2,1);
```

As you can see with the above queries we have two Users, Admin and Andres.

1. Admin has the both roles, user and admin
2. Andres just has the user role

### Crete Repository and Controller

The next step is setup our repository layer and also expose our endpoints in order to use them in the OAuth-server application.

```java
@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @RestResource(path = "searchByUsername")
    public User findByUsername(@Param("username") String username);

    @RestResource(path = "findEnabledUser")
    public List<User> findByEnabled(@Param("enabled") Boolean enabled);
}
```

Check that we create two custom queries in order to two retrieve our user by the username and also the Users availables.

If you want to make a call to one of the custom methods you could do it in the next way:

GET {server}:{port}/users/search/searchByUsername?username=Andres

Example:

http://localhost:8283/users/search/searchByUsername?username=Andres

## Create OAuthServer.

This is the most important, because in this project we are going to setup or Authorization Server.

Go to https://start.spring.io/ and add the next dependencies:

1. Spring web

2. Cloud OAuth2

3. Open Feingn

If you are using Maven please be sure that the next dependencies are in our pom.xml, and if you're using other project 
management tool please verify that similar dependencies have been added

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### Setup Feign Client

The first step is retrieve the users and roles from the user-microservices, in order to do that we need to enable
the Feign client in the spring boot main class adding @EnableFeignClients

```java
@SpringBootApplication
@EnableFeignClients
public class OauthMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OauthMicroserviceApplication.class, args);
	}
}
```

When that annotation is ready, we need to setup our Feign interface

```java
@FeignClient("user-service")
public interface UserFeignClient {

    @GetMapping("/users/search/searchByUsername")
    User findByUsernmae(@RequestParam("username") String username);
}
```

In this point is very likely that a problem appears, the entity User is not created in the OAuth Server Project, right?
But we need that dependency in our application, in order to fix that you must follow the next steps:

1. Go to your user-microservices application
2. Run mvn clean install (The purpose is generate the Jar with the dependencies)
3. Go to your user-microservices pom.xml file and identify the groupId, artifactId and version
4. Go to your oauth-microservices pom.xml file and add a new dependency with the groupId, artifactId and version of the user-microservices

With these steps the problems should be fixed.

### Define Authentication Type

Spring cloud offers several options for configuring user stores as:

1. In memory-user store
2. A jdbc-based user store
3. A Ldap-backed user store
4. A custom user details service.

No matter which user store you choose, you can configure it by overriding a configure() method defined in the WebSecurityConfigurerAdapter
configuration base class.

For our current implementation we are going to use the User Details Service approach.
 
In order to setup our kind of authentication we need to create a class that extends from WebSecurityConfigurerAdapter, 
that class should look in the next way:

```java
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService; // Pending for the next step

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(this.userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
```

These are the important things to highlight in the above code:

1. We marked this class as @Configuration
2. We extends WebSecurityConfigurerAdapter 
3. We override the method configure() and set the authentication way as userDetailsService()
4. We created a bean with BCryptPasswordEncoder type (This is going to be used to encrypt the password)
5. We set the passwordEncoder with the bean created in the step 4.

### Create Authentication Service

The authentication service is created with the task of let the OAuth server know who is requesting a JWT.

Because we are getting this information from another microservice, we need to use the UserDetailsService interface from 
spring cloud, if we want to use this interface we need to implement the method:

```java
UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
```

So we are going to create a new class call it "UserDetails" that implements UserDetailsService from spring security.

```java
@Service
public class UserDetails implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userFeignClient.findByUsernmae(username);
        if(user == null) {
            throw new UsernameNotFoundException("Error in login, the username does not exist in the system");
        }
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .peek(authority-> log.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
```

There a lot of thing happening in the above code:

1. The class implements the loadUserByUsername method from UserDetailsService
2. The class inject the feign client that we already created.
3. The first step is to retrieve the User information using the username and the feign client.
4. When we retrieve the User roles info, we have to map those roles to GrantedAuthority.
5. When we retrieve the User info, we have to map that user to the User from Spring cloud.

So in this step we basically are setting up our OAuth server in order to know who is requesting the JWT.

### Authorization Server Config

The last step is to finally setup our AuthorizationServer config, in this step is where the real "magic" happends.

So we need to create a need class that extends from AuthorizationServerConfigurerAdapter

```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    
    private static final String password = "12345";

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("angular_front_end_app")
                .secret(passwordEncoder.encode(password))
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter());
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("some_secret_code");
        return tokenConverter;
    }
}
```

As you can see the above class is kinda complex, so we are going to split it in order to explain it as much as we can.

As initial thought please take into account that this class we need to marked with @Configuration and @EnableAuthorizationServer.  

The second very important thing is that we need to override three methods from the AuthorizationServerConfigurerAdapter class.

These are the three methods to override:

```java
public class AuthorizationServerConfigurerAdapter implements AuthorizationServerConfigurer {
    public AuthorizationServerConfigurerAdapter() {
    }

    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    }

    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    }
}
```

### AuthorizationServerSecurityConfigurer

In this method we are going to declare to trust in all the requests that call the /oauth/token endpoint with the
"permitAll()" flag.


```java
 @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }
```

### AuthorizationServerEndpointsConfigurer

In this method, we are going to setup the authentication manager(please keep in mid that the variables authenticationManager and userDetailsService were
injected in this class) and also we are going to configure everything related with the token, as you can see we are returning
a JWT with a signingKey as "some_secret_code"

```java
 @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter());
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("some_secret_code");
        return tokenConverter;
    }
```

### ClientDetailsServiceConfigurer

This last method is the most important, because we are going to define here, which client are going to be able
to use the authorization, the scopes, the grantTypes and also the duration of the token.

```java
  @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("angular_front_end_app")
                .secret(passwordEncoder.encode(password))
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600);
    }
```

When this setup is ready, we are going to be able to test our OAuthServer!.

## Testing OAuthServer

In order to test that our OAuth server is working as expected we need to follow the next steps:

1. We are going to test using postmand, so please download it
2. Create a new request pointing to the {server}:{port}/oauth/token
3. Set up the Authorization as Basic Auth and put the client and password.
4. Setup the body as x-www-form-urlencoded and add the username, password and grantype(this one as password).
5. Send the request :)


### Authorization setup example

![](https://github.com/andresmontoyab/Spring/blob/master/resources/postman-security-authorization.png)

### Body Example

![](https://github.com/andresmontoyab/Spring/blob/master/resources/postman-security-body.png)

### Results

When the previous configuration is done, we just click in "Send"

![](https://github.com/andresmontoyab/Spring/blob/master/resources/postman-security-jwt.PNG)

And as you can see, there is our JWT!.

## Protecting API

After we setup our Authorization Server is not to configure our resource server, or the API.

### Add OAuth2.0 Dependency

First thing that we want to do is add the dependency in our API

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
```

### ResourceServer Class

After add the dependency we are going to setup a resource server configuration class in where we are going to setup all the
configuration for the OAuth2.0 implementation in the Protected Resources.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //
    }
}
```

As you can see in the above code, if we want to create our resource server configuration class we need to follow the next steps:

1. Mark the class as @Configuration
2. Mark the class as @EnableResourceServer
3. Extends the class ResourceServerConfigurerAdapter
4. Implement the both configure methods.

### ResourceServerSecurityConfigurer

This is the first method that we must implement in order to setup up our resource server and is the easy one, basically
in this method we are going to setup the logic to decoded our incoming JWT to json and verify if is a valid JWT.

In order to do that we have to use the same jwtKey, JwtTokenStore and JwtAccessTokenConverter that we used in 
our Authorization Server, so if you compare this information in both (Authorization and Protected API) project must do
exactly the same. 

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;
    

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // not yet
    }
    
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(jwtKey); // Same key Authorization server
        return tokenConverter;
    }
}
```

### HttpSecurity

In this method we are going to setup which endpoints  are we going to protect and also which roles are required in order to use
the protected apis.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            // Already explained
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
         http.authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/security/oauth/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/products", "/api/items", "/api/users/users" ).permitAll() 
                        .antMatchers(HttpMethod.GET, "/api/users/users/{id}", "/api/products/{id}",
                                "/api/items/{id}/amount/{amount}").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.POST, "/api/products/create", "/api/items/create", "/api/users/users").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PUT, "/api/products/update/{id}", "/api/items/update/{id}", "/api/users/users/{id}").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE , "/api/products/delete/{id}", "/api/items/delete/{id}", "/api/users/users/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated();
    }
}
```

So as we can see in the previous code there are some open api and also some protected resources.

1. The antMatchers is a rule that is going to be applied to the api that match with HttpMethod and also the url
2. As you can see the /api/security/oauth/** is open to everyone, because in this api we are going to ask for JWT.
3. There is a method call hasAnyRole in where we can say that this api is going to be open for multiples roles
4. There is a method call hasRole in where the api is going to be available only for one role.
5. Finally the ".anyRequest().authenticated()" means that if there is another request that doesn't match with the previous rules
this request must be authenticated. 

### HttpSecurity with CORS

One last thing to take into account is the CORS configuration, this is require when we want to call our resource server from a 
client that is outside of our domain, in order to setup CORS we can follow the next steps.

```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            // Already explained
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
         http.authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/security/oauth/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/products", "/api/items", "/api/users/users" ).permitAll() 
                        .anyRequest().authenticated()
                        .and().cors().configurationSource(corsConfigurationSource());;
    }
    
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Arrays.asList("*")); // Let all the origin
            corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", corsConfiguration);
            return source;
        }
    
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
            FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
            bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return bean;
        }
}
```

As you can see in the above code we need to create some beans in order to configure our CORS:

1. Add the configurationSource() method in our configure method.
2. Create a CorsConfigurationSource bean in where we specified which domains, http methods and headers are allowed.
3. Create a FilterRegistrationBean<CorsFilter> in order to apply the corst configuration globally.

## Testing Protected API

This is the final part of this section ans basically we are going to test that our security implementation is working as expected.

### Unprotected resources

As you can remember there are some endpoint that are available to all the public, example :  "/api/users/users"

![](https://github.com/andresmontoyab/Spring/blob/master/resources/unprotected-resource.png)

In the above images we can notice the next points:

1. We are not using any kind of authorization.
2. The response from the server is 200.

### Protected resources

This is the most important part, there are protected end points like /api/users/users/{id} in where we need a JWT with the correct
roles in order to call it. 

Let's first try to call this endpoint without any kind of authorization.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/protected-resource-without-jwt.png)

As you can see in the previous image, in this case we are not able to get into the resource because is protected and we are 
not using any kind of authorization.

Now, let's try to call the same endpoint but using one admin JWT.

The first step is retrieve the JWT from our OAuth Server, in order to create please go to 
the section [Testing OAuthServer](#Testing-OAuthServer)

When we have ready our JWT token, we are going to add an authorization in our request.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/protected-resource-with-jwt.png)

Finally as you can see above we are able to get into our protected server using our JWT, in order to achieved that please follow
the next steps.

1. Generate the JWT with the correct roles.
2. Setup the authorization as Bearer Token
3. Put your token in the "token" section
4. Send the request