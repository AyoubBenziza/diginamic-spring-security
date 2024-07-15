package fr.diginamic.springdemo.services;

import fr.diginamic.springdemo.entities.UserAccount;
import fr.diginamic.springdemo.exceptions.UsernameExistsException;
import fr.diginamic.springdemo.repositories.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        create(new UserAccount("admin", passwordEncoder.encode("admin"), "ROLE_ADMIN"));
        create(new UserAccount("user", passwordEncoder.encode("user"), "ROLE_USER"));
    }

    public boolean isAdmin(String username) {
        return userAccountRepository.findByUsername(username).getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public void create(UserAccount user) {
        userAccountRepository.save(user);
    }

    public void update(UserAccount user) {
        userAccountRepository.save(user);

    }

    public void delete(UserAccount user){
        userAccountRepository.delete(user);
    }

    public void registerUser(String username, String password) {
        if (userAccountRepository.findByUsername(username) != null) {
            throw new UsernameExistsException("There is already an account with that username: " + username);
        }
        UserAccount newUser = new UserAccount(username, passwordEncoder.encode(password), "ROLE_USER");
        userAccountRepository.save(newUser);
    }
}
