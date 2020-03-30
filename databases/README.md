# Dealing with DB in Spring
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
            * [Embedded and Embeddable](#Embedded-and-Embeddable)
        * [Entity Manager](#Entity-Manager)
            * [Methods](#Entity-Manager)
                * [Persist](#persist)
                * [Merge](#merge)
                * [Remove](#remove)
                * [Flush](#flush)
                * [Detach](#detach)
                * [Clear](#clear)
                * [Refresh](#refresh)
            * [Entity Life Cycle Methods](#Entity-Life-Cycle-Methods)
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
		* [Locking](#Locking)	
			* [Optimistic Locking](#Optimistic-Locking)		
			* [Pessimistic Locking](#Pessimistic-Locking)	
        * [Transaction Management](#Transaction-Management)
            * [Isolation Problems](#Isolation-Problems)
            * [Isolation Levels](#Isolation-Levels)
    * [Spring Data](#Spring-Data)
        * [Pagination](#Pagination)
        * [Custom Search](#Custom-Search)
        * [Spring Data Jpa Rest](#Spring-Data-Jpa-Rest)
    * [Jpa Caching](#Jpa-Caching)
        * [First Level Cache](#First-LevelCache)
        * [Second Level Cache](#Second-level-Cache)
    * [N + 1 Problem](#N-+-1-Problem)        

	
# Spring and Databases

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

One important thing to understand with JPA/Hibernate is that even we do not to write sql code in the background hibernates is using JDBC, so internally
hibernrate is writing SQL code for us.


![](https://github.com/andresmontoyab/Spring/blob/master/resources/hibernate-architecture.PNG)

## ORM

ORM stands for Object Relational Mapping and is an strategy used it to map information from the relational world like tables to the objetual world like objects. 

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

## Embedded and Embeddable

There are some scenarios in where there are common fields among tables, so lets say we have two tables, the first one is called
student and the second one is call school, both of this tables have an address, right?. In that scenarios are we going to create
the address field in both entities or there is a way to just create it once and work for both?.

The answer is yes, with Embedded and Embeddable, this two annotations works for this kinda problems, let's see an example.

```java
@Embeddable
public class Address {

    private String line1;
    private String line2;
    private String city;

    public Address() {
    }
}

@Entity
public class School {
    
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    public School() {
    }

    public School(String name) {
        this.name = name;
    }
}

@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToOne
    private Passport passport;
    
    protected Student() {
    }
}
```

In the above code, both entities are going to have all the fields defined in the Address Embeddable class.

## Entity Manager

## Methods

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

## Entity Life Cycle Methods

There are some default methods that are related with the entity life cycle.

Basically there are two kind of methods Post and Pre. The post methods are execute after we load, persist, update or remove 
our information and the pre methods are execute before you persist, update or remove information.

The next list are the life cycle methods

1. PostLoad
2. PostPersist
3. PostRemove
4. PostUpdate
5. PrePersist
6. PreRemove
7. PreUpdate

If you want to use one of this, you have to do it in the repository(where the entity manager is created) and also mark one
method with one of the above list


```java
@PostLoad
private void postLoad() {
    // things here...
}
```

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

## Locking

In real world almost all the application have to deal with concurrency problems, in order to avoid those kind of problems we can think in two kind of solutions, 
the isolations level or lock the data that we need in the moment.

Lock basically are some solutions in where we can restrict the access to specfic data in the database in order to keep consistency.

## Optimistic Locking

1. In order to use optimistic locking, we need to have an entity including a property with @Version annotation

2. Optimistic locking is based on detecting changes on entities by checking their version attribute

3. If any concurrent update takes place, OptmisticLockException occurs. After that, we can retry updating the data.

```java
@Entity
public class Student {
 
    @Id
    private Long id;
 
    private String name;
 
    private String lastName;
 
    @Version
    private Integer version;
 
    // getters and setters
 
}
```

There are some rules if we want to use the optimistic lock

* Each entity must have only one version attribute.

* Type of version attribute must be: int, Integer, long, Long, short, Short or java.sql.Timestamp.

## Lock Modes

JPA provides us with two different optimistic lock modes (and two aliases):

1. OPTIMISTIC – READ : 

OPTIMISTIC or READ will ensure the other object has not been updated at the time of your commit.

2. OPTIMISTIC_FORCE_INCREMENT - WRITE:

OPTIMISTIC_FORCE_INCREMENT will ensure the other object has not been updated, and will increment its version on commit.

We can find all types listed above in the LockModeType class.

Example of using optimistic lock

```java 
// Using em.find()
entityManager.find(Student.class, studentId, LockModeType.OPTIMISTIC);

// Using a create query
Query query = entityManager.createQuery("from Student where id = :id");
query.setParameter("id", studentId);
query.setLockMode(LockModeType.OPTIMISTIC_INCREMENT);
query.getResultList()

// Explicit Locking
Student student = entityManager.find(Student.class, id);
entityManager.lock(student, LockModeType.OPTIMISTIC);

// Named Query
@NamedQuery(name="optimisticLock",
  query="SELECT s FROM Student s WHERE s.id LIKE :id",
  lockMode = WRITE)
```

One last thought about the optimistic lock is that because with this kind of lock we are not locking any table or data, it is not posible
deadlocks with this approach.

## Pessimistic Locking

We can use a pessimistic lock to ensure that no other transactions can modify or delete reserved data.

There are two types of locks we can retain: an exclusive lock and a shared lock. We could read but not write in data when someone else holds a shared lock. 
In order to modify or delete the reserved data, we need to have an exclusive lock.

## Lock Modes

1. PESSIMISTIC_READ: Allows us to obtain a shared lock and prevent the data from being updated or deleted. We won't be able to make any updates or deletes though.

If we want to update the data we need to use a PESSIMISTIC_WRITE LcokMode.

2. PESSIMISTIC_WRITE: Allows us to obtain an exclusive lock and prevent the data from being read, updated or deleted.

Any transaction that needs to acquire a lock on data and make changes to it should obtain the PESSIMISTIC_WRITE lock. According to the JPA specification, 
holding PESSIMISTIC_WRITE lock will prevent other transactions from reading, updating or deleting the data.

3. PESSIMISTIC_FORCE_INCREMENT: Works like PESSIMISTIC_WRITE and it additionally increments a version attribute of a versioned entity

This lock works similarly to PESSIMISTIC_WRITE, but it was introduced to cooperate with versioned entities – entities which have an attribute annotated with @Version.

Examples

```java
// em.find()
entityManager.find(Student.class, studentId, LockModeType.PESSIMISTIC_READ);

// Using a create query
Query query = entityManager.createQuery("from Student where studentId = :studentId");
query.setParameter("studentId", studentId);
query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

// Explicit Locking
tudent resultStudent = entityManager.find(Student.class, studentId);
entityManager.lock(resultStudent, LockModeType.PESSIMISTIC_WRITE);
query.getResultList()

// With Nam,e
@NamedQuery(name="lockStudent",
  query="SELECT s FROM Student s WHERE s.id LIKE :studentId",
  lockMode = PESSIMISTIC_READ)
```


## Lock Scope

Lock scope parameter defines how to deal with locking relationships of the locked entity

### PessimisticLockScope.NORMAL

We should know that the PessimisticLockScope.NORMAL is the default scope. With this locking scope, 
we lock the entity itself. When used with joined inheritance it also locks the ancestors.

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
 
    @Id
    private Long id;
    private String name;
    private String lastName;
 
    // getters and setters
}
 
@Entity
public class Employee extends Person {
 
    private BigDecimal salary;
 
    // getters and setters
}
```

### PessimisticLockScope.EXTENDED

The EXTENDED scope covers the same functionality as NORMAL. In addition, it's able to block related entities in a join table.

```java
@Entity
public class Customer {
 
    @Id
    private Long customerId;
    private String name;
    private String lastName;
    @ElementCollection
    @CollectionTable(name = "customer_address")
    private List<Address> addressList;
 
    // getters and setters
}
 
@Embeddable
public class Address {
 
    private String country;
    private String city;
 
    // getters and setters
}
```

With the above code, for the both entities Customer and also customer_address is going to be create a lock.

Another interesting fact we should be aware of is that not all persistence providers support lock scopes. (Hibernate does not support it)

## Transaction Management

Let's talk now about transactions, this is one of the most important concepts when we are talking about database, a transaction is
a number of steps that must be done in order to finish a specific task.
 
Example: The "transfer" money transaction have two steps

1. Withdraw money from account A 
2. Deposit money in the account b. 

But with this transaction arises one question, What happend if the second operation fails? What the application should do?

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

## Spring Data

Spring Data provides abstraction on top of the persistence store you are using (JPA, NoSQL, JDBC etc.) 
you can significantly reduce the amount of boilerplate code required to implement data access layers for those persistence stores

Spring Data has many modules corresponding to supported persistence stores:

1. Spring Data JDBC

2. Spring Data JPA 

3. Spring Data MongoDB

so forth ...


![](https://github.com/andresmontoyab/Spring/blob/master/resources/spring-data.PNG)

## Spring Data JPA

Spring Data JPA is the JPA specific implementaion of Spring Data, usually the JPA provider for Spring Data JPA is Hibernate.

In order to use Spring Data JPA we need to follow the next steps:

1. Configure our Entity(JPA) or Document(NoSql->Mongo)

2. Define an Id in the Entiy or Document

3. Create Interface that extends from JpaRepository interface

4. To the generics of JpaRepository the first argument is the entity and the second is the type of the Id

Example:

```java
@Entity
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}

public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

         
}

class MainApp implements CommandLineRunner {

    @Autowired
    CourseSpringDataRepository courseSpringDataRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringHibernateDepthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Course newCourse = new Course("New Spring Data Course");
        courseSpringDataRepository.save(newCourse); // Saving course
        newCourse.setName("New Spring Data Course - Updated");
        courseSpringDataRepository.save(newCourse); // Updating course
        
        courseSpringDataRepository.findAll(); // Getting all courses
        
        Sort sort = new Sort(Sort.Direction.DESC, "name");
        courseSpringDataRepository.findAll(); // Getting all courses sorted
        
    }
}
```


## Pagination

When you have a set of result and you want to divide those result in pages, for instante if we have 100 records in one table
and we want only to return 20 at time, so in this scenario we use pagination.

```java
      // Pagination
      void workingWithPagination() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Course> firstPage = courseSpringDataRepository.findAll(pageRequest);
        firstPage.getContent(); // First Page
        
        Pageable pageable = firstPage.nextPageable();
        Page<Course> secondPage = courseSpringDataRepository.findAll(pageable);
        secondPage.getContent();
} // Second Page
```

## Custom Search

If we want to add custom search to our spring data repository, we need to modify the interface that we created. There are 
two ways to create these custom queries:

1. Adding the method with the next structure "especialList" + exactly name atribute, for instance findByName();

The especialList are the next findBy, countBy, deleteBy.

2. Adding the @Query annotation and in inside writing JPQL.

```java
public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);
    List<Course> findByNameAndId(String name, String id);
    List<Course> countByName(String name);
    List<Course> findByNameOrderByIdDesc(String name);
    List<Course> deleteByName(String name);

    @Query("Select c From Course c where name like '%50%'")
    List<Course> courseWith50InName();

    @Query(value = "Select c From Course c where name like '%50%'", nativeQuery = true)
    List<Course> courseWith50InNameUsingNativeQuery();
}
```

## Spring Data Jpa Rest

We can expose our repositories as endpoint using Spring Data Jpa Rest.

Steps:

1. Download the Spring Data JPA Rest dependencie

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

2. Add the @RepositoryRestResource(path = "/courses") in the Repository.

```java
@RepositoryRestResource(path = "/courses")
public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);
    List<Course> findByNameAndId(String name, String id);
    List<Course> countByName(String name);
    List<Course> findByNameOrderByIdDesc(String name);
    List<Course> deleteByName(String name);
}
```

## Jpa Caching

Usually in applications we need to call external service as database in order to retrieve data that we need, this request
takes a good amount of time, but what happend if we need to retrieve multiples time the same information, Are we going to call
multiples time to database for the same information? The answer is no, we only are going to call one time and store that information
in cache and in the next calls we retrieve the data from the cache.

In JPA there are two levels of caching:

* FLC: First Level of Caching 
* SLC: Second level of Caching

| Transaction One         | Transaction Two         |  Transaction Three      | Transaction Four        |
|-------------------------|-------------------------|-------------------------|-------------------------|
|Persistence Context1(FLC)|Persistence Context1(FLC)|Persistence Context1(FLC)|Persistence Context1(FLC)|
|                                           Second Level Cache                                          |
|                                           Database                                                    |
 
 So as you can see in the above table, the first level of cache has the scope of the transaction, so you could cache information
 in only the same transactions.
 
 The second level of cache is across all the transactions, in the second level cache usually is store all the common information
 require for all the transactions
 
## First Level Cache
 
 In order to setup the first level of cache the only thing that you need to do is mark your method as @Transactional
 with the spring annotations.
 
## Second Level Cache
 
 If you want to setup a second level of cache please be aware that usually you only store in this cache data that does not change
 or not change often.
 
 Second Level cache needs configuration, in order to setup the second level cache you can follow the next steps:
 
 1. Add dependencie
 
 ```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-ehcache</artifactId>
</dependency>
```
		
2. Enable second level cache in properties file
 ```properties
    spring.jpa.properties.hibernate.cache.use_second_level_cache=true
 ```

3. Specify the caching framework, in this cause is going to be EhCache (application.properties)
 ```properties
     spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory
```
4. Setup hibernate about what is going to be cache (application.properties)
 ```properties
    spring.jpa.properties.javax.persistence.sharedCache.mode=ENABLE_SELECTIVE
 ```       
In this point your configuration can have multiple options:

    *. ALL: All entities an entity related state and data are cached
    
    *. NONE: Caching is disabled for the persistence unit
    
    *. ENABLE_SELECTIVE: Caching is enabled for all entities for @Cacheable(true) is specified. All another entities are not cached.
    
    *. DISABLE_SELECTIVE: Caching is enabled for all entities except those for which @Cacheable(false) is specified.
    
    *. Unspecified: Caching behavior is undefined: provider-specific default may apply.

5. Select the data to cache

In order to mark an entity as cacheable we mark it with @Cacheable

 ```java
 @Entity
 @Cacheable
 public class Course {
 
     @Id
     @GeneratedValue
     private Long id;
 
     private String name;
     
     // getter, setter and constructors
 
 }
 ```
 
 After all of this configuration our second level cache should works.
 
## N + 1 Problem

When we are talking about N + 1 Problem is about performance issue. Basically is when
we have lazy relationships in our relationships, let's see an example.

We have two entities Courses and Students:

* Course entities has a relation oneToMany with students.
* The students atribute in Course entity is Lazy Fetch.

 |Course                           | Amount Students |
 |---------------------------------|-----------------|
 |Software Architecture|Possible   |40               |
 |Database Design                  |35               | 
 |Learnig Testing                  |50               | 

With the above information let's review the behaviour  

1. When we retrieve all the courses from database, hibernate is going to run one JDBC query in order to retrieve
the information of the courses (without the student)

2. Because the relation between course and students is lazy all the students are not going to be loaded.

3. If we want to retrieve the student we need to iterate over the List<Course> and get the students "course.getStudent()" 
with that statement hibernate is going to run another query to get the student for that specific course.

4. At the end if we want to retrieve all the courses and students, four JDBC statement were created, that is the N + 1 Problem.

## Solving N+1 Problem

## First Solution

As we said before, this problem arise because we have a lazy fetch in our relationship, one of the easier way to solve this problem
is chaging the fetch type to eager.

But the consecuence to make it eager is that every time that we retrieve a course is going to load all the students.

## Second Solution

The second option is somethig call, Entity graph, this approach we are not going to change our entity and in the other hand
we can specify in a method that we want to retrieve the students too.


 ```java
 @Entity
 @Cacheable
 public class CourseRepository {
        
      @Autowired
      EntityManager entityManager;
    
      public void retrieveCoursesWithStudent() {
             EntityGraph<Course> entityGraph = entityManager.createEntityGraph(Course.class);
             entityGraph.addSubgraph("students");
             List<Course> courses = entityManager.createNamedQuery("query_get_all_courses", Course.class)
                     .setHint("javax.persistence.loadgraph", entityGraph)
                     .getResultList();
             courses.forEach(course -> logger.info("Course -> {} Students -> {}", course, course.getStudents()));
         }
 
 }
 ```
In the above solution we set up that every time that the method "retrieveCoursesWithStudent" is called, the Entity Manager is going
to create a graph in where we say "We want also the students", and with this method, we can leave the relationship as lazy
and only called the students when we needed.
 
    
