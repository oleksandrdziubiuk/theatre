package com.dev.theatre.security;

import static org.springframework.security.core.userdetails.User.withUsername;

import com.dev.theatre.model.Role;
import com.dev.theatre.model.User;
import com.dev.theatre.service.UserService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("Can't find user by email " + email));
        UserBuilder userBuilder = withUsername(email);
        userBuilder.password(user.getPassword());
        userBuilder.roles(user.getRoles().stream()
                .map(Role::getRoleName)
                .toArray(String[]::new));
        return userBuilder.build();
    }
}
