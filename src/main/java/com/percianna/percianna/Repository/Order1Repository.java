package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Order1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Order1Repository extends JpaRepository<Order1,Long> {
    @Query("SELECT o FROM Order1 o WHERE o.user.Id = :userId")
    List<Order1> findAllByUserId(@Param("userId") Long userId);
    @Query("SELECT o FROM Order1 o WHERE o.id = :orderId AND o.user.Id = :userId")
    Optional<Order1> findByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
    @Query("SELECT o FROM Order1 o WHERE o.orderSide = 'SELL'")
    List<Order1> findAllSellOrdersWithUsers();

    @Query("SELECT o FROM Order1 o WHERE o.user.Id = :userId AND o.enterprise= :enterpriseSymbol")
    List<Order1> findOrdersByUserIdAndEnterpriseSymbol(@Param("userId") Long userId, @Param("enterpriseSymbol") String enterpriseSymbol);


}
