package com.percianna.percianna.Controller;

import com.percianna.percianna.Dto.EmailAlreadyExistsException;
import com.percianna.percianna.Dto.JsonReponse;
import com.percianna.percianna.Dto.MailRequest;
import com.percianna.percianna.Entity.Order1;
import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Response.JwtResponse;
import com.percianna.percianna.Services.EmailService;
import com.percianna.percianna.Services.OrderServices;
import com.percianna.percianna.Services.UserServices;
import com.percianna.percianna.ressource.JWTUtility;
import com.percianna.percianna.ressource.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RequestMapping("/public")
@RestController

@CrossOrigin("**")
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderServices orderServices;

    @PostMapping("/adduser")
    public ResponseEntity<JsonReponse> addUser(@RequestBody User user) {
        try {
       User savedUser = userServices.saveUser(user);

            // Send a confirmation email
            sendConfirmationEmail(savedUser);
            return ResponseEntity.ok().body(new JsonReponse(true,"User created successfully"));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonReponse(false,"Email address already exists"));
        }
        // Add the user to the database


        // Send a confirmation email



    }

    //get total benefits
    @GetMapping("/users/{userId}/total-benefit")
    public ResponseEntity<Double> getTotalBenefitForUser(@PathVariable Long userId) {
        double totalBenefit = orderServices.getTotalBenefitForUser(userId);
        return ResponseEntity.ok(totalBenefit);
    }


    //get total investment amount
    @GetMapping("/users/{userId}/total-amount")
    public ResponseEntity<Double> getTotalInvestmentForUser(@PathVariable Long userId) {
        double totalBenefit = orderServices.getTotalInvestmentForUser(userId);
        return ResponseEntity.ok(totalBenefit);
    }

    //get total current value
    @GetMapping("/users/{userId}/total-value")
    public ResponseEntity<Double> getTotalCurrentValueForUser(@PathVariable Long userId) {
        double totalBenefit = orderServices.getTotalCurrentValueForUser(userId);
        return ResponseEntity.ok(totalBenefit);
    }

    private void sendConfirmationEmail(User user) {
        // Prepare the email model
        Map<String, Object> model = new HashMap<>();
        model.put("Name", user.getFirstName()); // Modify this to match your user entity
        model.put("location", "Ariana, Tunisia"); // Adjust as needed

        // Create a MailRequest object
        MailRequest request = new MailRequest();
        request.setTo(user.getEmail()); // Use the user's email as the recipient
        request.setFrom("louisfranck.moussima@esprit.tn");
        request.setSubject("Welcome to our platform!"); // Set the subject

        // Send the confirmation email
        emailService.sendEmail(request, model);
    }



    @GetMapping("/getuser")
    public List<User> findAllUser(){return userServices.getUser();}
    @GetMapping("/GetUser/{id}")
    public User findFatherById(@PathVariable("id")Long id){return userServices.getUserById(id);}
    @GetMapping("/user/{name}")
    public User findFatherByName(@PathVariable("name")String name){return userServices.getUserByName(name);}
    @PutMapping("/update")
    public User updateUser(@RequestBody User user){return userServices.updateUser(user);}

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id")Long id){return userServices.deleteUser(id);}
    @DeleteMapping("/remove/{name}")
    public String deleteUserByName(@PathVariable("name") String name){return userServices.deleteUserByName(name);}

   /* @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        User user = userServices.getUserByEmail(loginRequest.getEmail());

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Successful authentication
        return ResponseEntity.ok("Login successful");
    }

    */

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        User user = userServices.getUserByEmail(loginRequest.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // Successful authentication
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }




    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
        try {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );
        } catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails
                = userServices.loadUserByUsername(jwtRequest.getUsername());
        final  String token =
                jwtUtility.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @GetMapping("/getUserId")
    public ResponseEntity<?> getUserIdByEmail(@RequestParam String email) {
        Long userId = userServices.getIdByEmail(email);
        if (userId != null) {
            return ResponseEntity.ok().body(userId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
