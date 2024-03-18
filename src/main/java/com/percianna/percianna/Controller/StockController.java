package com.percianna.percianna.Controller;

import com.percianna.percianna.Dto.JsonReponse;
import com.percianna.percianna.Entity.Stock;
import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Repository.StockRepository;
import com.percianna.percianna.Services.StockServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StockController {

    @Autowired
    private StockServices stockServices;

    @PostMapping("/addstock")
    public Stock save(@RequestBody Stock stock) {

       return stockServices.addStock(stock);
    }

    @GetMapping("/getallstock")
    public List<Stock> getAll(){
        return stockServices.getAll();
    }

    @PutMapping("/updatestock/{symbol}")
    public ResponseEntity<Stock> updateStockLogo(@PathVariable String symbol, @RequestBody Stock updatedStock) {
        Stock updated = stockServices.updateLogoBySymbol(symbol, updatedStock.getLogo());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
