package guru.springframework.didemo.services;

import org.springframework.context.annotation.Bean;

public class GreetingServiceFactory {

    private GreetingRepository GreetingRepository;

    public GreetingServiceFactory(guru.springframework.didemo.services.GreetingRepository greetingRepository) {
        GreetingRepository = greetingRepository;
    }

    public GreetingService createGreetingService(String lang) {

        switch (lang) {
            case "en":
                return new PrmaryGreetingService();
            case "de":
                return new PrymaryGreetingServiceDeutch();
            case "es":
                return new PrimaryGreetingServiceSpanish();
            default:
                return new PrmaryGreetingService();
        }
    }
}
