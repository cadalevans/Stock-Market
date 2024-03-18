package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Integer> {
    Optional<Stock> findBySymbol(String symbol);

    @Query("SELECT s FROM Stock s WHERE s.symbol = :symbol")
    Optional<Stock> findStockBySymbol(@Param("symbol") String symbol);
}
