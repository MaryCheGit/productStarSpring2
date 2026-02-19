package com.projecr.mariya.productStarSpring2.service;

import com.projecr.mariya.productStarSpring2.model.Result;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class CsvParserService {
    
    public List<Result> parseFile(String filePath) throws IOException {
        List<Result> results = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new IOException("Файл не найден: " + filePath);
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int lineNumber = 0;
        
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty()) continue;
            
            try {
                Result result = parseLine(line);
                results.add(result);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка в строке " + lineNumber + ": " + e.getMessage());
            }
        }
        reader.close();
        
        return results;
    }
    
    private Result parseLine(String line) {
        String[] parts = line.split(",");
        
        if (parts.length < 4) {
            throw new IllegalArgumentException("Недостаточно полей. Ожидается 4 поля");
        }

        String fullName = parts[0].trim();
        String gender = parts[1].trim();
        String distance = parts[2].trim();
        String time = parts[3].trim();

        if (fullName.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        if (!gender.equals("М") && !gender.equals("Ж")) {
            throw new IllegalArgumentException("Пол должен быть 'М' или 'Ж'");
        }

        if (!distance.equals("5 км") && !distance.equals("10 км")) {
            throw new IllegalArgumentException("Дистанция должна быть '5 км' или '10 км'");
        }

        if (!time.matches("\\d{1,2}:\\d{2}(:\\d{2})?")) {
            throw new IllegalArgumentException("Неверный формат времени. Ожидается MM:SS или HH:MM:SS");
        }

        return new Result(fullName, gender, distance, time);
    }
}