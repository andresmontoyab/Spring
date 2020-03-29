package com.learnig.database.databasedemo;

import com.learnig.database.databasedemo.jdbc.entity.Person;
import com.learnig.database.databasedemo.jdbc.PersonJdbcDao;
import com.learnig.database.databasedemo.jpa.entity.Student;
import com.learnig.database.databasedemo.jpa.repositories.StudentRepository;
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

	@Autowired
	StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(DatabaseDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("*********************************------------******************");
		logger.info("THE JDBC EXAMPLES ARE GOING TO BEGGING");
		logger.info("All Users -> {}", personJdbcDao.findAll());
		logger.info("User with id 10001 {}", personJdbcDao.findById(10001));
		logger.info("Deleting user with id 10001 {}", personJdbcDao.deleteById(10001));
		logger.info("Creating User 10010, amount of row inserted {} ", personJdbcDao.insert(new Person(10010, "Juan", "Buenos Aires", new Date())));
		logger.info("Updating User 10010, amount of rows updated {} ", personJdbcDao.update(new Person(10010, "Pedro", "Buenos", new Date())));
		logger.info("THE JDBC EXAMPLES FINISHED");
		logger.info("*********************************------------******************");


		logger.info("*********************************------------******************");
		logger.info("THE JPA EXAMPLES ARE GOING TO BEGGING");
		logger.info("FindById Student by Id 10001 {}", studentRepository.findById(10001));
		logger.info("Create Student with id 10020 {}", studentRepository.createStudent(new Student(10020, "Pedro", "Buenos", new Date())));
		logger.info("Create Student with id 10020 {}", studentRepository.createStudent(new Student(10021, "Her", "Buenos", new Date())));
		logger.info("Deleting user 1 ");
		studentRepository.deleteById(1);
		logger.info("Retrieve all students {}", studentRepository.findAll());
		logger.info("THE JPA EXAMPLES FINISHED");
		logger.info("*********************************------------******************");

	}
}
