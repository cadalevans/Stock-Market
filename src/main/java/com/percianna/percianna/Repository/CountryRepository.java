package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Portfolio,Long> {
}
