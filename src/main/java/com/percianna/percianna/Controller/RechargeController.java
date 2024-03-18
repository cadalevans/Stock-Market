package com.percianna.percianna.Controller;

import com.percianna.percianna.Entity.Recharge;
import com.percianna.percianna.Services.RechargeServices;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@CrossOrigin("**")
public class RechargeController {

    @Autowired
    private RechargeServices rechargeServices;

    @PostMapping("/payments/{userId}")
    public ResponseEntity<String> processPayment(@PathVariable Long userId,@RequestBody Recharge paymentRequest) {
        try {
            Recharge transaction = rechargeServices.processPayment(paymentRequest,userId);

            // Return a success response to the client
            return ResponseEntity.ok().body("Payment successful");
        } catch (StripeException e) {
            e.printStackTrace();

            // Return an error response to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment failed: " + e.getMessage());
        }
    }
    @GetMapping("/payments/all")
    public List<Recharge> findAll(){

        return rechargeServices.getAll();
    }


        private final String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/";

        @GetMapping("/definition/{word}")
        public String getDefinition(@PathVariable String word) {
            String fullUrl = apiUrl + word;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(fullUrl, String.class);
        }

}
