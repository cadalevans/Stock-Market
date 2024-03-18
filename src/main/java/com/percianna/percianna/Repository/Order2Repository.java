package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Order1;
import com.percianna.percianna.Entity.Order2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Order2Repository extends JpaRepository<Order2,Long> {
    /*@Query("SELECT o FROM Order2 o WHERE o.user.Id = :userId")
    List<Order1> findAllByUserId(@Param("userId") Long userId);

     */
    @Query("SELECT o FROM Order2 o WHERE o.id = :orderId AND o.sicav.id = :sicavId")
    Optional<Order2> findByIdAndSicav(@Param("orderId") Long orderId, @Param("sicavId") Long sicavId);
    @Query("SELECT o FROM Order1 o WHERE o.orderSide = 'SELL'")
    List<Order1> findAllSellOrdersWithUsers();

    @Query("SELECT o FROM Order2 o WHERE o.sicav.id = :sicavId AND o.enterprise= :enterpriseSymbol")
    List<Order2> findOrdersBySicavIdAndEnterpriseSymbol(@Param("sicavId") Long userId, @Param("enterpriseSymbol") String enterpriseSymbol);

    List<Order2> findOrdersBySicavId(Long sicavId);
}

