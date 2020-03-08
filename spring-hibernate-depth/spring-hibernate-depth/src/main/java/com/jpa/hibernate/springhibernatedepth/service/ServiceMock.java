package com.jpa.hibernate.springhibernatedepth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceMock {


    private final RestTemplate restTemplate;

    public ServiceMock(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRootResource() {
        //String result = restTemplate.getForObject("http://localhost:8090", String.class);
        ResponseEntity<String> result = restTemplate.getForEntity("https://jsonplaceholder.typicode.com/todos/1", String.class);
        ResponseEntity<String> resultTwo = restTemplate.getForEntity("https://jsonplaceholder.typicode.com/todos/2", String.class);
        ResponseEntity<String> resultThree = restTemplate.getForEntity("https://jsonplaceholder.typicode.com/comments", String.class);
        System.out.println("getRootResource: " + result);

        return result.toString();
    }

    public String addComment(String comment) {
        String result = null;
        try {
            result = restTemplate.postForObject("http://localhost/add-comment", comment, String.class);
            System.out.println("addComment: " + result);
        } catch (HttpClientErrorException e) {
            result = e.getMessage();
        }

        return result;
    }

    @Configuration
    static class ConfigTemplate{
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
