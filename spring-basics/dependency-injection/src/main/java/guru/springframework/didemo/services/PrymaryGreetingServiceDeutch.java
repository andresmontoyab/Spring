package guru.springframework.didemo.services;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

public class PrymaryGreetingServiceDeutch implements GreetingService {
    @Override
    public String sayGreetings() {
        return "Hi there in deucth";
    }
}
