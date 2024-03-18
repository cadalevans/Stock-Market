package com.percianna.percianna.Controller;

import com.percianna.percianna.Entity.Order1;
import com.percianna.percianna.Entity.Portfolio;
import com.percianna.percianna.Services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class Order1Controller {

    @Autowired
    private OrderServices orderService;

    @PostMapping("/assignUser/{userId}")
    public ResponseEntity<Order1> assignUserToOrder(@PathVariable Long userId, @RequestBody Order1 order) {
        try {
            Order1 assignedOrder = orderService.assignUserToOrder(userId, order);
            return new ResponseEntity<>(assignedOrder, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user-orders/{userId}")
    public ResponseEntity<List<Order1>> getAllOrdersByUserId(@PathVariable Long userId) {
        List<Order1> orders = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user-portfolios/{userId}")
    public ResponseEntity<List<Portfolio>> getAllOrdersByUserIdPortfolio(@PathVariable Long userId) {
        List<Portfolio> portfolios = orderService.getAllOrdersByUserIdPortfolio(userId);
        return ResponseEntity.ok(portfolios);
    }


    @PutMapping("/update-order/{userId}/{orderId}")
    public ResponseEntity<Order1> updateOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @RequestBody Order1 updatedOrder
    ) {
        try {
            Order1 updated = orderService.updateOrder(orderId, userId, updatedOrder);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sell")
    public ResponseEntity<List<Order1>> getAllSellOrdersWithUsers() {
        List<Order1> sellOrders = orderService.getAllSellOrdersWithUsers();
        return new ResponseEntity<>(sellOrders, HttpStatus.OK);
    }
}
