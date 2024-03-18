package com.percianna.percianna.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomStockSymbolGenerator {

    public static void main(String[] args) {
        List<String> enterpriseSymbols = Arrays.asList("TSLA", "GOOG", "AAPL", "PYPL", "AMZN", "TM", "SONY", "AMD",
                "CSCO", "IBM", "BLUE", "F", "PLTR", "MARA", "MSFT", "ORAN", "BABA", "META",
                "NVDA", "WMT");

        List<String> randomSymbols = getRandomStockSymbols(enterpriseSymbols, 5); // Change 5 to the desired number of stocks
        System.out.println("Random Stock Symbols: " + randomSymbols);
    }

    private static List<String> getRandomStockSymbols(List<String> symbols, int numberOfStocks) {
        List<String> randomSymbols = new ArrayList<>();

        // Ensure that the number of requested stocks is not greater than the available symbols
        numberOfStocks = Math.min(numberOfStocks, symbols.size());

        // Generate random indices and retrieve corresponding stock symbols
        Random random = new Random();
        for (int i = 0; i < numberOfStocks; i++) {
            int randomIndex = random.nextInt(symbols.size());
            randomSymbols.add(symbols.get(randomIndex));
        }

        return randomSymbols;
    }
}
