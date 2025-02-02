package com.inventarios.prueba_tecnica.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.inventarios.prueba_tecnica.Entity.User;
import com.inventarios.prueba_tecnica.Entity.UserRegister;
import com.inventarios.prueba_tecnica.Repository.UserRepository;

public class UserInfoDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(name);
        return user.map(UserRegister::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: "));
    }
}
