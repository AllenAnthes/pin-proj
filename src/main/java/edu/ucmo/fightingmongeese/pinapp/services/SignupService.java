package edu.ucmo.fightingmongeese.pinapp.services;

import edu.ucmo.fightingmongeese.pinapp.models.User;
import edu.ucmo.fightingmongeese.pinapp.models.UserRole;
import edu.ucmo.fightingmongeese.pinapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class SignupService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * set up a default user with two roles USER and ADMIN
     */
    @PostConstruct
    private void setupDefaultUser() {
        //-- just to make sure there is an ADMIN user exist in the database for testing purpose
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin",
                    passwordEncoder.encode("password"),
                    Arrays.asList(new UserRole("USER"), new UserRole("ADMIN"))));
        }
    }
}
