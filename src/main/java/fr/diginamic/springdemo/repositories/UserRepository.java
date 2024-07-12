package fr.diginamic.springdemo.repositories;

import fr.diginamic.springdemo.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsernameAndPassword(String username, String password);
    UserAccount findByUsername(String username);
}
