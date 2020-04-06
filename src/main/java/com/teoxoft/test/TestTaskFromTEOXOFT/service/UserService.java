package com.teoxoft.test.TestTaskFromTEOXOFT.service;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.User;
import com.teoxoft.test.TestTaskFromTEOXOFT.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.teoxoft.test.TestTaskFromTEOXOFT.entity.Role.ADMIN;
import static com.teoxoft.test.TestTaskFromTEOXOFT.entity.Role.USER;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void setDefaultUsers() {
        if (!userRepository.findById("user").isPresent()) {
            this.saveUser(new User("user", "password", USER));
        }
        if (!userRepository.findById("admin").isPresent()) {
            this.saveUser(new User("admin", "password", ADMIN));
        }
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).get();
    }
}
