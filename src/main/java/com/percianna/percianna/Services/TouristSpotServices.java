package com.percianna.percianna.Services;

import com.percianna.percianna.Entity.HistoricalData;
import com.percianna.percianna.Repository.TouristSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TouristSpotServices {

    @Autowired
    private TouristSpotRepository touristSpotRepository;



    public HistoricalData saveTouristSpot(HistoricalData historicalData){return touristSpotRepository.save(historicalData);}

    public List<HistoricalData> getAllBankLocations() {
        List<HistoricalData> historicalDataLocations = new ArrayList<>();
        touristSpotRepository.findAll().forEach(historicalDataLocations::add);
        return historicalDataLocations;
    }

}
