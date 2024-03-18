package com.percianna.percianna.Controller;

import com.percianna.percianna.Dto.EmailAlreadyExistsException;
import com.percianna.percianna.Dto.JsonReponse;
import com.percianna.percianna.Entity.Sicav;
import com.percianna.percianna.Services.SicavServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SicavController {

    @Autowired
    private SicavServices sicavServices;

    @PostMapping("/addsicav")
    public Sicav save(@RequestBody Sicav sicav) {

          return sicavServices.saveSicav(sicav);

    }

    @GetMapping("/getsicav")
    public List<Sicav> getAllSicav(){
        return sicavServices.getAllSicav();
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignSicavToOrder(
            @RequestParam Long sicavId,
            @RequestParam String stockSymbol,
            @RequestParam int quantity) {

        try {
            sicavServices.assignSicavToOrder(sicavId, stockSymbol, quantity);
            return ResponseEntity.ok("Order assigned successfully.");
        } catch (Exception e) {
            // Handle the exception appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning order: " + e.getMessage());
        }
    }

    @PostMapping("/assignSicavToUser")
    public ResponseEntity<String> assignSicavToUser(
            @RequestParam Long sicavId,
            @RequestParam Long userId,
            @RequestParam double amountToInvest) {
        try {
            sicavServices.assignSicavToUser(sicavId, userId, amountToInvest);
            return ResponseEntity.ok("Sicav assigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
