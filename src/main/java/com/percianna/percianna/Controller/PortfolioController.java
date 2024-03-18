package com.percianna.percianna.Controller;

import com.percianna.percianna.Services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioController {

    @Autowired
    private OrderServices orderServices;
}
