package com.percianna.percianna.Services;

import com.percianna.percianna.Entity.*;
import com.percianna.percianna.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SicavServices {

    @Autowired
    private SicavRepository sicavRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private Order2Repository orderRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;


    public void assignSicavToUser(Long sicavId, Long userId, double amountToInvest) {
        // Retrieve Sicav and User entities from the database
        Sicav sicav = sicavRepository.findById(sicavId)
                .orElseThrow(() -> new RuntimeException("Sicav not found with id: " + sicavId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Calculate the total cost for the user to invest in the Sicav
        double totalCost = sicav.getEnterPrice() + amountToInvest;

        if (user.getVirtualBalance() >= totalCost) {
            // Deduct the amount from the user's virtual balance
            double newVirtualBalance = user.getVirtualBalance() - totalCost;
            user.setVirtualBalance(newVirtualBalance);

            // Update the total amount of the Sicav
            double newTotalAmount = sicav.getTotalAmount() + amountToInvest;
            sicav.setTotalAmount(newTotalAmount);


            // Assign the Sicav to the User
            user.setSicav(sicav);

            // Save the updated entities to the database
            userRepository.save(user);
            sicavRepository.save(sicav);
        } else {
            // Rollback the transaction (if applicable)
            // Log or handle the insufficient funds error
            throw new RuntimeException("Insufficient funds to invest in the Sicav. Please check your virtual balance.");
        }
    }


    public void assignStockToSicav(Long sicavId, String stockSymbol) {
        Sicav sicav = sicavRepository.findById(sicavId).orElseThrow(() -> new RuntimeException("SICAV not found"));

        // Fetch the existing stock from the database using the stock symbol
        Stock existingStock = stockRepository.findBySymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found for symbol: " + stockSymbol));


        // Set the Sicav in the existing Stock entity
        existingStock.setSicav(sicav);

        // Add the existing stock to the Sicav's collection of stocks
        sicav.getStocks().add(existingStock);

        // Save the changes
        sicavRepository.save(sicav);
    }

    public  Sicav saveSicav(Sicav sicav){

        return sicavRepository.save(sicav);

    }

    public List<Sicav> getAllSicav(){
        return sicavRepository.findAll();
    }

    /*
    private void createPortfolioEntry2(Sicav sicav) {
        // Fetch stock information based on the stock symbol
        Stock stock = stockRepository.findBySymbol(order.getEnterprise())
                .orElseThrow(() -> new RuntimeException("Stock not found for symbol: " + order.getEnterprise()));

        Portfolio portfolioEntry = new Portfolio();
        portfolioEntry.setUser(sicav.getUser());
        portfolioEntry.setPurchaseDate(sicav.getTimestamp());
        portfolioEntry.setCostBasis(order.getBuyingPrice() * order.getQuantity());
        portfolioEntry.setStockSymbol(sicav.getSymbol());
        portfolioEntry.setStockName(sicav.getName()); // Assuming 'name' is the stock name in the Stock class
        portfolioEntry.setQuantity(1);
        portfolioEntry.setLogo(sicav.getSymbol()); // Assuming 'logo' is the stock logo in the Stock class

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

    public void assignSicavToOrder(Long sicavId, String stockSymbol, int quantity) {
        Sicav sicav = sicavRepository.findById(sicavId).orElseThrow(() -> new RuntimeException("SICAV not found"));

        // Fetch stock information based on the stock symbol
        Stock stock = stockRepository.findBySymbol(stockSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found for symbol: " + stockSymbol));

        // Check if the Sicav has sufficient funds to invest in the order
        double currentPrice = stock.getCurrentPrice();

        if (currentPrice != 0) {

            double requiredAmount = currentPrice * quantity;

            if (sicav.getTotalAmount() >= requiredAmount) {
                // Create a new order
                Order2 order = new Order2();
                order.setSicav(sicav);
                // order.setStock(stock);
                order.setQuantity(quantity);
                order.setPrice(currentPrice);
                order.setBuyingPrice(currentPrice * quantity);
                order.setEnterprise(stockSymbol);
                //order.setTimestamp(new Date());
                order.setStatus(OrderStatus.PENDING);  // Set status as needed

                // Deduct the required amount from the Sicav's total amount
                sicav.setTotalAmount(sicav.getTotalAmount() - requiredAmount);

                // Save the changes
                sicavRepository.save(sicav);
                orderRepository.save(order);
            } else {
                // Rollback the transaction (if applicable)
                // Log or handle the insufficient funds error
                throw new RuntimeException("Insufficient funds in the Sicav to invest in the order.");
            }

        }
        else{
            throw new RuntimeException("The current price for stock " + stock.getSymbol() + " is not available at the moment.");
        }
    }

    @Scheduled(cron = "0 * * * * ?") // Run daily at midnight
    public void updateSicavGrowthPercentages() {
        // Retrieve all Sicavs
        List<Sicav> sicavs = sicavRepository.findAll();

        for (Sicav sicav : sicavs) {
            double currentGrowthPercentage = calculateCurrentGrowthPercentage(sicav.getId());
            sicav.setCurrentGrowthPercentage(currentGrowthPercentage);
        }

        // Save the updated Sicavs
        sicavRepository.saveAll(sicavs);
    }

    public double calculateCurrentGrowthPercentage(Long sicavId) {
        // Retrieve all orders for the given Sicav
        List<Order2> sicavOrders = orderRepository.findOrdersBySicavId(sicavId);

        if (sicavOrders.isEmpty()) {
            // Handle the case when there are no orders for the Sicav
            return 0.0;
        }

        // Calculate the total buying value and total current value of all orders
        double totalBuyingValue = sicavOrders.stream()
                .mapToDouble(order -> order.getBuyingPrice()*1)
                .sum();
        double totalCurrentValue = sicavOrders.stream()
                .mapToDouble(order -> order.getPrice() * order.getQuantity())
                .sum();

        // Calculate the growth percentage
        double growthPercentage = ((totalCurrentValue - totalBuyingValue) / totalBuyingValue) * 100;

        return growthPercentage;
    }


}
