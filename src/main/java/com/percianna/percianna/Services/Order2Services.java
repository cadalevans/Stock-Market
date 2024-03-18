package com.percianna.percianna.Services;

import com.percianna.percianna.Entity.*;
import com.percianna.percianna.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class Order2Services {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private Order2Repository order2Repository;

    @Autowired
    private Order1Repository orderRepository;


    @Autowired
    private SicavRepository sicavRepository;
    @Autowired
    private StockServices stockServices;

    @Autowired
    private StockRepository stockRepository;

/*
    private void createPortfolioEntry(Order2 order) {
        // Fetch stock information based on the stock symbol
        Stock stock = stockRepository.findBySymbol(order.getEnterprise())
                .orElseThrow(() -> new RuntimeException("Stock not found for symbol: " + order.getEnterprise()));

        Portfolio portfolioEntry = new Portfolio();
        portfolioEntry.setUser(order.getUser());
        portfolioEntry.setPurchaseDate(order.getTimestamp());
        portfolioEntry.setCostBasis(order.getBuyingPrice() * order.getQuantity());
        portfolioEntry.setStockSymbol(order.getEnterprise());
        portfolioEntry.setStockName(stock.getName()); // Assuming 'name' is the stock name in the Stock class
        portfolioEntry.setQuantity(order.getQuantity());
        portfolioEntry.setLogo(stock.getLogo()); // Assuming 'logo' is the stock logo in the Stock class

        // Calculate and set other portfolio attributes, such as totalReturn and percentageGainLoss
        double gainLossPercentage = ((order.getPrice() - order.getBuyingPrice()) / order.getBuyingPrice()) * 100;
        portfolioEntry.setCostBasis(gainLossPercentage);
        portfolioEntry.setTotalReturn(order.getPrice() * order.getQuantity());
        // Save the portfolio entry in the database
        double totalReturn = (order.getPrice() * order.getQuantity()) - portfolioEntry.getCostBasis();
        portfolioEntry.setTotalReturn(totalReturn);

        portfolioRepository.save(portfolioEntry);
    }

 */

    public Order2 assignSicavToOrder(Long sicavId, Order2 order) {
        Optional<Sicav> sicavOptional = sicavRepository.findById(sicavId);

        if (sicavOptional.isPresent()) {
            Sicav sicav = sicavOptional.get();

            // Calculate the total cost of the order (price * quantity)
            double totalCost = order.getPrice() * order.getQuantity();

            // Check if the user's balance is sufficient
            if (totalCost <= sicav.getTotalAmount()) {
                // Deduct the cost from the user's virtual balance
                sicav.setTotalAmount(sicav.getTotalAmount() - totalCost);

                // Assign the user to the order
                order.setSicav(sicav);
                //order.setTimestamp(LocalDateTime.now());
                // Save the updated user and order entities
                sicavRepository.save(sicav);
                Order2 savedOrder = order2Repository.save(order);

                return savedOrder;

            } else {
                throw new InsufficientBalanceException("Insufficient balance. The order total exceeds the user's balance.");
            }
        } else {
            throw new EntityNotFoundException("User not found with id: " + sicavId);
        }
    }


    @Scheduled(cron = "0 0 * * * ?") // Run every hour
    private void updateClosePricesForEnterprises() {
        log.info("Scheduled Sicav task triggered at: {}", LocalDateTime.now());

        // Iterate through all enterprise symbols
        List<String> enterpriseSymbols = Arrays.asList("TSLA", "GOOG", "AAPL", "PYPL", "AMZN", "TM", "SONY", "AMD",
                "CSCO", "IBM", "BLUE", "F", "PLTR", "MARA", "MSFT", "ORAN", "BABA", "META",
                "NVDA", "WMT"/* Add other symbols as needed */);

        for (String symbol : enterpriseSymbols) {
            // Update close prices for the specified enterprise symbol
            updateClosePricesForEnterprise(symbol);
        }

    }

    private void updateClosePricesForEnterprise(String enterpriseSymbol) {
        // Update close prices in the database for all users with options on the specified enterprise
        List<Sicav> sicavs = sicavRepository.findByEnterpriseSymbolInOrders(enterpriseSymbol);

        for (Sicav sicav : sicavs) {
            List<Order2> sicavOrders = order2Repository.findOrdersBySicavIdAndEnterpriseSymbol(sicav.getId(), enterpriseSymbol);

            // Update the close price for each order with the current price from the stock
            for (Order2 order : sicavOrders) {
                double currentStockPrice = stockServices.getCurrentPrice(enterpriseSymbol);
                double oldpStockPrice = stockServices.getOldPrice(enterpriseSymbol);
                order.setPrice(currentStockPrice);
                order.setOlderPrice(oldpStockPrice);
                order.setPercentageChange((order.getPrice()-order.getOlderPrice())/100);
                order.setGain((order.getPrice()*order.getQuantity() - order.getBuyingPrice()));

                order2Repository.saveAll(sicavOrders);
            }



            // Save the updated orders for the user


        }

    }
}
