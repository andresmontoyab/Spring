# Spring Documentation

* [Spring Basic](#Spring-Basic)
* [Spring and Databases](#Spring-and-Databases)
    * [Setup Project](#Setup-database-project)
        * [data.sql](#data.sql)
    * [Spring JDBC](#Spring-JDBC)
        * [Spring JDBC vs JDBC](#Spring-JDBC-vs-JDBC)
        * [Jdbc Template](#Jdbc-Template)
            * [Creating Dao Jdbc Template](#Creating-Dao-Jdbc-Template)
            * [FindAll Jdbc](#FindAll-Jdbc)
            * [FindById Jdbc](#FindById-Jdbc)
            * [DeleteById Jdbc](#DeleteById-Jdbc)
            * [Insert Jdbc](#Insert-Jdbc)
            * [Update Jdbc](#Update-Jdbc)
    * [Spring JPA](#Spring-JPA)
        * [ORM](#ORM)
        * [Entities](#Entities)
        * [Entities Manager](#Entities-Manager)
            * [Persist](#persist)
            * [Merge](#merge)
            * [Remove](#remove)
            * [Flush](#flush)
            * [Detach](#detach)
            * [Clear](#clear)
            * [Refrest](#refresh)
        * [Repository](#Repository)
        * [JPQL](#JPQL)
            * [Named Queries](#Named-Queries)
        * [Criteria Query](#Criteria-Query)
        * [Relationships](#Relationships)
            * [One to One ](#One-To-One)
            * [One to Many ](#One-To-Many)
            * [Many to Many](#Many-To-Many)
            * [Fetching](#Fetching)
                * [Lazy](#Lazy)
                * [Eager](#Eager)
                * [Fetch By RelationShip](#Fetch-By-RelationShip)
        * [JPA Inheritance](#JPA-Inheritance)
            * [Single Table](#Single-Table)
            * [Table per class](#Table-per-class)
            * [Joined](#Joined )
            * [MappedSuperClass](#MappedSuperClass)
        * [Transaction Management](#Transaction-Management)
            * [Isolation Problems](#Isolation-Problems)
            * [Isolation Levels](#Isolation-Levels)
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


# Spring Basic

# Spring-and-Databases

## Setup database project

First you should go to https://start.spring.io/ and create a project with the next dependencies:

* Spring JDBC
* Spring JPA
* H2
* Web

When we finish the project setup we must download the project clicking in generate, and after that we should import our project in InteliJ

Also is require the next configuration

```java
spring.h2.console.enabled=true
server.port = 9090
```

And with the next url we are able to open the h2 console.

```java
http://localhost:9090/h2-console
```

Also there are another few config that show us useful information about.

```java
# Turn statistics on
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=debug

# Show all queries
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.type=trace
```

## data.sql

This is a file in which we can store sql information to load the base queries in the h2 memory database, this file should be at the same level of the properties file(and Autoconfiguration is going to detect it).

example : data.sql

```sql
create table PERSON
(
ID INTEGER not null,
NAME VARCHAR(255) not null,
LOCATION VARCHAR(255),
BIRTH_DATE timestamp,
primary key(id)
);

INSERT INTO PERSON (ID, NAME, LOCATION, BIRTH_DATE) VALUES (10001, 'Andres', 'Medellin', sysdate());
INSERT INTO PERSON (ID, NAME, LOCATION, BIRTH_DATE) VALUES (10002, 'Felipe', 'Medellin', sysdate());
INSERT INTO PERSON (ID, NAME, LOCATION, BIRTH_DATE) VALUES (10003, 'Pablo', 'Medellin', sysdate());
```

# Spring JDBC

## Spring JDBC vs JDBC

Spring JDBC is a feature created by Spring and basically help us to write simplier code when we want to use the JDBC approach.

1. If we use raw JDBC we need to use four interfaces. Driver, Connection, PreparedStatement and ResultSet.

2. If we use Spring JDBC we only need to call JdbcTemplate.

3. In the background Spring JDBC uses JDBC.

4. If you use Spring JDBC you do not need to worry about the connection handling.

4. Spring JDBC makes easier to write code.(less amount of code)

## Jdbc Template

Jdbc Template is a class provided by Spring that help us to configure automatically the connection with the database and also to write better code using the JDBC approach, this JdbcTemplate class works with the help of the Autoconfiguration feature.

## Creating Dao Jdbc Template

In the next code there is a basic Dao using Jdbc Template

```java
@Repository
public class PersonJdbcDao {

        @Autowired
        JdbcTemplate jdbcTemplate;

}
```

1. The class is marked as @Repository that means that Spring is going to know that this class is needed and spring is going to create an instance of this class. Also @Repository means that this class is related with the database comunication.

2. The JdbcTemplate is injected in the class, this class is autoconfigured by Spring and give us the connection and some methods to run SQL queries, as you are going to see in the next examples.

## FindAll Jdbc

The next code is going to return all the people that were inserted in the database.

```java
public List<Person> findAll() {
        return jdbcTemplate.query("select * from person",
                new BeanPropertyRowMapper<Person>(Person.class));
}
```

1. To retrieve all the people jdbc use the method "query" plus a sql staetment.

2. The BeanPropertyRowMapper is used when the atributes in your class are exactly the same that the atributes in the table, that means that Person(Object) and person(table) have the same atributes.
 
## FindById Jdbc

The next code is going to return the information of a person fiiltering by id.

```java
public Person findById(Integer id) {
        return jdbcTemplate.queryForObject
                ("select * from person p where p.id = ?",
                        new Object[]{id},
                        new personRowMapper());
}

class personRowMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet resultSet, int i) throws SQLException {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setLocation(resultSet.getString("location"));
            person.setBirthDate(resultSet.getTimestamp("birth_date"));
            return person;
        }
}
```

1. To retrieve one person we need to use the "queryForObject" method of the JdbcTemplate and also a sql 

2. There is a Object[] that is used to pass the parameters to the sql query.

3. There is a RowMapper custom implementation that is used to map to the entity(This is very similar to the BeanPropertyRowMapper, with the main difference that you can create a RowMapper)

## DeleteById Jdbc

```java
public Integer deleteById(Integer id) {
        return jdbcTemplate.update("delete from person p where p.id = ?", id);
}
```

1. To delete rows of the database we must use the method "update" plus a sql.

2. The update method return the amount of rows that the execution affected.

## Insert Jdbc

The next code is going to insert one row in the database

```java
public Integer insert(Person person) {
        return jdbcTemplate.update(
                "insert into person (id, name, location, birth_date)" +
                        " values (?,?,?,?)",
                        person.getId(),
                        person.getName(),
                        person.getLocation(),
                        new Timestamp(person.getBirthDate().getTime()));
}
```

1. Inserting information in the database require the method "update" plus a sql.

2. We should send every single parameter that is require to the insertion.

## Update Jdbc

The next code is going to insert one row in the database

```java
public Integer update(Person person) {
        return jdbcTemplate.update(
                "update person set name=?, location=?, birth_date=?" +
                "where id = ?",
                person.getName(),
                person.getLocation(),
                new Timestamp(person.getBirthDate().getTime()),
                person.getId());
}
```

1. Updating information in the database require the method "update" plus a sql.

2. We should send every single parameter that is require to the insertion.

All the previous methods are written in the database-demo project in the PersonJdbcDao class.

# Spring JPA

JPA is another approach when we are dealing with databases, the main difference is in a JPA approach we do not need to write sql code and all we have to do is write Java classes.

JPA is a standard, the most famous implementation is Hibernate.

## ORM

ORM stands for Object Relational Mapping and is an strategy used it to map information from the relational world like tables to the objectual world like objects. 

## Entities

The entities are the representation of the relational world in the objectual world.

Example:

Relational World.


```sql
create table PERSON
(
    ID INTEGER not null,
    NAME VARCHAR(255) not null,
    LOCATION VARCHAR(255),
    BIRTH_DATE timestamp,
    primary key(id)
);
```

Objectual World

```java
package com.learnig.database.databasedemo.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity
@NamedQuery(name="find_all_students", query="Select s from Student s")
public class Person {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String location;
    private Date birthDate;

    public Person() { // todo this is require
    }

    public Person(int id, String name, String location, Date birthDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
```

## Entity Manager

Entity manager, manage the entities, that means all the operation that you need to perform in a specific session.

```java
   @PersistenceContext
    EntityManager entityManager;
```

Some facts about the entity manager.

1. If you're inside a transation and you're managing something with the entity manager, updating, adding, deleting that particular thing continue being managed by the entity manager until the end of the transaction.

2. EntityManager is the interface of the persistence context.

### persist.

Persist create a new Object in the database.

```java
Course course = new Course("New course Example");
entityManager.persist(course);          // Create new Object in the DB
```

### merge.

Merge method update an specific object in the database.

### remove.

The remove method is used to delete an specific object in the database.

### flush.

The method flus is going to sabe all the information or commit all operation until that specific point.

### detach.

The detach(Object o) method help us to remove the track of a certain object, if we detach one object the 
entity manager is not going to manage that object anymore.

### clear.

The clear() method clean all the operation until that point, for example if we do not add any flush() 
and use clear, nothing in the db is going to be updated because is a transanction.

### refresh.

The refresh() method help us to recovery the entity information from the DB.

Example step1: persist(), step2: flush(), step3, setValue(), step4 refresh()

If the refresh method is delete it the final info in the db is going to be the info
in the setValue, but because we are using the refresh the final info is the original.

## Repository

The repository could be compare with a Dao class, in this class usually are created all the logic related with the database, like insertions or updates.

The repository class uses the entityManager in order to handle all the operation with the database, also require to use the entity objects to create the relation with the specific tables.

The next example is a basic repository class.

```java
@Repository
@Transactional
public class CourseRepository {

    @Autowired
    EntityManager entityManager;
}
```

## JPQL 

Java Persistance Query Language, is a way of write queries using entities.

```java
String jpql = "Select c from Course c";
Query query = entityManager.createQuery(jpql);
return (ArrayList) query.getResultList();
```

In the previous example we used the entity manager as usual and also we define the Select jpql query in order to retrieve all the courses from the database.

Examples

```java
   public void retrieveCoursesWithoutStudents() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c where c.students is empty", Course.class);
        List<Course> resultList = query.getResultList();
        logger.info("Course without students {}", resultList);
    }

    public void retrieveCoursesWithMoreThanTwoStudents() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c where size(c.students) >= 2", Course.class);
        List<Course> resultList = query.getResultList();
        logger.info("Course without students {}", resultList);
    }

    public void retrieveCoursesOrderBySize() {
        TypedQuery<Course> query = entityManager.createQuery("Select c from Course c order by size(c.students)", Course.class);
        List<Course> resultList = query.getResultList();
        logger.info("Course without students {}", resultList);
    }

    // JOIN -> Select c,s from Course c JOIN c.students s;
    // LEFT JOIN -> Select c,s from Course c LEFT JOIN c.students s;
    // CROSS JOIN -> Select c,s from Course c CROSS JOIN c.students s;
    public void join() {
        Query query = entityManager.createQuery("Select c,s from Course c JOIN c.students s");
        List<Object[]> resultList = query.getResultList();
        logger.info("JOIN Course size-> {}", resultList.size());
        for (Object[] result : resultList) {
            logger.info("Course {} Student {}", result[0], result[1]);
        }
    }
    
     public List<Student> retrieveStudentWithPassport() {
           TypedQuery<Student> query = entityManager.createQuery("Select s from Student s where s.passport.number like '%11%'", Student.class);
           return query.getResultList();
       }
```

As you can see in the above examples with JPQL you use the name of the entity instead of tables, and even you can use nested properties as Student.passport.number.

## Named Queries

Named Queries is a way to re use JPQL queries.

To define a NamedQueries we have to follow the next steps:

1. Go to the entity.
2. Create the Query in the top
3. If you want to create more than one NamedQuery you must use the annotation NamedQueries.

Example Named Query:

```java
@NamedQuery(name="query_get_all_courses", query="Select c from Course c"),
```

Example NamedQueries

```java
@NamedQueries(value = {
        @NamedQuery(name="query_get_all_courses", query="Select c from Course c"),
        @NamedQuery(name="query_get_all_courses_copy", query="Select c from Course c")
})
```

To call a NamedQuery you should only use the entity manager's method createNamedQuery with the name of the QueryMethod.

```java
    public List<Course> selectWithJPQL() {
        String jpql = "Select c from Course c";
        Query query = entityManager.createNamedQuery("query_get_all_courses");
        return (ArrayList) query.getResultList();
    }
```

## Criteria Query

JPQL is not the only way to retrieve information from the DB, there is another approach that is call Criteria Query.

The main difference between these approaches is that JPQL syntax is very similar to SQL, and Criteria Query is not related with SQL
is pure Java code.

In order to use Criteria Query you can follow the next steps.

1. Use Criteria Builder to create a Criteria Query returning the expected result object.

2. Define root for tables that are involved in the query

3. Define predicates etc using Criteria Builder.

4. Add Predicate etc to the Criteria Query.

5. Build the TypedQuery using the entity manager and the Criteria Query.

Examples:

```java
@Repository
@Transactional
public class CourseRepositoryCriteria {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityManager entityManager;

    public void getAllCourses() {
        // Step 1 -> Select c from Course c;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        //Step 4

        //Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses with Criteria Query -> {}", courses);
    }

    public void getAllCoursesRelatedWithSpring() {
        // Step 1 -> Select c From Course c where name like '%Spring%'
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        Predicate likeSpring = criteriaBuilder.like(courseRoot.get("name"), "%Spring%");

        //Step 4
        criteriaQuery.where(likeSpring);

        // Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses with word Spring and with Criteria Query -> {}", courses);
    }

    public void getAllCoursesWithoutStudents() {
        // Step 1 -> Select c From Course c where c.students is empty
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        Predicate studentIsEmpty = criteriaBuilder.isEmpty(courseRoot.get("students"));

        //Step 4
        criteriaQuery.where(studentIsEmpty);

        // Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses with no students and with Criteria Query -> {}", courses);
    }

    public void getAllCoursesJoinWithStudents() {
        // Step 1 -> Select c From Course c where c JOIN c.students
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        //Step 2
        Root<Course> courseRoot = criteriaQuery.from(Course.class);

        //Step 3
        Join<Object, Object> join = courseRoot.join("students", JoinType.LEFT);

        //Step 4
        // Step 5
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(courseRoot));
        List<Course> courses = query.getResultList();
        logger.info("Courses Join with students and with Criteria Query -> {}", courses);
    }
}
```

## Relationships

When we are using relational database is very common that among tables exist some relations, for instances if we have two tables Student and Course, this both table are related in some way, the most common relation is that a student can have multiple courses and also a course can have multiple students,  in this example the relationship between these tables is ManyToMany. In the next steps we are going to see the different kind of relationships and how to configure each one.

## One To One

When we are talking about OneToOne Relationship is when one row of the table 1 just map to one row of the table 2. For instance let's say that there are two tables Student and Passport, usually the student only have only one passport and also that specific passport is only related with one student, so the relation is going to be OneToOne.

Steps to setup a OneToOne Relationship:

1. Create Entity Student

2. Create Entity Passport

3. Create the relationship using @OneToOne annotation.

```java
@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne
    private Passport passport;
}
```

In the previous code the annotation @OneToOne is in the Student class, nevertheless is also posible to put the annotation in the Passport entity with the relation to the Student, even is posible to put it in both entities and that is called Bidirectional relationship.

In the background the @OneToOne annotation is going to create in the entity a relation with another entity that relation is called foreign key.

4. Using relationship in Repository. 

```java
public void saveStudentWithPassport() {
        Passport passport = new Passport("z1234");
        entityManager.persist(passport);
        Student student = new Student("Andres New Course");
        student.setPassport(passport);
        entityManager.persist(student);
    }
```

As you can see in the previous code there is a dependency in the Entities, If you use persist(student) without previously use persist(passport) the application is going to fail., because you require a passport already inserted in the database to be able to map it in the student entity. So please be aware of the order of insertion.

## Many To One 

When we are talking about Many to One or One To Many relationship basically means that the relation between table 1 to table 2 are 0...n rows and viceversa.
So let's say that we have two entities Course and Review, one course could have more than one review.

Steps to setup OneToMany / Many to One relationships

1. First define in which table is going to be the reference.

2. Add the anotation @ManyToOne in the entiy that has the reference.

3. Add the anotation @OneToMany in the entity that does not have the reference

4. Add the mappedBy atribute in the table that does not have the reference with the name value of the reference.

Example.

```java

public class Review {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Course course;
    
    // constructors, getters and setters ....
}

public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "course") 
    private List<Review> reviews = new ArrayList<>();

    // constructors, getters and setters ....
}
```

The first thing that we need to understand about the above code is the reference, a reference basically means in which table is going
to be saved the relation or let's call it foreign key, usually the reference is store is the table with many relations in this case
in review.

Second, as you can see we use both anotations @OneToMany and @ManyToOne in order to make it work.

Third, the mappedBy must be write it in the table that does not have the anotation, and must have the same reference 
atribute name that was write in the review table. (in this case "course").

Finally in the background hibernate is going to create an new column in the review table called course_id, if we change the
reference name in the class course that name also is going to be changed (For instance, we named the refences as "c" in the database
is going to be create a column called c_id").  

## Adding Reviews.

To add information when is a OneToMany relationship we could follow the next steps.

1. Get the main entity.

2. Create the new information ("Using just new())"

3. Settup relation among the main entity and the new information

4. Persist the new information.

```java
 public void addReviewsForCourse() {
        Course course = findById(1001L);  // There is a course with id 1001 in the db.
        logger.info("course.getReviews -> {}", course.getReviews());
        //Creating Reviews
        Review review1 = new Review("5", "This one is a new description");
        Review review2 = new Review("5", "and here we again.");

        // Setting relationships
        review1.setCourse(course);
        course.addReview(review1);
        course.addReview(review2);
        review2.setCourse(course);

        // Adding review to the db
        entityManager.persist(review1);
        entityManager.persist(review2);
        logger.info("course.getReviews -> {}", course.getReviews());
    }
```

## Deleting Reviews.

In order to delete one row of the OneToMany relationship we can follow the next steps.

1. Get the main entity.

2. Remove the element that we want to delete from the list

3. Merge the changes

```java
 public void deleteTheFirstReview() {
        Course course = findById(1001L);
        logger.info("course.getReviews -> {}", course.getReviews());
        List<Review> reviews = course.getReviews();
        Review review = reviews.get(0);
        course.removeReview(review); // In the course Entity there is a method that removes that specific element
        entityManager.merge(course);
        logger.info("course.getReviews -> {}", course.getReviews());
    }
 ```
 
## Many To Many

Many to Many is the most complex relationship in the relational world, and basically is when both tables can have multiples
related rows in the other table, for instance, let's say that we have two entities courses and students.

* As we may know one course can have multiple students, right?, also one students could be attending multiple courses.

* In the above case the relationship between course and student tables are many to many.

* The proper way to handle this kind of relationship is creating something called "Join Tables". Join Tables means a table
that is created only with the ids of the main tables, for instance (JoinTableName -> Course_Student with columns student_id and
course_id) and in this way we break the Many to Many relationship into two one to many relationship.

### Tables with Many to Many RelationShip

![](https://github.com/andresmontoyab/Spring/blob/master/resources/many-to-many.PNG)

### Tables with join table

![](https://github.com/andresmontoyab/Spring/blob/master/resources/many-to-many-join-table.PNG)

Steps to setup the many to many relationship.

1. Put the @ManyToMany annotation in both entities.

With this step is enough, nevertheless there is something wrong, if we run the application we noticed that hibernate created
two join table course_student and student_course the reason is because both entities have the @ManyToMany and there's no any owner of
the relationship. In order to fix that once more time we use the mappedBy atribute.

2. Use the mappedBy atribbute in the owner of the relationship.

Example:

 ```java
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}


public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    private List<Course> courses = new ArrayList<>();
}
 ```
 
```java
 public class Course {
 
     @Id
     @GeneratedValue
     private Long id;
 
     private String name;
 
     @ManyToMany(mappedBy = "courses")
     private List<Student> students = new ArrayList<>();
 }
```

Even that the above approach works there is a little problem, in term of design what happen if the join table or even the column names
in the join table are already created an have different names that course_student, course_id, student_id, in order to solve that
we can replace the mappenBy setup by the @JoinTable annotation and the result is pretty much the same.

```java
 public class Course {
 
     @Id
     @GeneratedValue
     private Long id;
 
     private String name;
 
     @ManyToMany
     @JoinTable(name="STUDENT_COURSE",
     joinColumns = @JoinColumn(name = "STUDENT_ID"),
     inverseJoinColumns = @JoinColumn(name = "COURSE_ID"))
     private List<Student> students = new ArrayList<>();
 }
```
 
 ## Adding course and Student 
 
```java
public class StudentRepository {
    public void insertStudentAndCourse(){
         // Create objects
         Student student = new Student("Jack");
         Course course = new Course("Microservices in 1000 steps");
    
         // Getting sequence from hibernate.
         entityManager.persist(student);
         entityManager.persist(course);
    
         // Setup relation
         student.addCourse(course);
         course.addStudent(student);
    
         // update the relation in db, in this point is when the record are going to be inserted by hibernate.
         entityManager.persist(student);
    
         // only at the end hibernate is going to insert the records
     }
     
    public void insertCourseToAStudent(Long id) {
            Student student = entityManager.find(Student.class, id);
            Course course = new Course("Trying to add a new course");
            student.addCourse(course);
            entityManager.persist(course);
            entityManager.persist(student);
        }
}
```

As we see above, if we want to add record to the database when are relationship among the entities are require three steps:

1. Create Object
2. Setup relationship
3. Persist all the object.(If you don't have any cascade configuration)

## Fetching

When we are using Jpa-Hibernate there is a very important concept that is called fetching and basically means when and how the information is loaded from the database. For instance if we have a relationship between two tables and we want to retrieve all the info of the table and also all the info of the sub-tables only calling a findBy of the main entity is going to loaded all the info of the sub-entities but we can setup this behaviour.

### Lazy

If we setup the relationship as lazy the information of the sub-entities is going to be loaded only when is really require it.

### Eager

If we setup the relationship as eager the information of the sub-entities and the main entity are going to be loaded all together.

### Fetch By RelationShip

At this point we already defined what are the relationships in hibernate and what are the fetch types, it is also important to know
that by default those relationships have an specific fetch type

@OneToOne   -> Eagerly

@ManyToOne  -> Eagerly

@OneToMany  -> Lazy

@ManyToMany -> Lazy

## Cascade Types

In order to know more about what is cascade you ca refer to the next link. https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/

## JPA Inheritance

As we may know one of the main features of the OOP world is the inheritance, now the question Can we use inheritance with JPA? and the answer is yes.

Let's explain JPA inheritance with an example.

We have two kind of employees: FullTimeEmployee(salary) and PartTimeEmployee(hourlyWage) and both of this employees share some atributes
as id and name.

Steps to setup the inheritances

1. Define an abstract class with common properties.

2. Create the concrete classes that extend from the abstract class

3. Define the inheritance type to use (Single Table, Table per class, Mapped super class or Joined)

```java
@Entity
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public Employee() {
    }
  }
  
  @Entity
  public class FullTimeEmployee extends Employee {
  
      public FullTimeEmployee() {
      }
  
      public FullTimeEmployee(String name, BigDecimal salary) {
          super(name);
          this.salary = salary;
      }
  
      private BigDecimal salary;
}

@Entity
public class PartTimeEmployee extends Employee {

    public PartTimeEmployee() {
    }

    public PartTimeEmployee(String name, BigDecimal hourlyWage) {
        super(name);
        this.hourlyWage = hourlyWage;
    }

    private BigDecimal hourlyWage;
}
```

## Single Table

Single table is de default type of inheritance and means that all of the information of the concrete class (FullTimeEmployee and PartTimeEmployee) 
are going to be store in the same table(Employee) and in this table one column is going to be add named DTYPE that basically means
what kind of employee is.

In order to setup Single Table inheritance we must update the abstract class adding @Inhertiance annotation, also if we want to change
the name of the DTYPE column we can add the annotation @DiscriminatorColumn(name = "EMPLOYEE_TYPE") with the name

```java
@Entity
@Inheritance
@DiscriminatorColumn(name = "EMPLOYEE_TYPE")
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public Employee() {
    }
  }
```

Regarding the single table approach it is very good in performance because all the information is only in one table, so no join are required
but regarding data integrity has some problems, because there are going to be a lot of "null" values.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/inheritance-single-table-null.PNG)

## Table per class

Table per class is another approach to the JPA inheritance but with this approach JPA is going to map the concrete class to different
tablas, one table per concrete class, so in this way there is no only one table could be n tables.

In order to setup the table per class we only need to add the  inheritance strategy type as TablePerClass

Example:

```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public Employee() {
    }
  }
```

The performance with this approach is slower than the Single table, because we need to union all the tables if we want to retrieve all the 
employees.

There is another problem with this approach all the common field are mapped in both tables, so if we have 3 concrete tables, these three tables
are going to have the common fields.

## Joined 

Joined strategy solves the problem of the Table Per class in where all the common field are mapped in all the tables, with this approach
JPA is going to create one Table for the abstract class with all the common fields and also create table for the concrente tables with the
specific fields. In order to relate the concrete tables with the parent table, in each concrete table is going to be store a foreign key with the 
needed id.

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public Employee() {
    }
  }
```
If we want to retrieve all the information (common fields + specific fields) we need to use joins with the parent table and concrete tables.

## MappedSuperClass

This last option is basically not use inheritance at all, the abstract class is going to be only a map class, and for this reason
this class can not be an entity. Mapping only applied to subclasses because no table exist for the abstract class.

```java
@MappedSuperclass
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public Employee() {
    }
  }
```

MapSuperclass completly delete the inheritance from the abstract class to the concrete class,
so the difference between MapSuerclass and Table per class is that for Table per class the abstract 
class is an entity and there is an inheritance relationship among the abstract class and the concrete, but in the
mappedSuperClass there isn´t.

## Transaction Management

Let's talk now about transactions, this is one of the most important concepts when we are talking about database, a transaction is
a number of steps that must be done in order to finish a specific task.
 
Example: The "transfer" money transaction have two steps

1. Withdraw money from account A 
2. Deposit money in the account b. 

But with this transaction arises one questions, What happend if the second operation fails? What the application should do?

In the above scenarios the application should rollback(Revert all the changes to the initial status.), but if both steps(all steps)
in the transacction finish sucessfully the aplication must commit the changes (preserve the changes in the db.)

In relation database there are some key concepts that we must know.

### ACID

* A - Atomic: If the transaction have multiples steps, the changes are only going to be commited if all the steps are sucessfully.

* C - Consistency: Be sure to keep the integrity of the data.
  
* I - Isolate: One transaction or operation can not affect another one.

* D - Durable: When an operation finish, the result must be keep it in the db.

## Isolation Problems

When we are talking about isolation, we find some problems some of them are the next:

1. Dirty Read: Happens when we are working in a parallel environment an the row that we are using is changed in the same time
for another Thread , and that cause that the consistency of the application fails.

2. Non repeatable Read:  When I'm reading the same value twice in the transaction and I get two different values.

3. Phanthom Read: At different times I'm getting different number of rows in the same transaction with the same query.

## Isolation Levels 

1. Read Uncommited: No restriction, you allow any transaction to read any data.

2. Read Commited: Only allow a transaction to read the data if is commited by other transaction.

3. Repeatable Read: It will lock an specific row, and it will not be available for other transaction, only when this transaction
complete the execution this row will be release(this only is going to create a lock for one row).

4. Serializable: A Lock will be create for any row that matches a predicate(where a.name = '%a%'), so those transaction that 
are trying to modify or add at least one of those rows aren't allow to do it.


|                 | Dirty Read  |   Non repeatable Read | Phantom Read   |
|-----------------|-------------|-----------------------|----------------|
|Read Uncommited  |Possible     |Possible               |Possible        |
|Read Committed   |Solved       |Possible               |Possible        | 
|Repetable Read   |Solved       |Solved                 |Possible        | 
|Serializable     |Solved       |Solved                 |Solved          | 

In the above table are mapping the isolation problems with Isolation level solutions.

In order to configre the isolation level in spring you only need to modifies the @Transactiontional annotation.

```java
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@Transactional(isolation = Isolation.READ_COMMITTED)
@Transactional(isolation = Isolation.REPEATABLE_READ)
@Transactional(isolation = Isolation.SERIALIZABLE)
```

Be aware to import the @Transactional annotation from spring and not from javax.

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

                <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
		</dependency>

2. Config rest template bean.


                @Configuration
                public class AppConfig {

                        @Bean("restClient")
                        @LoadBalanced
                        public RestTemplate registerRestTemplate() {
                                return new RestTemplate();
                        }
                }

3. Call service

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

4. Config Properties.

                currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001

# Naming Server

In the previous configuration we add a pool of two host, but what happend if we create another instances in http://localhost:8002, Is our client side able to see this new instance?The answer is No, if we want that our client side see the new instance we must to change the properties file and added the new instance.

                currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001,http://localhost:8002

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

                spring.application.name=netflix-eureka-naming-server
                server.port=8761
                eureka.client.register-with-eureka=false
                eureka.client.fetch-registry=false

## Connect to Eureka Server

1. First in the microservices or project that we want to use the eureka services we must add the next dependency.

        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

2. Add in the microservices main class the @EnableDiscoveryClient anotation 

3. Finally configure the properties file in order to let the application know where is eureka.

                eureka.client.service-url.default-zone=http//:localhost:8761/eureka

4. Eureka Server with Ribbon.

Did you remember what was the problem that Naming Server fixs? Do not hardcoded information in our properties files, so basically if we want to connect Eureka with Ribbon the only thing that we have to do is delete the hardcoded configuration in our properties files, that is because we already install eureka client and ribbon, so in background they understand each other withouth the neeeded of configuration properties.

                ## Delete or comment the following line
                currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001,http://localhost:8002

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

                spring.application.name=netflix-zuul-api-gateway-server
                server.port=8765
                eureka.client.service-url.default-zone=http//:localhost:8761/eureka

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

                <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-starter-sleuth</artifactId>
                </dependency>

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

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-zipkin</artifactId>
		</dependency>

Also is required the dependency of RabbitMQ in order to send the information to the zipkin server.

                <dependency>
			<groupId>org.springframework.amqp</groupId>
			<artifactId>spring-rabbit</artifactId>
		</dependency>

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

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
		</dependency>

2. Enable Hystrix

Mark the main class with the next annotation

                @EnableHystrix

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