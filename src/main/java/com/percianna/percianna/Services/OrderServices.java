package com.percianna.percianna.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.percianna.percianna.Entity.*;
import com.percianna.percianna.Exception.OrderNotCancellableException;
import com.percianna.percianna.Exception.OrderNotFoundException;
import com.percianna.percianna.Repository.Order1Repository;
import com.percianna.percianna.Repository.PortfolioRepository;
import com.percianna.percianna.Repository.StockRepository;
import com.percianna.percianna.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class OrderServices {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private Order1Repository order1Repository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockServices stockServices;

    @Autowired
    private  StockRepository stockRepository;


    public Order1 assignUserToOrder(Long userId, Order1 order) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Calculate the total cost of the order (price * quantity)
            double totalCost = order.getPrice() * order.getQuantity();

            // Check if the user's balance is sufficient
            if (totalCost <= user.getVirtualBalance()) {
                // Deduct the cost from the user's virtual balance
                user.setVirtualBalance(user.getVirtualBalance() - totalCost);

                // Assign the user to the order
                order.setUser(user);
                //order.setTimestamp(LocalDateTime.now());
                // Save the updated user and order entities
                userRepository.save(user);
                Order1 savedOrder = order1Repository.save(order);
                createPortfolioEntry(savedOrder);
                return savedOrder;

            } else {
                throw new InsufficientBalanceException("Insufficient balance. The order total exceeds the user's balance.");
            }
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }

    // calculate the benefits or loss
/*
    public double calculateBenefit(Long userId, String stockSymbol) {
        // Find all orders for the specified user and stock symbol
        List<Order1> userOrders = order1Repository.findOrdersByUserIdAndEnterpriseSymbol(userId, stockSymbol);

        // Calculate the total investment amount
        double totalInvestment = userOrders.stream()
                .mapToDouble(order -> order.getBuyingPrice() * order.getQuantity())
                .sum();

        // Get the current price of the stock
        double currentPrice = stockServices.getCurrentPrice(stockSymbol);

        // Calculate the total value of the investment at the current price
        double totalValue = userOrders.stream()
                .mapToDouble(order -> currentPrice * order.getQuantity())
                .sum();

        // Calculate the profit or loss
        double benefit = totalValue - totalInvestment;

        return benefit;
    }

 */


    private void createPortfolioEntry(Order1 order) {
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

    private Order1 saveOrder(Order1 order1){
        return order1Repository.save(order1);
    }
    private Order1 getOrder(Long id){
        return order1Repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order1 not found with ID: " + id));
    }
    public Order1 cancelOrder(Long orderId) {
        Optional<Order1> optionalOrder = order1Repository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order1 order1 = optionalOrder.get();
            if (order1.getStatus() == OrderStatus.PENDING) {
                // Set the order1 status to CANCELLED
                order1.setStatus(OrderStatus.CANCELLED);
                // Save the updated order1
                return order1Repository.save(order1);
            } else {
                // Handle cases where the order1 is not cancellable (e.g., already executed)
                throw new OrderNotCancellableException("This order1 cannot be cancelled.");
            }
        } else {
            // Handle cases where the order with the given ID doesn't exist
            throw new OrderNotFoundException("Order1 not found.");
        }
    }

    public List<Portfolio> getAllOrdersByUserIdPortfolio(Long userId) {
        return portfolioRepository.findAllByUserIdPortfolio(userId);
    }

    public List<Order1> getAllOrdersByUserId(Long userId) {
        return order1Repository.findAllByUserId(userId);
    }
    public Order1 updateOrder(Long orderId, Long userId, Order1 updatedOrder) {
        // Find the user
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Find the order by ID and user
            Optional<Order1> existingOrderOptional = order1Repository.findByIdAndUserId(orderId, userId);

            if (existingOrderOptional.isPresent()) {
                Order1 existingOrder = existingOrderOptional.get();

                // Update the fields of the existing order with the values from the updated order
                existingOrder.setOrderSide(updatedOrder.getOrderSide());
                existingOrder.setPrice(updatedOrder.getPrice());
               /*

               existingOrder.setQuantity(updatedOrder.getQuantity());
                existingOrder.setPrice(updatedOrder.getPrice());
                existingOrder.setOrderSide(updatedOrder.getOrderSide());

                */
                // Update other fields as needed

                // Save the updated order
                return order1Repository.save(existingOrder);
            } else {
                throw new EntityNotFoundException("Order not found with id: " + orderId + " for user id: " + userId);
            }
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }
    public List<Order1> getAllSellOrdersWithUsers() {
        return order1Repository.findAllSellOrdersWithUsers();
    }

    @Scheduled(cron = "0 * * * * ?")//(cron = "0 0 */3 * * ?")//(cron = "0 * * * * ?") // Run every hour
    private void updateClosePricesForEnterprises() {
        log.info("Scheduled task triggered at: {}", LocalDateTime.now());

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
        List<User> users = userRepository.findByEnterpriseSymbolInOrders(enterpriseSymbol);

        double totalInvestmentAmounts = 0.0;
        double totalCurrentValues = 0.0;

        for (User user : users) {
            List<Order1> userOrders = order1Repository.findOrdersByUserIdAndEnterpriseSymbol(user.getId(), enterpriseSymbol);
            List<Portfolio> userPortfolio = portfolioRepository.findByUserAndStockSymbol(user.getId(), enterpriseSymbol);

            // Update the close price for each order with the current price from the stock
            for (Order1 order : userOrders) {
                double currentStockPrice = stockServices.getCurrentPrice(enterpriseSymbol);
                order.setPrice(currentStockPrice);
                double investmentAmount = order.getBuyingPrice() * order.getQuantity();
                double currentValue = order.getPrice() * order.getQuantity();
                double x = currentValue - investmentAmount;
                order.setGainLoss(x);
                totalInvestmentAmounts += investmentAmount;
                totalCurrentValues += order.getPrice() * order.getQuantity();
                double percentageGainLoss = ((currentValue - investmentAmount) / investmentAmount) * 100;
                order.setPercentageGainLoss(percentageGainLoss);


            }
            for (Portfolio portfolio: userPortfolio){
                double totalReturn = (stockServices.getCurrentPrice(enterpriseSymbol) * portfolio.getQuantity()) - portfolio.getCostBasis();
                portfolio.setTotalReturn(totalReturn);


            }
            double totalBenefit = totalCurrentValues - totalInvestmentAmounts;
            user.setTotalBenefit(totalBenefit);
            user.setTotalCurrentValue(totalCurrentValues);
            user.setTotalInvestmentAmount(totalInvestmentAmounts);
            userRepository.save(user);
            // Save the updated orders for the user
            order1Repository.saveAll(userOrders);
            portfolioRepository.saveAll(userPortfolio);
        }
    }




    //retrieve the totalbenefit
    public double getTotalBenefitForUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getTotalBenefit();
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
     }

     //retrieve total current value
     public double getTotalCurrentValueForUser(Long userId) {
         Optional<User> userOptional = userRepository.findById(userId);
         if (userOptional.isPresent()) {
             User user = userOptional.get();
             return user.getTotalCurrentValue();
         } else {
             throw new EntityNotFoundException("User not found with id: " + userId);
         }
     }

    //retrieve total investment amount
    public double getTotalInvestmentForUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getTotalInvestmentAmount();
        } else {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
    }

    //Upadate AlphaVantage close price automatically

    /*
    private static final String API_KEY = "VA32VORH1R2ZOW7G";
    private static final String API_BASE_URL = "https://www.alphavantage.co/query";

    @Scheduled(cron = "0 0 * * * ?") // Run every hour, minute
    public void updateClosePricesAutomatically() {

        log.info("Scheduled task triggered at: {}", LocalDateTime.now());
        // Fetch the latest data from Alpha Vantage for a specific enterprise symbol
        List<String> enterpriseSymbols = Arrays.asList("TSLA", "GOOG", "AAPL","PYPL","AMZN","TM","SONY","AMD",
                "CSCO","IBM","BLUE","F","PLTR","MARA","MSFT","ORAN","BABA","META",
                "NVDA","WMT");

        for (String symbol : enterpriseSymbols) {
            double latestClosePrice = fetchLatestClosePriceFromAlphaVantage(symbol);
            updateClosePricesForEnterprise(symbol, latestClosePrice);
        }
    }



    public double fetchLatestClosePriceFromAlphaVantage(String symbol) {
        String url = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=%s", symbol, API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        String alphaVantageResponse = restTemplate.getForObject(url, String.class);

        // Parse the Alpha Vantage response to extract and return the latest close price
        return parseAlphaVantageResponse(alphaVantageResponse);
    }



    private void updateClosePricesForEnterprise(String enterpriseSymbol, double latestClosePrice) {
        // Update close prices in the database for all users with options on the specified enterprise
        List<User> users = userRepository.findByEnterpriseSymbolInOrders(enterpriseSymbol);
        for (User user : users) {
            List<Order1> userOrders = order1Repository.findOrdersByUserIdAndEnterpriseSymbol(user.getId(), enterpriseSymbol);

            // Update the close price for each order with the latest close price
            for (Order1 order : userOrders) {
                order.setPrice(latestClosePrice);
            }

            // Save the updated orders for the user
            order1Repository.saveAll(userOrders);
        }
    }
    private double parseAlphaVantageResponse(String alphaVantageResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(alphaVantageResponse);

            // Get the latest timestamp in the "Time Series (5min)"
            String latestTimestamp = rootNode.path("Time Series (5min)").fieldNames().next();

            // Get the close price for the latest timestamp
            double latestClosePrice = rootNode
                    .path("Time Series (5min)")
                    .path(latestTimestamp)
                    .path("4. close")
                    .asDouble();

            return latestClosePrice;
        } catch (Exception e) {
            // Handle parsing errors
            e.printStackTrace();
            return 0.0; // Return a default value or throw an exception based on your requirements
        }
    }

     */

    ////////////////***********************************
/*
    public void processOrder(String userEmail, String stockSymbol, int quantity) {
        // Get user and stock details
        User user = userRepository.findByEmail(userEmail);
        Stock stock = stockRepository.findBySymbol(stockSymbol);

        // Get the current stock close price
       Double closePrice = 8.3;// ... (fetch from your data source)

                // Calculate total price
                Double bonus = 3.0;  // your bonus
        Double totalPrice = closePrice * quantity + bonus;

        // Check if the user has sufficient virtual balance
        if (user.getVirtualBalance() >= totalPrice) {
            // Deduct the total price from the user's virtual balance
            user.setVirtualBalance(user.getVirtualBalance() - totalPrice);

            // Create an Order1 instance
            Order1 order = new Order1();
            order.setUser(user);
            order.setOrderSide(OrderSide.BUY);  // Assuming this is a buy order
            order.setOrderType(OrderType.LIMIT);  // Assuming this is a limit order
            order.setQuantity(quantity);
            order.setPrice(closePrice);  // Assuming the user is willing to pay the current close price
            order.setTimestamp(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);  // Assuming the order is initially pending

            // Save the order to the database
            order1Repository.save(order);

            // Update the user's portfolio
            Portfolio userPortfolio = portfolioRepository.findByUserAndStock(user, stock);
            if (userPortfolio != null) {
                userPortfolio.setQuantity(userPortfolio.getQuantity().add(BigDecimal.valueOf(quantity)));
            } else {
                userPortfolio = new Portfolio();
                userPortfolio.setUser(user);
                userPortfolio.setStock(stock);
                userPortfolio.setQuantity(BigDecimal.valueOf(quantity));
            }
            portfolioRepository.save(userPortfolio);

            // Save the updated user to the database
            userRepository.save(user);
        } else {
            // Handle insufficient funds (e.g., throw an exception or return an error message)
        }
    }
    */

}
