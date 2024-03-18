package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Sicav;
import com.percianna.percianna.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SicavRepository extends JpaRepository<Sicav,Long> {

    @Query("SELECT DISTINCT s FROM Sicav s JOIN s.orders o WHERE o.enterprise = :symbol")
    List<Sicav> findByEnterpriseSymbolInOrders(@Param("symbol") String symbol);
}
