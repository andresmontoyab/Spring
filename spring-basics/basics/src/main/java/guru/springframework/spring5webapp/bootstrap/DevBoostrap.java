package guru.springframework.spring5webapp.bootstrap;

import guru.springframework.spring5webapp.model.Author;
import guru.springframework.spring5webapp.model.Book;
import guru.springframework.spring5webapp.model.Publisher;
import guru.springframework.spring5webapp.repositories.AuthorRepository;
import guru.springframework.spring5webapp.repositories.BookRepository;
import guru.springframework.spring5webapp.repositories.PublisherRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DevBoostrap implements ApplicationListener<ContextRefreshedEvent>{

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private PublisherRepository publisherRepository;


    // Spring efectua la injeccion de dependencias de estos dos atributos o interfaces.
    // esta clase será creada y controalda por spring, author y book repository obtendran una implementacion de spring data JPA, y haran auto wired.
    public DevBoostrap(AuthorRepository authorRepository, BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData() {

        Publisher pub1 = new Publisher("Worx","123456");


        // Eric
        Author eric = new Author("Eric", "Evans");
        Book ddd = new Book("Domain Driven Design", "1234", pub1);
        eric.getBook().add(ddd);
        ddd.getAuthors().add(eric);

        publisherRepository.save(pub1);
        authorRepository.save(eric);
        bookRepository.save(ddd);

        // Rod

        Author Rod = new Author("Rod", "Johnson");
        Book noEJB = new Book("J222 Development without EJB", "2345",pub1);
        Rod.getBook().add(noEJB);

        authorRepository.save(Rod);
        bookRepository.save(noEJB);
    }
}
