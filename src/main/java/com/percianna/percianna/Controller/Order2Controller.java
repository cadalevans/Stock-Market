package com.percianna.percianna.Controller;

import com.percianna.percianna.Entity.Order1;
import com.percianna.percianna.Entity.Order2;
import com.percianna.percianna.Services.Order2Services;
import com.percianna.percianna.Services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
public class Order2Controller {

    @Autowired
    private Order2Services order2Services;

    @PostMapping("/assignSicav/{sicavId}")
    public ResponseEntity<Order2> assignSicavToOrder(@PathVariable Long sicavId, @RequestBody Order2 order) {
        try {
            Order2 assignedOrder = order2Services.assignSicavToOrder(sicavId, order);
            return new ResponseEntity<>(assignedOrder, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
