package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Order1;
import com.percianna.percianna.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    @Query("SELECT p FROM Portfolio p WHERE p.user.Id = :userId AND p.stockSymbol = :enterpriseSymbol")
    List<Portfolio> findByUserAndStockSymbol(@Param("userId") Long userId, @Param("enterpriseSymbol") String enterpriseSymbol);
    @Query("SELECT p FROM Portfolio p WHERE p.user.Id = :userId")
    List<Portfolio> findAllByUserIdPortfolio(@Param("userId") Long userId);

}
