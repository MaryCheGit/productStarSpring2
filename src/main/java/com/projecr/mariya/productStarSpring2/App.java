package com.projecr.mariya.productStarSpring2;

import com.projecr.mariya.productStarSpring2.config.AppConfig;
import com.projecr.mariya.productStarSpring2.model.Result;
import com.projecr.mariya.productStarSpring2.processor.ResultsProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.IOException;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ResultsProcessor processor = context.getBean(ResultsProcessor.class);
        
        try {
            String currentPath = Paths.get("").toAbsolutePath().toString();
            String filePath = currentPath + "\\results.csv";
            
            System.out.println("Поиск файла: " + filePath);
            processor.loadData(filePath);
            
            System.out.println("\n=== Топ-3 мужчин на дистанции 10 км ===");
            processor.getTopResults("10 км", "М", 3)
                    .forEach(System.out::println);
            
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}