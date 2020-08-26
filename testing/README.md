# Spring Test

* [Why testing](#Why-testing)
    * [Testing Pyramid](#Testing-Pyramid)
    * [Ice Cream Anti Pattern](#Ice-Cream-Anti-Pattern)
* [Code Under Test](#Code-Under-Test)
* [Types of Tests](#Types-of-Tests)
	* [Unit test](#Unit-test)
	* [Integration-Test](#Integration-Test)
	* [Functional test](#Functional-test)
* [TDD (Test Driven Development)](#TDD-(Test-Driven-Development))
* [BDD (Behavior Driven Development)](#BDD-(Behavior-Driven-Development))
* [Double Test](#Double-Test)
	* [Mock](#Mock)
	* [Spy](#Spy)
* [JUnit4 Annotations](#JUnit4-Annotations)
* [Spring Boot Anotations](#Spring-Boot-Anotations)
* [Creating a Junit Testing](#Creating-a-Junit-Testing)
* [Using Mockito](#Using-Mockito)
* [Integrations Tests](#Integrations-Tests)
	* [MockMVC](#MockMVC)
	* [DataJpa Test](#DataJpa-Test)
* [Maven Fail Safe](#Maven-Fail-Safe)

## Why testing

- Is a way to identify errors in the development stage.
- Increase product quality.
- Reduce maintenance cost.

### Testing Pyramid

When we are testing an application we should know that there are multiples kind of testing.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/testing-pyramid.PNG) 

Regarding the above image we can have the next conclusions.
 - Unit Test are the bases of the pyramid, ideally you're application should have the larger amount of unit test in order to detect bug pre-deploy.
 - Integration test are less in amount.
 - The final kind of testing is the manual, if we applied correctly unit/integration test in this phase there are not going to be a lot of bugs.

 ### Ice Cream Anti Pattern

 On the other hand if you break the testing pyramid is very likely that you're going to face the anti pattern ice cream.

![](https://github.com/andresmontoyab/Spring/blob/master/resources/ice-cream-anti-pattern.PNG)

In this anti pattern there is a lack of unit/integration testing, the amount of manual testing is huge, and that means a lot of time spend in manual testing.
 - One very important thing to keep in mind is that manual testing takes more time than unit/integration testing.


## Code Under Test

Is the code that is going to be tested.

## Types of Tests

### Unit test

1. It is code used to verify the behavior of the code under the test.
2. Unit test are designed to test specific code.
3. Unit test should be small and fast.
4. Unit test must be isolated, with no dependencies(DB, Context, Queues).

### Integration Test

1. An integration test is done to demonstrate that different pieces of the system work together.
2. Integration tests can cover whole applications.
3. Integration tests require much more effort to put together.
4. Can include Spring Context, database or message brokers.
5. Integration test are slower than unit test.

### Functional test

1. Tipically means testing of the running application.
2. The application is deploy in an environment.
3. All the functional features are testing, like web services, message brokers so forth.


## TDD (Test Driven Development)

Test-driven development (TDD) is a software development process that relies on the repetition of a very short development cycle: requirements are turned into very specific test cases, then the code is improved so that the tests pass.

Steps:

- In base of the scenarios write tests that pass those scenarios
- Run the test, because there are no implementation yet the test should fail (red)
- Start writing the implementation that make the test pass (green)
- Refactor the implementation

## BDD (Behavior Driven Development)

Behavioral Driven Development (BDD) is a software development approach that has evolved from TDD (Test Driven Development). It differs by being written in a shared language, which improves communication between tech and non-tech teams and stakeholders

Some technologies used to implement BDD are JBehave, Cucumber or Spock

## Double Test

### Mock

It is a class false implementation using in testing.

### Spy

It is a partial mock, let us overwrite some methods of the real class.
reales.

## JUnit4 Annotations

It is the most common testing framework in the Java/Spring environment.


- @Test: Mark a method as a test method.
- @Before: It is a method executed just before each test.
- @After: It is a method executed just after each test.
- @Ignore: Ignore a specific test.
- @Test(Expected = Exception.class): The expected atribute it is used when we need to check if the method thorws an execption.
- @Test(timeout = 10): Fails if the methods take more than 10 miliseconds

## Spring Boot Anotations

- @RunWith(SpringoRunner.class): Run the test with the Spring Context.
- @TestConfiguration: Set a configuration for the tests.
- @MockBean: Inject a mock
- @SpyBean: Inject a spy
- @DataJpaTest: It is used to test the data layer
- @Transactional: Run the as transaction, when the test finish it apply a rollback
- @BeforeTransaction: A method that is executed before the transaction
- @AfterTransaction: A method that is executed after the transaction
- @Commit: After the test finish must apply a commit.
- @Rollback: After the test finish must apply a rollback.
- @Sql: Set some sql script to run before.
- @Repeat: Repeat a test x times.

## Creating a Junit Testing

```java
public class CategoryTest {

    Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void getId(){
        Long idValue = 4l;
        category.setId(4l);
        assertEquals(idValue, category.getId());
    }
}
```

## Using Mockito

Base Class

```java
@Controller
public class IndexController {

	private final RecipeService recipeService;


	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping({"","/","/index"})
	public String IndexPage(Model model) {
		log.debug("Im in the controller");
		model.addAttribute("recipes", recipeService.getRecipe());

		return "index";
	}
}
```

Tests

```java
public class IndexControllerTest {

	IndexController indexController;

	@Mock
	RecipeService recipeService;
	@Mock
	Model model;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		indexController = new indexController(recipeService);
	}

	@Test
	public void indexPage() {

		// given
		Set<Recipe> recipes = new HashSet<>();
		Recipe recipe1 = new Recipe();
		recipe1.setId(1l);
		Recipe recipe2 = new Recipe();
		recipe1.setId(2l);
		Recipe recipe3 = new Recipe();
		recipe1.setId(3l);
		recipes.add(recipe1);
		recipes.add(recipe2);
		recipes.add(recipe3);

		when(recipeService.getRecipe()).thenReturn(recipes);

		ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

		//when
		String viewName = indexController.IndexPage(model);

		//then
		assertEquals("index", viewName);
		verify(recipeService, times(1)).getRecipe();
		verify(model, times(1)).addAttribute(anyString(), argumentCaptor.capture());
		Set<Recipe> indRecipes = argumentCaptor.getValue();
		assertEquals(2, indRecipes.size());


	}
}		
```

## Integrations Tests

### MockMVC

MockMvc is a tool that help us to test endpoints. Under the hood MockMvc use a mock servlet context.

```java
@Test
public void testMockMVC() throws Exception {
	MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

	mockMvc.perform(get("/")).
			andExpect(status().isOk())
			.andExpect(view().name("index"));
}
```

### DataJpa Test

In order to create an data layer integration test we could use the @DataJpaTest and @RunWith annotations.

```java
@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryIT {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void findByDescription() {
		Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Each");
		assertEquals("Each",unitOfMeasureOptional.get().getDescription());
	}

	@Test
	public void findByDescriptionCup() {
		Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Cup");
		assertEquals("Cup",unitOfMeasureOptional.get().getDescription());
	}

}
		```	    

## Maven Fail Safe

Maven Fail Safe It is a pluging that let us run the test phase either for unit test and for integration test.

In order to use this pluging you should add the next xml code in our pom file.

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-failsafe-plugin</artifactId>
	<version>2.20</version>
	<configuration>
		<includes>
			<include>**/*IT.java</include>
		</includes>
		<additionalClasspathElements>
			<additionalClasspathElement>${basedir}/target/classes</additionalClasspathElement>
		</additionalClasspathElements>
		<parallel>none</parallel>
	</configuration>
	<executions>
		<execution>
			<goals>
				<goal>integration-test</goal>
				<goal>verify</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```








