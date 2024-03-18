package com.percianna.percianna.YahooFinance;

import com.percianna.percianna.Entity.StockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.util.List;

@RestController
//@RequestMapping("/api/yahoo-finance")
@CrossOrigin("**")
public class YahooFinanceController {
    @Autowired
    private YahooFinanceService yahooFinanceService;

    @GetMapping("/api/stock-data/fetch")
    public ResponseEntity<String> fetchStockData(@RequestParam String symbol) {
        String stockData = yahooFinanceService.fetchStockData(symbol);
        return ResponseEntity.ok(stockData);
    }
   /* @GetMapping("/api/stock-data")
    public StockData getStockData(@RequestParam String symbol) {
        return yahooFinanceService.getStockData(symbol);
    }

    */

    @GetMapping("/api/stock-data")
    public ResponseEntity<String> getGlobalNews(@RequestParam String symbol) {
        try {
            String newsData = yahooFinanceService.getGlobalNews(symbol);
            return ResponseEntity.ok(newsData);
        } catch (Exception e) {
            // Gérer l'exception de manière appropriée (journalisation, retourner une erreur, etc.).
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération des données.");
        }
    }

    @GetMapping("/api/stock-data/name")
    public ResponseEntity<String> getMarket(@RequestParam String symbol) {
        try {
            String newsData = yahooFinanceService.getMarket(symbol);
            return ResponseEntity.ok(newsData);
        } catch (Exception e) {
            // Gérer l'exception de manière appropriée (journalisation, retourner une erreur, etc.).
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération des données.");
        }
    }

}
