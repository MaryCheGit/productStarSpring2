package com.projecr.mariya.productStarSpring2.processor;


import com.projecr.mariya.productStarSpring2.model.Result;
import com.projecr.mariya.productStarSpring2.service.CsvParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ResultsProcessor {
    
    private final CsvParserService csvParserService;
    private List<Result> allResults;
    
    @Autowired
    public ResultsProcessor(CsvParserService csvParserService) {
        this.csvParserService = csvParserService;
    }
    
    public void loadData(String filePath) throws IOException {
        this.allResults = csvParserService.parseFile(filePath);
        System.out.println("Загружено результатов: " + (allResults != null ? allResults.size() : 0));
    }
    
    public List<Result> getTopResults(String distance, String gender, int n) {
        if (allResults == null || allResults.isEmpty()) {
            throw new IllegalStateException("Данные не загружены. Сначала вызовите loadData()");
        }
        
        if (n <= 0) {
            throw new IllegalArgumentException("Количество результатов должно быть положительным числом");
        }
        
        return allResults.stream()
                .filter(r -> r.getDistance().equals(distance))
                .filter(r -> r.getGender().equals(gender))
                .sorted(Comparator.comparing(Result::getTimeInSeconds))
                .limit(n)
                .collect(Collectors.toList());
    }
    
    public List<Result> getAllResults() {
        return allResults;
    }
    
    public void clearData() {
        this.allResults = null;
        System.out.println("Данные очищены");
    }
}