# Inyecion de Dependencias

La inyeccion de dependencias es unas de las principales funcionalidades brindades por Spring framework, en esta seccion se mostrará con más cercania como es el funcionamiento de estas.

# Principios S.O.L.I.D en OOP!!.

## ¿Por que usar este principios?

La programacion orientada a objetos es un fuerte concepto, sin embargo esto asegura la calidad del desarrollo de software, los principios SOLID estan orientados en la gestion de las dependencias, cuando tenemos una gestion de dependencias pobre el codigo será fragil y dificil de cambiar.

## S. Single Responsability Principle, "Just because you can doesnt mean you should"

1. Cada clase deberá tener una sola responsabilidad.

2. Evade las clases "dios".

3. Tus clases deben de ser pequeñas.

4. Si tienes clases grandes, busca maneras de dividirlas en clases pequeñas.

En los codigo legacy se puede observar mucho este tipo de comportamiento en los cuales se pueden encontrar clases con aproximadamente 2000 lineas de codigo.
## O. Open Closed Principle

1. Tus clases deben estar abiertas para extensiones pero cerrada para modificaciones.

2. Se debe ser capaz de extender el comportamiento de una clase, sin modificarla.

3. Usa variables privadas con sus respectivos setter y getter cuando se necesiten.

4. Usar class y metodos abstractos.

## L. Liskov Substitution Principle

1. Los objetos en un programa podran ser reemplazados por instancias de sus subtipos sin alterar la exactitud del programa.

2. Cualquier objeto en cualquier clase en OOP puede ser reemplazado por un objeto hijo.

Ejemplo

		class TrasportationDevice
		{
		   String name;
		   String getName() { ... }
		   void setName(String n) { ... }
		   double speed;
		   double getSpeed() { ... }
		   void setSpeed(double d) { ... }
		   
		   Engine engine;
		   Engine getEngine() { ... }
		   void setEngine(Engine e) { ... }
		   void startEngine() { ... }
		}

		class Car extends TransportationDevice
		{
		   @Override
		   void startEngine() { ... }
		}

		class Bicycle extends TransportationDevice
		{
		   @Override
		   void startEngine() /*problem!*/
		}

La bicicleta no tiene motoro, por ende este nivel de abstraccion es incorrecto.		

Implementacion del principio L

		class TrasportationDevice
		{
		   String name;
		   String getName() { ... }
		   void setName(String n) { ... }
		 
		   double speed;
		   double getSpeed() { ... }
		   void setSpeed(double d) { ... }
		}

		class DevicesWithoutEngines extends TransportationDevice
		{  
		   void startMoving() { ... }
		}

		class DevicesWithEngines extends TransportationDevice
		{  
		   Engine engine;
		   Engine getEngine() { ... }
		   void setEngine(Engine e) { ... }
		 
		   void startEngine() { ... }
		}

		class Car extends DevicesWithEngines
		{
		   @Override
		   void startEngine() { ... }
		}


		class Bicycle extends DevicesWithoutEngines
		{
		   @Override
		   void startMoving() { ... }
		}

## I. Integration Segregation Principles

1. Crear interfaces granulares que el cliente especifique.

2. Diferentes interfaces especificas son mejor que una interfaz general.

3. Manten tus componentes enfocados y minimiza la dependencia entre ellos.

4. Evade la interfases Dioses.

5. No crear interfaces con metodos que las clases no requieren.

6. Este principio apunta a pequellas y altamente cohesivas interfaces.

Ejemplo 

		public interface Toy {
		    void setPrice(double price);
		    void setColor(String color);
		    void move();
		    void fly();
		}


		public class ToyHouse implements Toy {
		    double price;
		    String color;
		    @Override
		    public void setPrice(double price) {
		        this.price = price;
		    }
		    @Override
		    public void setColor(String color) {
		        this.color=color;
		    }
		    @Override
		    public void move(){}
		    @Override
		    public void fly(){}    
		}

Como pueden observar no son necesarios los metodos move y fly, por ende estariamos implementando metodos innecesarios.

Implementacion del principio I

		package guru.springframework.blog.interfacesegregationprinciple;
		public interface Toy {
		     void setPrice(double price);
		     void setColor(String color);
		}

		package guru.springframework.blog.interfacesegregationprinciple;
		public interface Movable {
		    void move();
		}

		package guru.springframework.blog.interfacesegregationprinciple;
		public interface Flyable {
		    void fly();
		}

		package guru.springframework.blog.interfacesegregationprinciple;
		public class ToyHouse implements Toy {
		    double price;
		    String color;
		    @Override
		    public void setPrice(double price) {
		        this.price = price;
		    }
		    @Override
		    public void setColor(String color) {
		        this.color=color;
		    }
		    @Override
		    public String toString(){
		        return "ToyHouse: Toy house- Price: "+price+" Color: "+color;
		    }
		}



		package guru.springframework.blog.interfacesegregationprinciple;
		public class ToyCar implements Toy, Movable {
		    double price;
		    String color;
		    @Override
		    public void setPrice(double price) {
		        this.price = price;
		    }
		    @Override
		    public void setColor(String color) {
		     this.color=color;
		    }
		    @Override
		    public void move(){
		        System.out.println("ToyCar: Start moving car.");
		    }
		    @Override
		    public String toString(){
		        return "ToyCar: Moveable Toy car- Price: "+price+" Color: "+color;
		    }
		}

		package guru.springframework.blog.interfacesegregationprinciple;
		public class ToyPlane implements Toy, Movable, Flyable {
		    double price;
		    String color;
		    @Override
		    public void setPrice(double price) {
		        this.price = price;
		    }
		    @Override
		    public void setColor(String color) {
		        this.color=color;
		    }
		    @Override
		    public void move(){
		        System.out.println("ToyPlane: Start moving plane.");
		    }
		    @Override
		    public void fly(){
		        System.out.println("ToyPlane: Start flying plane.");
		    }
		    @Override
		    public String toString(){
		        return "ToyPlane: Moveable and flyable toy plane- Price: "+price+" Color: "+color;
		    }
		}

Como se puede observar cada una de las clases implementa solamente las interfaces que requiere.		

## D. Dependency Inversion Principle

1. Las abstracciones deberian de depender sobre los detalles

2. Los detalles no deberian depender sobre las abstracciones.

3. Modulos a alto nivel no deberian depender de modulos de bajo nivel. Ambos deberian depender de abstraccion.

3. Es importante que los objetos a alto y bajo nivel dependan de la misma interaccion de abstraccion.

Ejemplo 

		public class LightBulb {
		    public void turnOn() {
		        System.out.println("LightBulb: Bulb turned on...");
		    }
		    public void turnOff() {
		        System.out.println("LightBulb: Bulb turned off...");
		    }
		}

		public class ElectricPowerSwitch {
		    public LightBulb lightBulb;
		    public boolean on;
		    public ElectricPowerSwitch(LightBulb lightBulb) {
		        this.lightBulb = lightBulb;
		        this.on = false;
		    }
		    public boolean isOn() {
		        return this.on;
		    }
		    public void press(){
		        boolean checkOn = isOn();
		        if (checkOn) {
		            lightBulb.turnOff();
		            this.on = false;
		        } else {
		            lightBulb.turnOn();
		            this.on = true;
		        }
		    }
		}

Como pueden notar anteriormente hay alto acoplamiento entre estas dos clases, debido a que una depende directamente de la otra.		

Aplicando el principio.

Como dice el princpio los modulos no deberian depender entre sí, deberian depender de abstracciones.

		package guru.springframework.blog.dependencyinversionprinciple.highlevel;
		public interface Switch {
		    boolean isOn();
		    void press();
		}

		package guru.springframework.blog.dependencyinversionprinciple.highlevel;
		public interface Switchable {
		    void turnOn();
		    void turnOff();
		}

		package guru.springframework.blog.dependencyinversionprinciple.highlevel;
		public class ElectricPowerSwitch implements Switch {
		    public Switchable client;
		    public boolean on;
		    public ElectricPowerSwitch(Switchable client) {
		        this.client = client;
		        this.on = false;
		    }
		    public boolean isOn() {
		        return this.on;
		    }
		   public void press(){
		       boolean checkOn = isOn();
		       if (checkOn) {
		           client.turnOff();
		           this.on = false;
		       } else {
		             client.turnOn();
		             this.on = true;
		       }
		   }
		}

		package guru.springframework.blog.dependencyinversionprinciple.lowlevel;
		import guru.springframework.blog.dependencyinversionprinciple.highlevel.Switchable;
		public class LightBulb implements Switchable {
		    @Override
		    public void turnOn() {
		        System.out.println("LightBulb: Bulb turned on...");
		    }
		    @Override
		    public void turnOff() {
		        System.out.println("LightBulb: Bulb turned off...");
		    }
		}

# Spring Aplication Context

En el contexto estaran almacenados todos los Spring bean utilizados por la aplicacion, allí spring especificará que es necesario para cada uno de estos y realizará la inyeccion de dependencias de los mismos.

Ejemplos.

1. Crearemos un pequeño controlador (cuando añadimos la anotamos @Controller automaticamente Spring lo interpreta como un bean.)

		package guru.springframework.didemo.controllers;

		import org.springframework.stereotype.Controller;

		@Controller
		public class MyController {

		    public String hello() {
		       System.out.println("Hello !!");
		       return "foo";
		    }
		}

2. Con nuestro controlador/bean creado, ahora veremos como se comporta en el contexto, para esto deberemos capturarlo desde nuestra aplicacion y podremos observar que nuestro controlador ya fue instanciado por Spring.

package guru.springframework.didemo;

import guru.springframework.didemo.controllers.MyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

		@SpringBootApplication
		public class DiDemoApplication {

			public static void main(String[] args) {

				ApplicationContext ctx = SpringApplication.run(DiDemoApplication.class, args);
				MyController controller = (MyController) ctx.getBean("myController");
				controller.hello();
			}
		}

3. Con estos dos pasos podremos ejecutar nuestro proyecto y el resultado será la implemetacion de nuestro metodo hello, es decir un mensaje de "Hola" en la consola. Lo que nos quiere decir que spring se encarga del gestionamiento de lso beans en su contexto en donde instancian clases que son nombradas como beans.

# Basic Dependency Injection

1. La Inyeccion de dependencias es donde una dependencia necesitada es inyecatada por otro objeto.

2. La clase siendo inyectada no tiene la responsabilidad de intanciar el objeto siendo inyectado (Evitamos la declaracion de objetos new Object)

## Tipos de Inyecion de dependencias.

1. Por propiedades de clases(Menos preferido) --> Usando propiedades privates (No recomendado).

Implemetacion Inyeccion de dependencias sin Spring

		    @Before
		    public void setUp() throws  Exception {
		        this.propertyInjectedController = new PropertyInjectedController();
		        this.propertyInjectedController.greetingService = new GreetingServiceImpl();
		    }

Implementacion inyeccion de dependencias con Spring

2. Por medio de Setters (Area de mucho debate).

Implemetacion Inyeccion de dependencias sin Spring

		    public void setUp() throws Exception {
		        this.getterInjectedController = new GetterInjectedController();
		        this.getterInjectedController.setGreetingService(new GreetingServiceImpl());
		    }

Implementacion inyeccion de dependencias con Spring


			@Controller
			public class PropertyInjectedController {

			    @Autowired
			    public GreetingServiceImpl greetingService;

			    public String sayHello() {
			        return greetingService.sayGreetings();
			    }
			}

			@Controller
			public class GetterInjectedController {
			    private GreetingService greetingService;

			    public String sayHello() {
			        return greetingService.sayGreetings();
			    }

			    @Autowired
			    public void setGreetingService(GreetingService greetingService) {
			        this.greetingService = greetingService;
			    }
			}

3. Por medio de constructores (Más preferido).

Implemetacion Inyeccion de dependencias sin Spring

		    @Before
		    public void setUp() throws Exception {
		        this.constructorInjectedController = new ConstructorInjectedController(new GreetingServiceImpl());
		    }

Implementacion inyeccion de dependencias con Spring

			@Controller
			public class ConstructorInjectedController {
			    private GreetingService greetingService;

			    public ConstructorInjectedController(GreetingService greetingService) {
			        this.greetingService = greetingService;
			    }

			    public String sayHello() {
			        return greetingService.sayGreetings();
			    }
			}

Como pueden observar cuando usamos la DI con los constructores no es necesario anotarlo con "autowired" debido a que Spring tiene activada automaticamente la DI con los constructores.

En conclusion con la anotacion @Controller le decimos a Spring que esta clase será un bean que deberá añadir al contexto y con la anotacion @Autowired le estamos diceindo que en ese punto deberá realizr una inyeccion de la dependencia solicitada.				    

## Concrete Claseses Vs Interfaces

1. DI puede realizarce sobre clases concretas o interfaces.

2. Generalmente DI en clases concretas debe evitarse.

3. DI con interfaces es más preferido, nos permite en tiempo de ejecucion decidir la implementacion a inyectar. Sigue el principio de segregation Interface ademas crea un codigo mucho más testeable.

## Inversion de Control IoC

1. Es una tecnica que termite que las dependencias sean inyectadas en tiempo real.

2. Las dependencias no estan predeterminadas.

Los marcos de trabajo amenudo juegan el rol de programa principal en la coordinacion y secuenciamiento de la actividad del programa . Esta invserion de control brinda a el marco de trabajo el poder de servir como un esqueleto extensible.

La forma de funcionar es que Spring se levanta, mira las clases anotadas y determina las dependencias y como wiring las dependencias entre si y satisfacer las depedencias. 

## Diferencia en IoC y DI

1. DI hace más referencia a la composicion entre clases, tu compones tus clases con DI en mente.

2. IoC es ambiente del codigo en tiempo real,  Spring Framework IoC container.

# Qualifiers

En algunas ocasiones tenemos varios bean bajo una misma implementacion, tenemos nuestra interface GreetingService y esta interface es implementada por 3 clases ConstructorGreetingService, SetterGreetingService y PropertyInjectedGreetingService (Estas tres clases estan bajo la anotacion @Service con la cual spring lás tomará bajo su contexto y creará un bean). Para estas tres clases necesitamos inyectar una dependencia de GreetingService, sin embargo como tenemos 3 bean diferentes de esta implementacion Spring no sabrá cual inyectarle, por lo cual surgen los Qualifiers, con esta herramienta podremos decirle a spring cual será el bean a inyectar.

1. Para el constructor-

		    @Autowired
		    public ConstructorInjectedController(@Qualifier("constructorGreetingService") GreetingService greetingService) {
		        this.greetingService = greetingService;
		    }

2. Para el setter

		    @Autowired
		    @Qualifier("setterGreetingService")
		    public void setGreetingService(GreetingService greetingService) {
		        this.greetingService = greetingService;
		    }

Recordemos que cuando Spring detecta un bean por alguna de las anotaciones, este lo guardará en el contexto como el nombre de la clase, empezando por minuscula.

# Primary Beans

Partiendo del mismo escenario anterior, cuando tenemos diferentes beans que implementaron una misma interface Spring no sabrá cual tomar a menos que le digamos con un Qualifier cual deberá ser, sin embargo hay otra opcion y son los primary bean, si no especificamos una Qualifier para inyectar en especifico y creamos nuestro primary bean spring inyectará este.

Creacion Primary Bean.

		@Service
		@Primary
		public class PrmaryGreetingService implements GreetingService {

		    @Override
		    public String sayGreetings() {
		        return "Hello - Primary GreetingService ";
		    }
		}

Inyeccion Primary Bean


		@Controller
		public class MyController {

		    private GreetingService greetingService;

		    public MyController(GreetingService greetingService) {
		        this.greetingService = greetingService;
		    }

		    public String hello() {
		       return greetingService.sayGreetings();
		    }
		}

Esta inyeccion se realizará por el constructor, como se puede observar no fue necesario @Autowired debido a que la inyeccion es por el constructor y tampoco necesitaremos @Qualifier debido a que nuestro Primary bean será inyectado ya que no hay ningun qualifier en especifico.

# Spring Profiles		

Los perfiles son una herramienta de Spring que nos permite variar de ambientes de ejecucion, por ejemplo en nuestro ambiente de desarrollo deberemos tener una configuracion determinada para la base de datos, y para nuestro ambiente de desarrollo otra configuracion, los perfiles de spring nos permite switchear facilmente entre estas caracteristicas.

1. Definicion de perfiles.

Perfil 1

		@Service
		@Profile("en")
		@Primary
		public class PrmaryGreetingService implements GreetingService {

		    @Override
		    public String sayGreetings() {
		        return "Hello - Primary GreetingService ";
		    }
		}

Pefil 2


		@Service
		@Profile("es")
		@Primary
		public class PrimaryGreetingServiceSpanish implements GreetingService {

		    @Override
		    public String sayGreetings() {
		        return "Hola Mundo - Spanish";
		    }
		}

2. Switchar de perfiles.

En nuestro archivo application.properties deberemos poner el perfil que deseamos activar(Solo 1).

		spring.profiles.active=es
		spring.profiles.active=en


# Profile Default

Como observamos anteriormente podemos swichear entre perfiles, sin embargo si debemos realizar una inyeccion de depedencia y tenemos 3 beans para ellos y cada uno de estos beans es de diferentes perfil, pero no especificamos ninguno de los perfiles a utilziar en el archivo application.properties, deberemos asignarle a uno de estos perfiles la cualidad de "default" para que este sea llamado cuando no se especifica ninguno, esto se podrá realizar de la siguiente manera.

		@Profile({"es","default"})

El default profile es solo activo cuando no se especifica ningun perfil.

# Spring Bean Factory




