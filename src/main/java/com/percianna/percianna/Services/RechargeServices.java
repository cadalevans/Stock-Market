package com.percianna.percianna.Services;

import com.percianna.percianna.Entity.Recharge;
import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Repository.RechargeRepository;
import com.percianna.percianna.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class RechargeServices {

    @Value("${stripe.key.secret}")
    private String secretkey;

    @Autowired
    private RechargeRepository rechargeRepository; // repository pour stocker les paiements dans la base de données
    @Autowired
    private UserRepository userRepository;
    public RechargeServices() throws StripeException {
    }

    @Value("${stripe.key.secret}")
    private String secretKey;

    public void saveRecharge(Recharge recharge) {


        rechargeRepository.save(recharge);
    }

    // User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));



    public Recharge processPayment(Recharge paymentRequest, Long userId) throws StripeException {
        Stripe.apiKey = secretKey;



            // Create a PaymentIntent object with the payment details
            Map<String, Object> params = new HashMap<>();
            params.put("amount", paymentRequest.getAmount());
            params.put("currency", paymentRequest.getCurrency());
            params.put("payment_method_types", Collections.singletonList("card"));
            params.put("payment_method", "pm_card_visa");

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Confirm the PaymentIntent to charge the payment
            paymentIntent.confirm();

            // Update the user's balance with the payment amount
           /* double paymentAmount = (double) paymentIntent.getAmount() / 100; // Convert from cents to dollars
            user.setVirtualBalance(user.getVirtualBalance() + paymentAmount);

            // Save the updated user
            userRepository.save(user);
            */
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            paymentRequest.setUser(user);  // Set the user in the Recharge entity
            double paymentAmount = (double) paymentIntent.getAmount() / 100; // Convert from cents to dollars
            user.setVirtualBalance(user.getVirtualBalance() + paymentAmount);

            // Save the updated user
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

            // Set the payment details and save the transaction to the database
            paymentRequest.setPaymentId(paymentIntent.getId());
            return rechargeRepository.save(paymentRequest);

    }

    public List<Recharge> getAll(){
        return rechargeRepository.findAll();
    }
}
