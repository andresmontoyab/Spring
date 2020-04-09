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
        * [Protocol Endpoints](#Protocol Endpoints)    
        * [Scopes](#Scopes)    
        * [Grant Types](#Grant-Types)    
            * [Authorization Code](#Authorization-Code)
            * [Authorization Code with PKCE](#Authorization-Code-with-PKCE)
            * [Client Credentials](#Client-Credentials)
            * [Implicit](#Implicit)
            * [Password](#Password)
            * [Refresh](#Refresh)    
    * [Spring Cloud Security](#Spring-Cloud-Security)

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

## Spring Cloud Security

Spring Cloud Security offers a set of primitives for building secure applications and services with minimum fuss.

