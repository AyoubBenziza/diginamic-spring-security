package fr.diginamic.springdemo.services;

import org.springframework.stereotype.Service;

/**
 * Service for the Hello entity
 * This class is used to interact with the HelloRepository
 * @author AyoubBenziza
 */
@Service
public class HelloService {

    /**
     * Say hello
     * @return the hello message
     */
    public String salutations() {
        return "Je suis la classe de service et je vous dis Bonjour !";
    }
}
