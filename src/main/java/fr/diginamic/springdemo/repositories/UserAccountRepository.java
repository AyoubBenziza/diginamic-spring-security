package fr.diginamic.springdemo.repositories;

import fr.diginamic.springdemo.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);
    void delete(UserAccount user);
}
