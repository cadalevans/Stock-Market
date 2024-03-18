package com.percianna.percianna.YahooFinance;

import com.percianna.percianna.Entity.StockData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@Service
public class YahooFinanceService {
    private static final String API_KEY = "VA32VORH1R2ZOW7G";
    private static final String API_BASE_URL = "https://www.alphavantage.co/query";


    public String fetchStockData(String symbol) {
        String url = String.format("%s?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=%s", API_BASE_URL, symbol, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    //Yahoo finance not working code
    public StockData getStockData(String symbol) {
        String apiUrl = "https://query1.finance.yahoo.com/v7/finance/quote?&symbols=" + symbol;
        RestTemplate restTemplate = new RestTemplate();
        StockData stockData = restTemplate.getForObject(apiUrl, StockData.class);

        return stockData;
    }

    // Global News
    //private static final String apiKey="";
   // private static final String apiUrl= "https://www.alphavantage.co/query?function=GLOBAL_NEWS&apikey=" + API_KEY;

    public String getGlobalNews(String symbol) {
        String url = String.format("%s?function=NEWS_SENTIMENT&tickers=%s&apikey=%s", API_BASE_URL, symbol, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }


    // ______________________ function=SYMBOL_SEARCH&keywords _________________________
    public String getMarket(String symbol) {
        String url = String.format("%s?function=SYMBOL_SEARCH&keywords=%s&apikey=%s", API_BASE_URL, symbol, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

}
//http://localhost:8080/api/yahoo-finance/historical-data?symbol=GOOG