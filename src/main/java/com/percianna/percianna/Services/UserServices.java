package com.percianna.percianna.Services;

import com.percianna.percianna.Dto.EmailAlreadyExistsException;
import com.percianna.percianna.Dto.MailRequest;
import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;




    public User saveUser(User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        // Check if the email already exists in the database
        if (emailExists(user.getEmail())) {
            // Handle the case where the email already exists, e.g., throw an exception or return an error response
            throw new EmailAlreadyExistsException("Email address already exists");
        }

        User savedUser = userRepository.save(user);

        // Send a confirmation email
       // sendConfirmationEmail(savedUser);

        return savedUser;
    }
    private boolean emailExists(String email) {
        // Use the repository's findByEmail method to check if an entry with the given email exists
        User user = userRepository.findByEmail(email);
        return user != null; // If user is not null, the email exists
    }

    public List<User> getUser(){return userRepository.findAll();}
    public User getUserById(Long id){return userRepository.findById(id).orElse(null);}

    public User getUserByName(String name){return userRepository.findByFirstName(name);}
    public String deleteUser(Long id){ userRepository.deleteById(id);
        return "Success Delete User "+id;}
    public String deleteUserByName(String name){userRepository.deleteByFirstName(name);
        return "Success Delete User "+name;}


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
   public User updateUser(User user){

        User existingUser = userRepository.findById(user.getId()).orElse(null);
        existingUser.setEmail(user.getEmail());

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create a set of authorities or roles that the user has
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Add the user's roles to the authorities set, e.g., user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        // Create and return the UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Use email here
                user.getPassword(),
                true,
                true, // Set other flags (accountNonExpired, credentialsNonExpired, accountNonLocked) based on your needs
                true,
                true,
                authorities
        );
    }



    public Long getIdByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user != null? user.getId() : null;
    }

}
