package fr.diginamic.springdemo.mappers;

import fr.diginamic.springdemo.entities.UserAccount;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserMapper {
    public static UserDetails map(UserAccount userAccount) {
        return User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .authorities(userAccount.getAuthorities())
                .build();
    }
}
