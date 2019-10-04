package com.learnig.database.databasedemo;

import com.learnig.database.databasedemo.entity.Person;
import com.learnig.database.databasedemo.jdbc.PersonJdbcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class DatabaseDemoApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PersonJdbcDao personJdbcDao;

	public static void main(String[] args) {
		SpringApplication.run(DatabaseDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("All Users -> {}", personJdbcDao.findAll());
		logger.info("User with id 10001 {}", personJdbcDao.findById(10001));
		logger.info("Deleting user with id 10001 {}", personJdbcDao.deleteById(10001));
		logger.info("Creating User 10010, amount of row inserted {} ", personJdbcDao.insert(new Person(10010, "Juan", "Buenos Aires", new Date())));
		logger.info("Updating User 10010, amount of rows updated {} ", personJdbcDao.update(new Person(10010, "Pedro", "Buenos", new Date())));
	}
}
