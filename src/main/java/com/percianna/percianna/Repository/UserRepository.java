package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByFirstName(String firstName);

    void deleteByFirstName(String firstName);

    User findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN u.orders1 o WHERE o.enterprise = :symbol")
    List<User> findByEnterpriseSymbolInOrders(@Param("symbol") String symbol);
}
