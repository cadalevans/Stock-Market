package com.percianna.percianna.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.percianna.percianna.Entity.ClosePriceInfo;
import com.percianna.percianna.Entity.Stock;
import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class StockServices {

    private static final String API_KEY = "VA32VORH1R2ZOW7G";
    private static final String API_BASE_URL = "https://www.alphavantage.co/query";

    @Autowired
    private StockRepository stockRepository;

    public Stock addStock(Stock stock){

        return stockRepository.save(stock);
    }


    @Scheduled(cron = "0 0 0 * * ?") // Run every 3 hour
    public void updateStockPricesAutomatically() {

        log.info("Scheduled task triggered at: {}", LocalDateTime.now());

        // Fetch and update stock prices
        List<String> enterpriseSymbols = Arrays.asList("TSLA", "GOOG", "AAPL", "PYPL", "AMZN", "TM", "SONY", "AMD",
                "CSCO", "IBM", "BLUE", "F", "PLTR", "MARA", "MSFT", "ORAN", "BABA", "META",
                "NVDA", "WMT");

        for (String symbol : enterpriseSymbols) {
            updateStockPrice(symbol);
        }
    }
    ////////////////////
    private void updateStockPrice(String symbol) {

        ClosePriceInfo closePriceInfo = fetchLatestClosePriceFromAlphaVantage(symbol);
        Optional<Stock> stockOptional = stockRepository.findBySymbol(symbol);

        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            stock.setCurrentPrice(closePriceInfo.getLatestClosePrice());
            stock.setOldPrice(closePriceInfo.getOldClosePrice());
            stockRepository.save(stock);
        }

    }

    private ClosePriceInfo fetchLatestClosePriceFromAlphaVantage(String symbol) {

        String url = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=%s", symbol, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        String alphaVantageResponse = restTemplate.getForObject(url, String.class);

        // Parse the Alpha Vantage response to extract and return the close prices
        return parseAlphaVantageClosePriceResponse(alphaVantageResponse);

    }

    private ClosePriceInfo parseAlphaVantageClosePriceResponse(String alphaVantageResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(alphaVantageResponse);

            JsonNode timeSeriesNode = rootNode.path("Time Series (5min)");

            // Get all timestamps in the "Time Series (5min)"
            Iterator<String> timestampIterator = timeSeriesNode.fieldNames();

            // Extract timestamps in reverse order
            List<String> timestamps = new ArrayList<>();
            while (timestampIterator.hasNext()) {
                timestamps.add(timestampIterator.next());
            }

            // Handle the case when there are no timestamps or just one timestamp
            if (timestamps.isEmpty()) {
                return new ClosePriceInfo(0.0, 0.0); // Return default values or handle appropriately
            } else if (timestamps.size() == 1) {
                double latestClosePrice = timeSeriesNode
                        .path(timestamps.get(0))
                        .path("4. close")
                        .asDouble();
                return new ClosePriceInfo(latestClosePrice, 0.0); // Assuming oldClosePrice is 0.0 in this case
            } else {
                // Assuming timestamps.size() >= 2
                double latestClosePrice = timeSeriesNode
                        .path(timestamps.get(0))
                        .path("4. close")
                        .asDouble();
                double oldClosePrice = timeSeriesNode
                        .path(timestamps.get(timestamps.size() - 1))
                        .path("4. close")
                        .asDouble();
                return new ClosePriceInfo(latestClosePrice, oldClosePrice);
            }
        } catch (Exception e) {
            // Handle parsing errors
            e.printStackTrace();
            return new ClosePriceInfo(0.0, 0.0); // Return default values or throw an exception based on your requirements
        }
    }

    public double getCurrentPrice(String symbol) {
        Optional<Stock> stockOptional = stockRepository.findBySymbol(symbol);
        return stockOptional.map(Stock::getCurrentPrice).orElse(0.0);
    }

    public double getOldPrice(String symbol) {
        Optional<Stock> stockOptional = stockRepository.findBySymbol(symbol);
        return stockOptional.map(Stock::getOldPrice).orElse(0.0);
    }

    // Get all stock

    public List<Stock> getAll(){

        return stockRepository.findAll();

    }


    public Stock updateLogoBySymbol(String symbol, String newLogo) {
        Optional<Stock> optionalStock = stockRepository.findBySymbol(symbol);

        if (optionalStock.isPresent()) {
            Stock existingStock = optionalStock.get();
            existingStock.setLogo(newLogo);
            // You can update other fields as needed
            return stockRepository.save(existingStock);
        } else {
            // Handle the case where the stock with the given symbol doesn't exist
            throw new IllegalArgumentException("Stock with symbol " + symbol + " not found");
        }
    }


}
