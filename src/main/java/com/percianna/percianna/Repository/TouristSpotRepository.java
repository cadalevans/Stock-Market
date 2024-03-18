package com.percianna.percianna.Repository;

import com.percianna.percianna.Entity.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristSpotRepository extends JpaRepository<HistoricalData,Long> {
}
