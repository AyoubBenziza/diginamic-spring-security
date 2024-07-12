package fr.diginamic.springdemo.services;

import fr.diginamic.springdemo.entities.UserAccount;
import fr.diginamic.springdemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password) != null;
    }

    public UserAccount getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username).getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
