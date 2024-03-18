package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge,Integer> {
}
