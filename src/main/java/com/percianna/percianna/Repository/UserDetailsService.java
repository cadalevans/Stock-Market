package com.percianna.percianna.Repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsService {
    UserDetails loadUserByFirstname(String firstName) throws UsernameNotFoundException;
}
